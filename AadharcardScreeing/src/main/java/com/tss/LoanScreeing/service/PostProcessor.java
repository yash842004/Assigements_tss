package com.tss.LoanScreeing.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PostProcessor {

    // PAN Card patterns
    private static final Pattern PAN_PATTERN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    
    // Aadhaar patterns (12 digits, with or without spaces/dashes)
    private static final Pattern AADHAAR_PATTERN = Pattern.compile("\\b(?:\\d{4}[\\s-]?){2}\\d{4}\\b");
    
    // Date patterns (various formats)
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "\\b(?:0?[1-9]|[12]\\d|3[01])[/\\-.](?:0?[1-9]|1[0-2])[/\\-.](?:19|20)?\\d{2}\\b|" +
        "\\b(?:0?[1-9]|1[0-2])[/\\-.](?:0?[1-9]|[12]\\d|3[01])[/\\-.](?:19|20)?\\d{2}\\b"
    );

    public String cleanPanNumber(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "Not Found";
        }

        String cleanedText = rawText.replaceAll("\\s+", "").toUpperCase();
        Matcher matcher = PAN_PATTERN.matcher(cleanedText);
        
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "Not Found";
    }

    public String cleanAadhaarNumber(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "Not Found";
        }

        Matcher matcher = AADHAAR_PATTERN.matcher(rawText);
        
        if (matcher.find()) {
            String aadhaar = matcher.group(0);
            // Remove spaces and dashes, keep only digits
            aadhaar = aadhaar.replaceAll("[\\s-]", "");
            
            // Format as XXXX XXXX XXXX
            if (aadhaar.length() == 12) {
                return aadhaar.substring(0, 4) + " " + 
                       aadhaar.substring(4, 8) + " " + 
                       aadhaar.substring(8, 12);
            }
        }
        return "Not Found";
    }

    public String extractDateOfBirth(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "Not Found";
        }

        Matcher matcher = DATE_PATTERN.matcher(rawText);
        
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "Not Found";
    }

    public String correctName(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "Not Found";
        }

        // Remove special characters and numbers, keep only letters and spaces
        String cleaned = rawText.replaceAll("[^a-zA-Z\\s]", "");
        
        // Remove extra spaces and convert to proper case
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        if (cleaned.length() < 2) {
            return "Not Found";
        }

        // Convert to proper case (First Letter Capital)
        String[] words = cleaned.split("\\s+");
        StringBuilder properCase = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                if (properCase.length() > 0) {
                    properCase.append(" ");
                }
                properCase.append(word.substring(0, 1).toUpperCase())
                         .append(word.substring(1).toLowerCase());
            }
        }
        
        return properCase.toString();
    }

    // Alias method for compatibility
    public String extractName(String rawText) {
        return extractNameFromRawText(rawText);
    }

    public String extractNameFromRawText(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "Not Found";
        }

        String[] lines = rawText.split("\\n");
        
        // Try specific patterns first for Aadhaar cards
        String aadhaarName = extractAadhaarName(rawText);
        if (!"Not Found".equals(aadhaarName)) {
            return aadhaarName;
        }
        
        // Look for name patterns in general ID cards
        for (String line : lines) {
            line = line.trim();
            
            // Skip lines that are too short or contain common non-name text
            if (line.length() < 3) continue;
            
            // Skip lines with government text, numbers, addresses, etc.
            String upperLine = line.toUpperCase();
            if (upperLine.contains("GOVERNMENT") || 
                upperLine.contains("INDIA") ||
                upperLine.contains("AADHAAR") ||
                upperLine.contains("AADHAR") ||
                upperLine.contains("DOB") ||
                upperLine.contains("MY AADHAAR") ||
                upperLine.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*") || // Aadhaar number pattern
                upperLine.matches(".*\\d{2}-\\d{2}-\\d{4}.*") || // Date pattern
                upperLine.matches(".*[0-9]{10,}.*") || // Long numbers
                line.length() > 30) { // Very long lines are usually addresses
                continue;
            }
            
            // Look for lines that look like names (only letters and spaces, reasonable length)
            if (line.matches("^[a-zA-Z\\s]+$") && 
                line.length() >= 3 && 
                line.length() <= 25 &&
                line.split("\\s+").length >= 1 && 
                line.split("\\s+").length <= 4) {
                
                // Additional filtering for common name patterns
                String[] words = line.split("\\s+");
                boolean validName = true;
                
                for (String word : words) {
                    // Each word should be at least 2 characters and start with capital or lowercase
                    if (word.length() < 2 || word.matches(".*[^a-zA-Z].*")) {
                        validName = false;
                        break;
                    }
                }
                
                if (validName) {
                    return correctName(line);
                }
            }
        }
        
        return "Not Found";
    }

    private String extractAadhaarName(String rawText) {
        String[] lines = rawText.split("\\n");
        
        // In Aadhaar cards, the name usually appears after "GOVERNMENT OF INDIA" and before the DOB
        // Look for patterns like "savARTH SHARMA" in the provided example
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            // Look for name patterns that might have OCR artifacts
            if (line.length() >= 5 && line.length() <= 30) {
                // Clean up common OCR misreads
                String cleaned = line.replaceAll("[@(){}\\[\\]]", "")
                                   .replaceAll("\\s+", " ")
                                   .trim();
                
                // Check if this looks like a name (mainly letters with possible OCR artifacts)
                if (cleaned.matches("^[a-zA-Z0-9\\s]{3,25}$")) {
                    // Further clean up numbers that might be misread letters
                    String nameCandidate = cleaned.replaceAll("0", "O")
                                                 .replaceAll("1", "I")
                                                 .replaceAll("5", "S")
                                                 .replaceAll("8", "B")
                                                 .replaceAll("[^a-zA-Z\\s]", "")
                                                 .trim();
                    
                    // Check if this is a valid name pattern
                    if (nameCandidate.matches("^[a-zA-Z\\s]{3,25}$") && 
                        nameCandidate.split("\\s+").length >= 1 &&
                        nameCandidate.split("\\s+").length <= 4) {
                        
                        // Skip common non-name text
                        String upper = nameCandidate.toUpperCase();
                        if (!upper.contains("GOVERNMENT") && 
                            !upper.contains("INDIA") &&
                            !upper.contains("AADHAAR") &&
                            !upper.contains("MY") &&
                            nameCandidate.length() >= 6) { // Names are usually at least 6 characters
                            
                            return correctName(nameCandidate);
                        }
                    }
                }
            }
        }
        
        return "Not Found";
    }

    public String extractAddress(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "Not Found";
        }

        // Look for common address patterns
        String[] lines = rawText.split("\\n");
        StringBuilder address = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            // Skip lines that are too short or contain only numbers/special chars
            if (line.length() > 10 && line.matches(".*[a-zA-Z].*")) {
                // Check if line contains address keywords
                if (line.toLowerCase().matches(".*(address|addr|pin|pincode|dist|district|state|village|city|town).*") ||
                    line.matches(".*\\d{6}.*")) { // Contains pincode
                    
                    if (address.length() > 0) {
                        address.append(", ");
                    }
                    address.append(line);
                }
            }
        }
        
        return address.length() > 0 ? address.toString() : "Not Found";
    }

    public String detectDocumentType(String rawText) {
        if (rawText == null) {
            return "Unknown";
        }

        String upperText = rawText.toUpperCase();
        
        if (upperText.contains("AADHAAR") || upperText.contains("AADHAR") || 
            upperText.contains("UNIQUE IDENTIFICATION") || upperText.contains("UIDAI")) {
            return "AADHAAR";
        } else if (upperText.contains("PERMANENT ACCOUNT NUMBER") || 
                   upperText.contains("PAN") || upperText.contains("INCOME TAX")) {
            return "PAN";
        } else if (upperText.contains("DRIVING") || upperText.contains("LICENCE") || 
                   upperText.contains("LICENSE")) {
            return "DRIVING_LICENSE";
        } else if (upperText.contains("VOTER") || upperText.contains("ELECTION")) {
            return "VOTER_ID";
        } else if (upperText.contains("PASSPORT")) {
            return "PASSPORT";
        }
        
        return "Unknown";
    }

    public double calculateConfidenceScore(String rawText, String extractedName, 
                                         String extractedId, String documentType) {
        double score = 0.0;
        
        // Base score for having raw text
        if (rawText != null && !rawText.trim().isEmpty()) {
            score += 20.0;
        }
        
        // Score for document type detection
        if (!"Unknown".equals(documentType)) {
            score += 25.0;
        }
        
        // Score for name extraction
        if (extractedName != null && !"Not Found".equals(extractedName) && extractedName.length() > 2) {
            score += 25.0;
        }
        
        // Score for ID extraction
        if (extractedId != null && !"Not Found".equals(extractedId)) {
            if ("PAN".equals(documentType) && PAN_PATTERN.matcher(extractedId).matches()) {
                score += 30.0;
            } else if ("AADHAAR".equals(documentType) && extractedId.replaceAll("\\s", "").length() == 12) {
                score += 30.0;
            } else {
                score += 15.0;
            }
        }
        
        return Math.min(100.0, score);
    }
}
