package com.tss.LoanScreeing.service;

import org.springframework.stereotype.Service;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextCorrectionService {

    // Common Indian names dictionary for correction
    private final Set<String> commonIndianNames = new HashSet<>(Arrays.asList(
            "AARAV", "VIVAAN", "ADITYA", "VIHAAN", "ARJUN", "SAI", "REYANSH", "AYAAN", "KRISHNA", "ISHAAN",
            "SHAURYA", "ATHARV", "ADVIK", "PRANAV", "VIVIAN", "MANAN", "YASH", "DHRUV", "ARYAN", "KAVYA",
            "ANAYA", "AADHYA", "SARA", "AANYA", "DIYA", "PIHU", "PRISHA", "ANVI", "RIYA", "MYRA",
            "AAROHI", "NAVYA", "PARI", "SAANVI", "AVNI", "AARYA", "KIARA", "AKSHARA", "VANYA", "SHANAYA",
            "AMIT", "RAHUL", "RAVI", "SURESH", "VIJAY", "AJAY", "SANJAY", "RAKESH", "MANOJ", "ANIL",
            "SUNITA", "PRIYA", "POOJA", "NEETA", "KAVITA", "RITA", "GEETA", "MEERA", "SEEMA", "REKHA",
            "KUMAR", "SHARMA", "SINGH", "VERMA", "GUPTA", "AGARWAL", "JAIN", "BANSAL", "GOEL", "MITTAL",
            "PATEL", "SHAH", "DESAI", "JOSHI", "MEHTA", "TRIVEDI", "PANDYA", "DAVE", "THAKKAR", "GANDHI"
    ));

    // Common address keywords
    private final Set<String> addressKeywords = new HashSet<>(Arrays.asList(
            "HOUSE", "NO", "STREET", "ROAD", "AVENUE", "LANE", "COLONY", "NAGAR", "VIHAR", "APARTMENT",
            "FLAT", "FLOOR", "BUILDING", "TOWER", "COMPLEX", "SECTOR", "BLOCK", "PLOT", "CITY", "TOWN",
            "VILLAGE", "STATE", "DISTRICT", "TEHSIL", "PIN", "PINCODE", "POSTAL", "CODE", "INDIA",
            "NEAR", "BEHIND", "OPPOSITE", "FRONT", "BESIDE", "ADJACENT", "CLOSE", "CROSS", "JUNCTION",
            "CIRCLE", "SQUARE", "MARKET", "BAZAAR", "MALL", "CENTRE", "CENTER", "PARK", "GARDEN"
    ));

    public String correctName(String rawName) {
        if (rawName == null || rawName.trim().isEmpty()) {
            return null;
        }

        String corrected = rawName.trim().toUpperCase()
                .replaceAll("[^A-Z\\s]", "") // Remove non-alphabetic characters
                .replaceAll("\\s+", " "); // Normalize spaces

        // Remove common OCR artifacts
        corrected = corrected
                .replace("0", "O")  // Zero to O
                .replace("5", "S")  // 5 to S
                .replace("1", "I")  // 1 to I
                .replace("8", "B")  // 8 to B
                .replace("6", "G")  // 6 to G
                .replace("2", "Z")  // 2 to Z
                .replace("4", "A"); // 4 to A

        // Split into words and correct each
        String[] words = corrected.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() >= 2) {
                String correctedWord = correctSingleWord(word);
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(correctedWord);
            }
        }

        return result.toString().trim();
    }

    private String correctSingleWord(String word) {
        if (word.length() < 2) {
            return word;
        }

        // Direct match
        if (commonIndianNames.contains(word)) {
            return word;
        }

        // Fuzzy match with common names
        String bestMatch = null;
        int bestScore = 0;

        for (String commonName : commonIndianNames) {
            int score = FuzzySearch.ratio(word, commonName);
            if (score > bestScore && score > 75) { // 75% similarity threshold
                bestScore = score;
                bestMatch = commonName;
            }
        }

        return bestMatch != null ? bestMatch : word;
    }

    public String correctAadhaarNumber(String rawAadhaar) {
        if (rawAadhaar == null) {
            return null;
        }

        // Extract only digits
        String digits = rawAadhaar.replaceAll("[^0-9]", "");

        // Handle common OCR mistakes in numbers
        digits = digits
                .replace("O", "0")  // O to 0
                .replace("o", "0")  // o to 0
                .replace("I", "1")  // I to 1
                .replace("l", "1")  // l to 1
                .replace("S", "5")  // S to 5
                .replace("s", "5")  // s to 5
                .replace("B", "8")  // B to 8
                .replace("G", "6")  // G to 6
                .replace("Z", "2")  // Z to 2
                .replace("A", "4"); // A to 4

        // Check if we have 12 digits
        if (digits.length() == 12) {
            return digits;
        }

        // If more than 12, try to find the best 12-digit sequence
        if (digits.length() > 12) {
            return findBestAadhaarSequence(digits);
        }

        return null; // Less than 12 digits, invalid
    }

    private String findBestAadhaarSequence(String digits) {
        // Look for patterns that suggest Aadhaar structure
        // Aadhaar typically doesn't start with 0 or 1
        for (int i = 0; i <= digits.length() - 12; i++) {
            String candidate = digits.substring(i, i + 12);
            if (!candidate.startsWith("0") && !candidate.startsWith("1")) {
                // Additional validation could be added here
                return candidate;
            }
        }

        // Fallback to first 12 digits
        return digits.substring(0, 12);
    }

    public String correctDateOfBirth(String rawDob) {
        if (rawDob == null || rawDob.trim().isEmpty()) {
            return null;
        }

        String cleaned = rawDob.trim();

        // Common OCR corrections for dates
        cleaned = cleaned
                .replace("O", "0")
                .replace("o", "0")
                .replace("I", "1")
                .replace("l", "1")
                .replace("S", "5")
                .replace("B", "8")
                .replace("G", "6")
                .replace("Z", "2");

        // Try to match different date patterns
        Pattern[] datePatterns = {
                Pattern.compile("(\\d{2})[/-](\\d{2})[/-](\\d{4})"), // DD/MM/YYYY or DD-MM-YYYY
                Pattern.compile("(\\d{1,2})\\s+(\\w{3})\\s+(\\d{4})"), // DD MMM YYYY
                Pattern.compile("(\\d{4})[/-](\\d{2})[/-](\\d{2})") // YYYY/MM/DD or YYYY-MM-DD
        };

        for (Pattern pattern : datePatterns) {
            Matcher matcher = pattern.matcher(cleaned);
            if (matcher.find()) {
                return matcher.group();
            }
        }

        return cleaned; // Return as is if no pattern matches
    }

    public String correctAddress(String rawAddress) {
        if (rawAddress == null || rawAddress.trim().isEmpty()) {
            return null;
        }

        String corrected = rawAddress.trim().toUpperCase();

        // Common OCR corrections
        corrected = corrected
                .replace("0", "O") // In addresses, 0 is more likely O in words
                .replace("1", "I") // 1 to I in words
                .replace("5", "S") // 5 to S
                .replace("8", "B") // 8 to B
                .replace("6", "G"); // 6 to G

        // Correct common address abbreviations
        corrected = corrected
                .replaceAll("\\bNO\\b", "NO.")
                .replaceAll("\\bST\\b", "STREET")
                .replaceAll("\\bRD\\b", "ROAD")
                .replaceAll("\\bAVE\\b", "AVENUE")
                .replaceAll("\\bAPT\\b", "APARTMENT")
                .replaceAll("\\bBLDG\\b", "BUILDING");

        // Normalize spaces
        corrected = corrected.replaceAll("\\s+", " ");

        return corrected.trim();
    }

    public String correctGender(String rawGender) {
        if (rawGender == null || rawGender.trim().isEmpty()) {
            return null;
        }

        String cleaned = rawGender.trim().toUpperCase();

        // Common OCR corrections
        cleaned = cleaned
                .replace("0", "O")
                .replace("1", "I")
                .replace("5", "S")
                .replace("8", "B");

        // Gender matching
        if (cleaned.contains("M") && !cleaned.contains("F")) {
            return "MALE";
        } else if (cleaned.contains("F")) {
            return "FEMALE";
        }

        // Fuzzy matching
        int maleScore = FuzzySearch.ratio(cleaned, "MALE");
        int femaleScore = FuzzySearch.ratio(cleaned, "FEMALE");

        if (maleScore > femaleScore && maleScore > 60) {
            return "MALE";
        } else if (femaleScore > 60) {
            return "FEMALE";
        }

        return null;
    }

    public double calculateTextQuality(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }

        double score = 100.0;
        
        // Penalize for non-printable characters
        long nonPrintable = text.chars().filter(c -> c < 32 || c > 126).count();
        score -= (nonPrintable * 2.0);

        // Penalize for excessive punctuation/special characters
        long specialChars = text.chars().filter(c -> !Character.isLetterOrDigit(c) && c != ' ').count();
        double specialRatio = (double) specialChars / text.length();
        if (specialRatio > 0.3) {
            score -= (specialRatio * 50);
        }

        // Bonus for proper capitalization patterns
        if (text.matches(".*[A-Z][a-z].*")) {
            score += 5;
        }

        // Bonus for common word patterns
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (commonIndianNames.contains(word.toUpperCase()) || 
                addressKeywords.contains(word.toUpperCase())) {
                score += 2;
            }
        }

        return Math.max(0, Math.min(100, score));
    }

    public Map<String, Double> validateExtractedData(String name, String aadhaar, String dob, String gender, String address) {
        Map<String, Double> confidenceScores = new HashMap<>();

        // Name validation
        if (name != null) {
            double nameScore = calculateTextQuality(name);
            if (name.matches("[A-Za-z\\s]{2,50}")) {
                nameScore += 10;
            }
            confidenceScores.put("name", Math.min(100, nameScore));
        } else {
            confidenceScores.put("name", 0.0);
        }

        // Aadhaar validation
        if (aadhaar != null && aadhaar.matches("\\d{12}")) {
            confidenceScores.put("aadhaar", 95.0);
        } else {
            confidenceScores.put("aadhaar", 0.0);
        }

        // DOB validation
        if (dob != null) {
            double dobScore = dob.matches("\\d{2}[/-]\\d{2}[/-]\\d{4}") ? 90.0 : 50.0;
            confidenceScores.put("dob", dobScore);
        } else {
            confidenceScores.put("dob", 0.0);
        }

        // Gender validation
        if (gender != null && (gender.equalsIgnoreCase("MALE") || gender.equalsIgnoreCase("FEMALE"))) {
            confidenceScores.put("gender", 95.0);
        } else {
            confidenceScores.put("gender", 0.0);
        }

        // Address validation
        if (address != null) {
            double addressScore = calculateTextQuality(address);
            if (address.length() > 20) {
                addressScore += 10;
            }
            confidenceScores.put("address", Math.min(100, addressScore));
        } else {
            confidenceScores.put("address", 0.0);
        }

        return confidenceScores;
    }

    /**
     * Fast text correction from multiple OCR results (optimized for performance)
     */
    public String correctTextFromMultipleResults(List<String> ocrResults) {
        if (ocrResults == null || ocrResults.isEmpty()) {
            return null;
        }
        
        if (ocrResults.size() == 1) {
            return applyBasicCorrections(ocrResults.get(0));
        }
        
        // Quick selection: choose longest result that passes basic validation
        String bestResult = ocrResults.stream()
            .filter(result -> result != null && result.trim().length() > 2)
            .max((a, b) -> Integer.compare(calculateQuickScore(a), calculateQuickScore(b)))
            .orElse(ocrResults.get(0));
            
        return applyBasicCorrections(bestResult);
    }
    
    /**
     * Quick scoring for fast result selection
     */
    private int calculateQuickScore(String text) {
        if (text == null) return 0;
        
        int score = text.length(); // Longer is often better
        
        // Bonus for alphanumeric content
        long alphanumeric = text.chars().filter(Character::isLetterOrDigit).count();
        score += (int)(alphanumeric * 0.5);
        
        // Penalty for excessive special characters
        long special = text.chars().filter(c -> !Character.isLetterOrDigit(c) && c != ' ').count();
        score -= special * 2;
        
        return score;
    }
    
    /**
     * Fast basic corrections (simplified version)
     */
    private String applyBasicCorrections(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        
        // Apply only essential character corrections
        String corrected = text
            .replace("0", "O") // In text context, 0 is likely O
            .replace("1", "I") // In text context, 1 is likely I
            .replace("5", "S") // 5 to S
            .replaceAll("\\s+", " ") // Clean up spaces
            .trim();
            
        return corrected;
    }
    
    /**
     * Correct a single text string using all available correction techniques
     */
    public String correctSingleText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        
        String corrected = text.trim();
        
        // Apply character-level corrections
        corrected = applyCharacterCorrections(corrected);
        
        // Apply word-level corrections using fuzzy matching
        corrected = applyWordLevelCorrections(corrected);
        
        // Apply pattern-based corrections for structured data
        corrected = applyPatternCorrections(corrected);
        
        return corrected;
    }
    
    /**
     * Select the best result from multiple OCR outputs
     */
    private String selectBestFromMultiple(List<String> results) {
        if (results.size() == 1) {
            return results.get(0);
        }
        
        String bestResult = "";
        double bestScore = 0.0;
        
        for (String result : results) {
            double score = calculateResultQuality(result);
            if (score > bestScore) {
                bestScore = score;
                bestResult = result;
            }
        }
        
        // If no single result is clearly better, try consensus approach
        if (bestScore < 70.0) {
            return buildConsensusResult(results);
        }
        
        return bestResult;
    }
    
    /**
     * Build consensus result by taking the most common characters at each position
     */
    private String buildConsensusResult(List<String> results) {
        if (results.isEmpty()) {
            return "";
        }
        
        // Find the longest result as reference
        String longest = results.stream()
            .max((s1, s2) -> Integer.compare(s1.length(), s2.length()))
            .orElse("");
        
        StringBuilder consensus = new StringBuilder();
        
        for (int i = 0; i < longest.length(); i++) {
            Map<Character, Integer> charCounts = new HashMap<>();
            
            // Count character occurrences at position i
            for (String result : results) {
                if (i < result.length()) {
                    char c = result.charAt(i);
                    charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
                }
            }
            
            // Select most common character
            char mostCommon = charCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(longest.charAt(i));
                
            consensus.append(mostCommon);
        }
        
        return consensus.toString();
    }
    
    /**
     * Calculate quality score for a single OCR result
     */
    private double calculateResultQuality(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        
        double score = 50.0; // Base score
        
        // Length score (reasonable lengths get bonus)
        int length = text.length();
        if (length >= 10 && length <= 200) {
            score += 10;
        } else if (length < 5) {
            score -= 20;
        }
        
        // Character diversity score
        long uniqueChars = text.chars().distinct().count();
        if (uniqueChars > length / 3) {
            score += 10;
        }
        
        // Alphanumeric ratio
        long alphanumeric = text.chars().filter(Character::isLetterOrDigit).count();
        double alphaRatio = (double) alphanumeric / length;
        if (alphaRatio > 0.8) {
            score += 15;
        }
        
        // Known word bonus
        String[] words = text.toLowerCase().split("\\s+");
        int knownWords = 0;
        for (String word : words) {
            if (commonIndianNames.contains(word.toUpperCase()) || 
                addressKeywords.contains(word.toUpperCase())) {
                knownWords++;
            }
        }
        if (words.length > 0) {
            score += (knownWords * 20.0 / words.length);
        }
        
        return Math.min(100.0, Math.max(0.0, score));
    }
    
    /**
     * Apply character-level corrections for common OCR mistakes
     */
    private String applyCharacterCorrections(String text) {
        String corrected = text;
        
        // Context-aware character corrections
        Map<String, String> corrections = new HashMap<>();
        corrections.put("0(?=[A-Za-z])", "O"); // 0 to O before letters
        corrections.put("O(?=\\d)", "0");      // O to 0 before digits
        corrections.put("1(?=[A-Za-z])", "I"); // 1 to I before letters
        corrections.put("I(?=\\d)", "1");      // I to 1 before digits
        corrections.put("5(?=[A-Za-z])", "S"); // 5 to S before letters
        corrections.put("S(?=\\d)", "5");      // S to 5 before digits
        
        for (Map.Entry<String, String> entry : corrections.entrySet()) {
            corrected = corrected.replaceAll(entry.getKey(), entry.getValue());
        }
        
        return corrected;
    }
    
    /**
     * Apply word-level corrections using fuzzy matching
     */
    private String applyWordLevelCorrections(String text) {
        String[] words = text.split("\\s+");
        StringBuilder corrected = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                corrected.append(" ");
            }
            
            String word = words[i];
            String cleanWord = word.replaceAll("[^A-Za-z0-9]", "");
            
            if (cleanWord.length() >= 3) {
                String bestMatch = findBestFuzzyMatch(cleanWord);
                if (bestMatch != null) {
                    // Preserve punctuation and case
                    String correctedWord = preserveWordFormatting(word, bestMatch);
                    corrected.append(correctedWord);
                } else {
                    corrected.append(word);
                }
            } else {
                corrected.append(word);
            }
        }
        
        return corrected.toString();
    }
    
    /**
     * Find best fuzzy match for a word
     */
    private String findBestFuzzyMatch(String word) {
        String upperWord = word.toUpperCase();
        
        // Direct match first
        if (commonIndianNames.contains(upperWord) || addressKeywords.contains(upperWord)) {
            return upperWord;
        }
        
        // Fuzzy match
        String bestMatch = null;
        int bestScore = 0;
        int threshold = Math.max(70, 100 - (word.length() * 5)); // Adaptive threshold
        
        Set<String> allWords = new HashSet<>();
        allWords.addAll(commonIndianNames);
        allWords.addAll(addressKeywords);
        
        for (String candidate : allWords) {
            int score = FuzzySearch.ratio(upperWord, candidate);
            if (score > bestScore && score >= threshold) {
                bestScore = score;
                bestMatch = candidate;
            }
        }
        
        return bestMatch;
    }
    
    /**
     * Preserve original word formatting while applying corrections
     */
    private String preserveWordFormatting(String original, String corrected) {
        if (original.length() == 0 || corrected.length() == 0) {
            return original;
        }
        
        StringBuilder result = new StringBuilder();
        int correctedIndex = 0;
        
        for (int i = 0; i < original.length() && correctedIndex < corrected.length(); i++) {
            char originalChar = original.charAt(i);
            
            if (Character.isLetter(originalChar)) {
                char correctedChar = corrected.charAt(correctedIndex++);
                if (Character.isUpperCase(originalChar)) {
                    result.append(Character.toUpperCase(correctedChar));
                } else {
                    result.append(Character.toLowerCase(correctedChar));
                }
            } else if (Character.isDigit(originalChar)) {
                if (correctedIndex < corrected.length() && Character.isDigit(corrected.charAt(correctedIndex))) {
                    result.append(corrected.charAt(correctedIndex++));
                } else {
                    result.append(originalChar);
                }
            } else {
                result.append(originalChar);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Apply pattern-based corrections for structured data
     */
    private String applyPatternCorrections(String text) {
        String corrected = text;
        
        // Aadhaar number pattern correction
        Pattern aadhaarCandidate = Pattern.compile("\\b\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}\\b");
        corrected = aadhaarCandidate.matcher(corrected).replaceAll(match -> {
            String number = match.group().replaceAll("[\\s-]", "");
            if (number.length() == 12) {
                return number.substring(0, 4) + " " + number.substring(4, 8) + " " + number.substring(8, 12);
            }
            return match.group();
        });
        
        // PIN code pattern correction
        Pattern pinCandidate = Pattern.compile("\\b\\d{6}\\b");
        corrected = pinCandidate.matcher(corrected).replaceAll(match -> {
            String pin = match.group();
            // Basic PIN validation (not starting with 0, reasonable range)
            if (!pin.startsWith("0") && Integer.parseInt(pin) >= 100000) {
                return pin;
            }
            return match.group();
        });
        
        // Date pattern corrections
        Pattern dateCandidate = Pattern.compile("\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{4}\\b");
        corrected = dateCandidate.matcher(corrected).replaceAll(match -> {
            String date = match.group();
            // Normalize date format
            return date.replaceAll("/", "-");
        });
        
        return corrected;
    }
    
    /**
     * Enhanced Aadhaar number extraction with improved pattern recognition
     */
    public String extractAadhaarFromText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        
        // Clean text and look for 12-digit sequences
        String cleanText = text.replaceAll("[^0-9\\s\\n-]", " ");
        
        // Multiple patterns to catch different OCR variations
        Pattern[] aadhaarPatterns = {
            // Standard format: 1234 5678 9012
            Pattern.compile("\\b(\\d{4})\\s+(\\d{4})\\s+(\\d{4})\\b"),
            // With hyphens: 1234-5678-9012
            Pattern.compile("\\b(\\d{4})[-](\\d{4})[-](\\d{4})\\b"),
            // Minimal spacing: 1234 5678 9012
            Pattern.compile("\\b(\\d{4})\\s*(\\d{4})\\s*(\\d{4})\\b"),
            // Numbers across lines
            Pattern.compile("(\\d{4})\\s*\\n\\s*(\\d{4})\\s*\\n\\s*(\\d{4})"),
            // Continuous 12 digits
            Pattern.compile("\\b(\\d{12})\\b")
        };
        
        for (Pattern pattern : aadhaarPatterns) {
            Matcher matcher = pattern.matcher(cleanText);
            while (matcher.find()) {
                String number;
                
                if (pattern.pattern().contains("(\\d{12})")) {
                    // For continuous 12 digits
                    number = matcher.group(1);
                } else if (matcher.groupCount() == 3) {
                    // For patterns with 3 groups
                    number = matcher.group(1) + matcher.group(2) + matcher.group(3);
                } else {
                    number = matcher.group().replaceAll("[^0-9]", "");
                }
                
                // Validate Aadhaar number
                if (isValidAadhaarNumber(number)) {
                    return formatAadhaarNumber(number);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Validate if a number could be a valid Aadhaar number
     */
    private boolean isValidAadhaarNumber(String number) {
        if (number == null || number.length() != 12) {
            return false;
        }
        
        // Basic validation rules
        if (number.startsWith("0") || number.startsWith("1")) {
            return false;
        }
        
        // Check if all digits are the same (unlikely for real Aadhaar)
        if (number.chars().distinct().count() == 1) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Format Aadhaar number as XXXX XXXX XXXX
     */
    private String formatAadhaarNumber(String number) {
        if (number == null || number.length() != 12) {
            return number;
        }
        return number.substring(0, 4) + " " + number.substring(4, 8) + " " + number.substring(8, 12);
    }

    /**
     * Advanced Aadhaar card specific text extraction with layout recognition
     */
    public String extractNameFromAadhaarLayout(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return null;
        }
        
        String[] lines = rawText.split("\\n");
        
        // Strategy 1: Look for name after "Government of India" or before Aadhaar number
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            // If this line contains "Government" or "India", the name is likely in the next few lines
            if (line.toLowerCase().contains("government") || line.toLowerCase().contains("india")) {
                for (int j = i + 1; j < Math.min(i + 4, lines.length); j++) {
                    String candidateName = extractNameFromLine(lines[j]);
                    if (candidateName != null && candidateName.length() > 2) {
                        return candidateName;
                    }
                }
            }
            
            // If this line has an Aadhaar number, look for name in previous lines
            if (line.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*")) {
                for (int j = Math.max(0, i - 3); j < i; j++) {
                    String candidateName = extractNameFromLine(lines[j]);
                    if (candidateName != null && candidateName.length() > 2) {
                        return candidateName;
                    }
                }
            }
        }
        
        // Strategy 2: Look for longest alphabetic line that looks like a name
        String bestNameCandidate = null;
        int bestScore = 0;
        
        for (String line : lines) {
            String candidate = extractNameFromLine(line);
            if (candidate != null) {
                int score = calculateNameScore(candidate);
                if (score > bestScore) {
                    bestScore = score;
                    bestNameCandidate = candidate;
                }
            }
        }
        
        return bestNameCandidate;
    }
    
    /**
     * Extract name from a single line
     */
    private String extractNameFromLine(String line) {
        if (line == null) return null;
        
        String cleaned = line.trim().toUpperCase();
        
        // Skip lines that are clearly not names
        if (cleaned.length() < 3 || cleaned.length() > 50) return null;
        if (cleaned.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*")) return null; // Contains Aadhaar
        if (cleaned.toLowerCase().contains("government")) return null;
        if (cleaned.toLowerCase().contains("authority")) return null;
        if (cleaned.toLowerCase().contains("aadhaar")) return null;
        if (cleaned.toLowerCase().contains("unique")) return null;
        if (cleaned.toLowerCase().contains("identification")) return null;
        if (cleaned.matches(".*\\d{2}[/-]\\d{2}[/-]\\d{4}.*")) return null; // Contains date
        
        // Must be mostly alphabetic
        long alphaCount = cleaned.chars().filter(Character::isLetter).count();
        if (alphaCount < cleaned.length() * 0.7) return null;
        
        // Apply character corrections
        cleaned = applyCharacterCorrections(cleaned);
        
        return cleaned;
    }
    
    /**
     * Calculate score for how likely a string is to be a name
     */
    private int calculateNameScore(String candidate) {
        int score = 0;
        
        // Length bonus (names are typically 5-30 characters)
        if (candidate.length() >= 5 && candidate.length() <= 30) {
            score += 20;
        }
        
        // Word count bonus (1-4 words is typical for names)
        String[] words = candidate.split("\\s+");
        if (words.length >= 1 && words.length <= 4) {
            score += 15;
        }
        
        // Known name bonus
        for (String word : words) {
            if (commonIndianNames.contains(word)) {
                score += 25;
            }
        }
        
        // Capitalization pattern bonus
        if (candidate.matches("[A-Z][a-z]*(?:\\s+[A-Z][a-z]*)*")) {
            score += 10;
        }
        
        return score;
    }
    
    /**
     * Advanced address extraction from Aadhaar layout
     */
    public String extractAddressFromAadhaarLayout(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return null;
        }
        
        String[] lines = rawText.split("\\n");
        StringBuilder addressBuilder = new StringBuilder();
        boolean foundName = false;
        boolean foundAadhaarNumber = false;
        
        // Strategy 1: Look for address between name and Aadhaar number
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            // Skip empty lines and very short lines
            if (line.length() < 3) continue;
            
            // Check if this line contains a name (typically after "Government of India")
            if (isLikelyName(line) && !foundName) {
                foundName = true;
                continue;
            }
            
            // Check if this line contains Aadhaar number
            if (line.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*")) {
                foundAadhaarNumber = true;
                break; // Stop here, address should be before Aadhaar number
            }
            
            // If we found the name, collect address lines until Aadhaar number
            if (foundName && !foundAadhaarNumber) {
                String addressPart = extractAddressFromLine(line);
                if (addressPart != null && addressPart.length() > 2) {
                    // Skip if it looks like DOB or gender
                    if (!line.matches(".*\\d{2}[/-]\\d{2}[/-]\\d{4}.*") && 
                        !line.toLowerCase().matches(".*(male|female).*")) {
                        
                        if (addressBuilder.length() > 0) {
                            addressBuilder.append(", ");
                        }
                        addressBuilder.append(addressPart);
                    }
                }
            }
        }
        
        // Strategy 2: If no address found with first strategy, look for address patterns
        if (addressBuilder.length() == 0) {
            for (String line : lines) {
                if (line.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*")) continue; // Skip Aadhaar line
                if (isLikelyName(line)) continue; // Skip name line
                
                String addressPart = extractAddressFromLine(line);
                if (addressPart != null && addressPart.length() > 5) {
                    // Look for address indicators
                    String lowerLine = addressPart.toLowerCase();
                    if (lowerLine.contains("nagar") || lowerLine.contains("colony") || 
                        lowerLine.contains("delhi") || lowerLine.contains("mumbai") ||
                        lowerLine.contains("bangalore") || lowerLine.contains("chennai") ||
                        addressPart.matches(".*\\d+.*")) { // Contains numbers (house/flat number)
                        
                        if (addressBuilder.length() > 0) {
                            addressBuilder.append(", ");
                        }
                        addressBuilder.append(addressPart);
                    }
                }
            }
        }
        
        String result = addressBuilder.toString().trim();
        return result.length() > 0 ? correctAddress(result) : null;
    }
    
    /**
     * Extract address components from a single line
     */
    private String extractAddressFromLine(String line) {
        if (line == null) return null;
        
        String cleaned = line.trim();
        
        // Skip lines that are clearly not addresses
        if (cleaned.length() < 2) return null;
        if (cleaned.toLowerCase().contains("government")) return null;
        if (cleaned.toLowerCase().contains("authority")) return null;
        if (cleaned.toLowerCase().contains("aadhaar")) return null;
        if (cleaned.toLowerCase().contains("unique")) return null;
        if (cleaned.toLowerCase().contains("identification")) return null;
        if (cleaned.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*")) return null; // Contains Aadhaar
        if (cleaned.matches(".*\\d{2}[/-]\\d{2}[/-]\\d{4}.*")) return null; // Contains date
        if (cleaned.toLowerCase().matches(".*(male|female).*")) return null; // Contains gender
        
        // Allow lines that look like addresses
        String lowerLine = cleaned.toLowerCase();
        
        // Check for city names
        String[] cities = {"delhi", "mumbai", "bangalore", "chennai", "kolkata", "hyderabad", "pune", "ahmedabad"};
        for (String city : cities) {
            if (lowerLine.contains(city)) {
                return cleaned;
            }
        }
        
        // Check for address components
        String[] addressIndicators = {"colony", "street", "road", "house", "no", "nagar", "vihar", 
                                     "apartment", "flat", "building", "sector", "block", "plot", 
                                     "pin", "dist", "state", "near", "raj", "new"};
        
        for (String indicator : addressIndicators) {
            if (lowerLine.contains(indicator)) {
                return cleaned;
            }
        }
        
        // If it contains numbers (house/flat number) and letters, it could be an address
        long digitCount = cleaned.chars().filter(Character::isDigit).count();
        long letterCount = cleaned.chars().filter(Character::isLetter).count();
        if (digitCount > 0 && letterCount > 0 && letterCount > digitCount) {
            return cleaned;
        }
        
        // If it has comma-separated parts, it might be an address
        if (cleaned.contains(",") && cleaned.split(",").length > 1) {
            return cleaned;
        }
        
        return null;
    }
    
    /**
     * Check if a line is likely to be a name
     */
    private boolean isLikelyName(String line) {
        if (line == null || line.length() < 3 || line.length() > 50) {
            return false;
        }
        
        String upper = line.toUpperCase().trim();
        
        // Check if it's mostly alphabetic
        long alphaCount = upper.chars().filter(Character::isLetter).count();
        if (alphaCount < line.length() * 0.7) {
            return false;
        }
        
        // Check against known names
        String[] words = upper.split("\\s+");
        for (String word : words) {
            if (commonIndianNames.contains(word)) {
                return true;
            }
        }
        
        // Check if it looks like a proper name (capitalized words)
        if (line.matches("^[A-Z][a-z]+(?:\\s+[A-Z][a-z]+)*$")) {
            return true;
        }
        
        return false;
    }
}
