package com.tss.LoanScreeing.service;

import com.tss.LoanScreeing.model.ExtractedData;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UltraHighAccuracyOcrService {

    @Autowired
    private ImageProcessor imageProcessor;

    @Autowired
    private ImageQualityAnalyzer qualityAnalyzer;

    @Autowired
    private PostProcessor postProcessor;
    
    @Autowired
    private TextCorrectionService textCorrectionService;

    private final List<OcrConfiguration> ocrConfigurations;
    private final Map<String, Pattern> patterns;

    public UltraHighAccuracyOcrService() {
        this.ocrConfigurations = initializeOcrConfigurations();
        this.patterns = initializePatterns();
    }

    private List<OcrConfiguration> initializeOcrConfigurations() {
        List<OcrConfiguration> configs = new ArrayList<>();

        // Configuration 1: Standard accuracy (most reliable)
        configs.add(new OcrConfiguration("standard", 6, 1, 
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,/-()"));

        // Configuration 2: Only add specialized config if needed
        configs.add(new OcrConfiguration("optimized", 3, 1,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/-()"));

        // Removed extra configurations for performance
        configs.add(new OcrConfiguration("numbers", 8, 1, "0123456789 "));

        // Configuration 4: Address focused
        configs.add(new OcrConfiguration("address", 6, 1,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789.,/-():"));

        // Configuration 5: Single character (for difficult text)
        configs.add(new OcrConfiguration("single_char", 10, 1,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789"));

        return configs;
    }

    private Map<String, Pattern> initializePatterns() {
        Map<String, Pattern> patterns = new HashMap<>();
        
        // Enhanced Aadhaar patterns
        patterns.put("aadhaar_spaced", Pattern.compile("\\b\\d{4}\\s+\\d{4}\\s+\\d{4}\\b"));
        patterns.put("aadhaar_continuous", Pattern.compile("\\b\\d{12}\\b"));
        patterns.put("aadhaar_dashed", Pattern.compile("\\b\\d{4}-\\d{4}-\\d{4}\\b"));
        
        // Name patterns
        patterns.put("name_after_colon", Pattern.compile("(?i)name[:\\s]+([A-Za-z\\s]{2,50})"));
        patterns.put("name_standalone", Pattern.compile("\\b[A-Z][a-z]+(?:\\s+[A-Z][a-z]+){1,3}\\b"));
        
        // DOB patterns
        patterns.put("dob_ddmmyyyy", Pattern.compile("\\b(\\d{2}[/-]\\d{2}[/-]\\d{4})\\b"));
        patterns.put("dob_ddmmmyyyy", Pattern.compile("\\b(\\d{1,2}\\s+(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+\\d{4})\\b"));
        
        // Gender patterns
        patterns.put("gender", Pattern.compile("(?i)\\b(male|female|m|f)\\b"));
        
        return patterns;
    }

    public ExtractedData extractWithUltraHighAccuracy(BufferedImage image) throws TesseractException {
        // Step 1: Analyze image quality
        ImageQualityAnalyzer.ImageQualityMetrics metrics = qualityAnalyzer.analyzeImage(image);
        System.out.println("Image Quality Analysis: " + metrics);

        // Step 2: Apply optimal preprocessing
        ImageQualityAnalyzer.PreprocessingStrategy strategy = qualityAnalyzer.recommendStrategy(metrics);
        System.out.println("Recommended Strategy: " + strategy);

        List<BufferedImage> preprocessedVariants = createPreprocessedVariants(image, strategy);
        
        // Step 3: Run multiple OCR passes with different configurations
        Map<String, ConfidenceScore> results = new HashMap<>();
        
        for (BufferedImage variant : preprocessedVariants) {
            for (OcrConfiguration config : ocrConfigurations) {
                try {
                    String text = performOcr(variant, config);
                    if (text != null && !text.trim().isEmpty()) {
                        ExtractedData data = extractDataFromText(text, config.name);
                        if (data != null) {
                            updateResultsWithConfidence(results, data, config, metrics);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("OCR failed with config " + config.name + ": " + e.getMessage());
                }
            }
        }

        // Step 4: Select best results based on confidence scores
        return selectBestResults(results);
    }

    private List<BufferedImage> createPreprocessedVariants(BufferedImage original, 
                                                          ImageQualityAnalyzer.PreprocessingStrategy strategy) {
        List<BufferedImage> variants = new ArrayList<>();
        
        try {
            // Aadhaar-specific preprocessing (primary method)
            BufferedImage aadhaarOptimized = preprocessForAadhaarCard(original);
            variants.add(aadhaarOptimized);
            
            // Original with recommended preprocessing
            BufferedImage optimized = imageProcessor.preprocessImage(original);
            variants.add(optimized);
            
            // High contrast variant for text clarity
            BufferedImage highContrast = enhanceContrast(original, 1.8);
            variants.add(highContrast);
            
            // Brightness adjusted variants
            if (strategy.brightnessAdjustment != 1.0) {
                BufferedImage brightAdjusted = adjustBrightness(original, strategy.brightnessAdjustment);
                variants.add(brightAdjusted);
            }
            
            // Binarized variant (black and white)
            BufferedImage binarized = binarizeImage(original);
            variants.add(binarized);
            
        } catch (Exception e) {
            System.err.println("Error creating preprocessed variants: " + e.getMessage());
            variants.add(original); // Fallback to original
        }
        
        return variants;
    }

    private List<BufferedImage> createOptimizedVariants(BufferedImage image, ImageQualityAnalyzer.PreprocessingStrategy strategy) {
        List<BufferedImage> variants = new ArrayList<>();
        
        try {
            // Variant 1: Standard preprocessing (most reliable)
            BufferedImage standard = imageProcessor.preprocessImage(image);
            variants.add(standard);
            
            // Variant 2: High contrast for text clarity (if image quality is poor)
            if (strategy.brightnessAdjustment < 0.8 || strategy.contrastEnhancement) {
                BufferedImage highContrast = enhanceContrast(image, 1.8);
                variants.add(highContrast);
            }
            
            // Variant 3: Only add binarized version if really needed
            BufferedImage binarized = binarizeImage(image);
            variants.add(binarized);
            
        } catch (Exception e) {
            System.err.println("Error creating optimized variants: " + e.getMessage());
            variants.add(image); // Fallback to original
        }
        
        return variants;
    }

    private BufferedImage enhanceContrast(BufferedImage image, double factor) {
        BufferedImage enhanced = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color original = new Color(image.getRGB(x, y));
                
                int r = Math.min(255, Math.max(0, (int)((original.getRed() - 128) * factor + 128)));
                int g = Math.min(255, Math.max(0, (int)((original.getGreen() - 128) * factor + 128)));
                int b = Math.min(255, Math.max(0, (int)((original.getBlue() - 128) * factor + 128)));
                
                enhanced.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        
        return enhanced;
    }

    private BufferedImage adjustBrightness(BufferedImage image, double factor) {
        BufferedImage adjusted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color original = new Color(image.getRGB(x, y));
                
                int r = Math.min(255, Math.max(0, (int)(original.getRed() * factor)));
                int g = Math.min(255, Math.max(0, (int)(original.getGreen() * factor)));
                int b = Math.min(255, Math.max(0, (int)(original.getBlue() * factor)));
                
                adjusted.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        
        return adjusted;
    }

    private BufferedImage binarizeImage(BufferedImage image) {
        BufferedImage binary = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = binary.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return binary;
    }

    private String performOcr(BufferedImage image, OcrConfiguration config) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(config.pageSegMode);
        tesseract.setOcrEngineMode(config.ocrEngineMode);
        
        if (config.whitelist != null && !config.whitelist.isEmpty()) {
            tesseract.setVariable("tessedit_char_whitelist", config.whitelist);
        }
        
        // Additional Tesseract configurations for higher accuracy
        tesseract.setVariable("tessedit_pageseg_mode", String.valueOf(config.pageSegMode));
        tesseract.setVariable("preserve_interword_spaces", "1");
        tesseract.setVariable("user_defined_dpi", "300");
        
        return tesseract.doOCR(image);
    }

    private ExtractedData extractDataFromText(String text, String configName) {
        ExtractedData data = new ExtractedData();
        
        // Extract Aadhaar number with multiple patterns
        String aadhaar = extractAadhaarNumber(text);
        if (aadhaar != null) {
            data.setAadhaarNumber(aadhaar);
        }
        
        // Extract name using context-aware methods
        String name = extractName(text, configName);
        if (name != null) {
            data.setName(name);
        }
        
        // Extract DOB
        String dob = extractDateOfBirth(text);
        if (dob != null) {
            data.setDateOfBirth(dob);
        }
        
        // Extract gender
        String gender = extractGender(text);
        if (gender != null) {
            data.setGender(gender);
        }
        
        // Extract address
        String address = extractAddress(text);
        if (address != null) {
            data.setAddress(address);
        }
        
        return data;
    }

    private String extractAadhaarNumber(String text) {
        // Try multiple Aadhaar patterns
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            if (entry.getKey().startsWith("aadhaar")) {
                Matcher matcher = entry.getValue().matcher(text);
                if (matcher.find()) {
                    String aadhaar = matcher.group().replaceAll("[^0-9]", "");
                    if (aadhaar.length() == 12 && isValidAadhaar(aadhaar)) {
                        return aadhaar;
                    }
                }
            }
        }
        return null;
    }

    private boolean isValidAadhaar(String aadhaar) {
        // Basic Aadhaar validation (Verhoeff algorithm would be more accurate)
        if (aadhaar.length() != 12) return false;
        if (aadhaar.matches("0+|1+|2+|3+|4+|5+|6+|7+|8+|9+")) return false; // All same digits
        return true;
    }

    private String extractName(String text, String configName) {
        // Use different strategies based on OCR configuration
        if ("names".equals(configName)) {
            return extractNameFromNamesFocusedText(text);
        }
        
        return postProcessor.extractName(text);
    }

    private String extractNameFromNamesFocusedText(String text) {
        String[] lines = text.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.length() > 2 && line.length() < 50 && 
                line.matches("^[A-Za-z\\s]+$") && 
                !line.toLowerCase().contains("name") &&
                !line.toLowerCase().contains("address") &&
                !line.toLowerCase().contains("date")) {
                return line.toUpperCase();
            }
        }
        return null;
    }

    private String extractDateOfBirth(String text) {
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            if (entry.getKey().startsWith("dob")) {
                Matcher matcher = entry.getValue().matcher(text);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        }
        return null;
    }

    private String extractGender(String text) {
        Matcher matcher = patterns.get("gender").matcher(text);
        if (matcher.find()) {
            String gender = matcher.group(1).toLowerCase();
            return gender.equals("m") || gender.equals("male") ? "Male" : "Female";
        }
        return null;
    }

    private String extractAddress(String text) {
        // Address is typically the longest coherent text block
        String[] lines = text.split("\\n");
        String longestLine = "";
        
        for (String line : lines) {
            line = line.trim();
            if (line.length() > longestLine.length() && 
                line.length() > 20 && 
                !line.matches(".*\\d{12}.*") && // Not Aadhaar line
                !line.toLowerCase().contains("name:") &&
                !line.toLowerCase().contains("dob:")) {
                longestLine = line;
            }
        }
        
        return longestLine.isEmpty() ? null : longestLine;
    }

    private void updateResultsWithConfidence(Map<String, ConfidenceScore> results, 
                                           ExtractedData data, 
                                           OcrConfiguration config,
                                           ImageQualityAnalyzer.ImageQualityMetrics metrics) {
        
        double baseConfidence = calculateBaseConfidence(config, metrics);
        
        if (data.getAadhaarNumber() != null) {
            updateFieldConfidence(results, "aadhaar", data.getAadhaarNumber(), baseConfidence + 20);
        }
        
        if (data.getName() != null) {
            updateFieldConfidence(results, "name", data.getName(), baseConfidence + 10);
        }
        
        if (data.getDateOfBirth() != null) {
            updateFieldConfidence(results, "dob", data.getDateOfBirth(), baseConfidence + 15);
        }
        
        if (data.getGender() != null) {
            updateFieldConfidence(results, "gender", data.getGender(), baseConfidence + 10);
        }
        
        if (data.getAddress() != null) {
            updateFieldConfidence(results, "address", data.getAddress(), baseConfidence + 5);
        }
    }

    private double calculateBaseConfidence(OcrConfiguration config, 
                                         ImageQualityAnalyzer.ImageQualityMetrics metrics) {
        double confidence = metrics.qualityScore;
        
        // Boost confidence for specialized configurations
        if ("numbers".equals(config.name)) {
            confidence += 10; // Better for Aadhaar numbers
        } else if ("names".equals(config.name)) {
            confidence += 5; // Better for name extraction
        }
        
        return Math.min(100, confidence);
    }

    private void updateFieldConfidence(Map<String, ConfidenceScore> results, 
                                     String field, String value, double confidence) {
        String key = field + ":" + value;
        ConfidenceScore existing = results.get(key);
        
        if (existing == null || existing.confidence < confidence) {
            results.put(key, new ConfidenceScore(field, value, confidence));
        }
    }

    private ExtractedData selectBestResults(Map<String, ConfidenceScore> results) {
        ExtractedData bestData = new ExtractedData();
        
        // Group results by field and collect all values for comprehensive correction
        Map<String, List<String>> fieldValues = new HashMap<>();
        Map<String, ConfidenceScore> bestByField = new HashMap<>();
        
        for (ConfidenceScore score : results.values()) {
            // Keep track of best confidence for each field
            ConfidenceScore existing = bestByField.get(score.field);
            if (existing == null || existing.confidence < score.confidence) {
                bestByField.put(score.field, score);
            }
            
            // Collect all values for each field for comprehensive correction
            fieldValues.computeIfAbsent(score.field, k -> new ArrayList<>()).add(score.value);
        }
        
        // Apply comprehensive text correction using multiple OCR results
        for (Map.Entry<String, List<String>> entry : fieldValues.entrySet()) {
            String field = entry.getKey();
            List<String> values = entry.getValue();
            
            switch (field) {
                case "aadhaar":
                    String correctedAadhaar = textCorrectionService.correctTextFromMultipleResults(values);
                    if (correctedAadhaar != null) {
                        correctedAadhaar = textCorrectionService.correctAadhaarNumber(correctedAadhaar);
                    }
                    bestData.setAadhaarNumber(correctedAadhaar);
                    break;
                case "name":
                    String correctedName = textCorrectionService.correctTextFromMultipleResults(values);
                    if (correctedName != null) {
                        correctedName = textCorrectionService.correctName(correctedName);
                    }
                    bestData.setName(correctedName);
                    break;
                case "dob":
                    String correctedDob = textCorrectionService.correctTextFromMultipleResults(values);
                    if (correctedDob != null) {
                        correctedDob = textCorrectionService.correctDateOfBirth(correctedDob);
                    }
                    bestData.setDateOfBirth(correctedDob);
                    break;
                case "gender":
                    String correctedGender = textCorrectionService.correctTextFromMultipleResults(values);
                    if (correctedGender != null) {
                        correctedGender = textCorrectionService.correctGender(correctedGender);
                    }
                    bestData.setGender(correctedGender);
                    break;
                case "address":
                    String correctedAddress = textCorrectionService.correctTextFromMultipleResults(values);
                    if (correctedAddress != null) {
                        correctedAddress = textCorrectionService.correctAddress(correctedAddress);
                    }
                    bestData.setAddress(correctedAddress);
                    break;
            }
        }
        
        // Final validation and confidence scoring
        Map<String, Double> validationScores = textCorrectionService.validateExtractedData(
                bestData.getName(), bestData.getAadhaarNumber(), bestData.getDateOfBirth(),
                bestData.getGender(), bestData.getAddress());
        
        // Calculate overall confidence
        double overallConfidence = validationScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        
        bestData.setConfidenceScore(overallConfidence);
        
        System.out.println("Final extraction results with confidence: " + overallConfidence);
        System.out.println("Validation scores: " + validationScores);
        
        return bestData;
    }

    /**
     * Collect multiple OCR results from different configurations and preprocessing variants
     */
    private Map<String, List<String>> collectMultipleOcrResults(List<BufferedImage> variants) {
        Map<String, List<String>> allResults = new HashMap<>();
        
        for (BufferedImage variant : variants) {
            for (OcrConfiguration config : ocrConfigurations) {
                try {
                    String text = performOcr(variant, config);
                    if (text != null && !text.trim().isEmpty()) {
                        // Extract field-specific text from the OCR result
                        Map<String, String> extractedFields = extractFieldsFromText(text);
                        
                        for (Map.Entry<String, String> field : extractedFields.entrySet()) {
                            allResults.computeIfAbsent(field.getKey(), k -> new ArrayList<>())
                                    .add(field.getValue());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("OCR failed with config " + config.name + ": " + e.getMessage());
                }
            }
        }
        
        return allResults;
    }
    
    /**
     * Extract specific fields from raw OCR text using advanced layout recognition
     */
    private Map<String, String> extractFieldsFromText(String text) {
        Map<String, String> fields = new HashMap<>();
        
        // Extract Aadhaar number using multiple patterns
        String aadhaarNumber = extractAadhaarFromText(text);
        if (aadhaarNumber != null) {
            fields.put("aadhaar", aadhaarNumber);
        }
        
        // Extract name using advanced Aadhaar layout recognition
        String name = textCorrectionService.extractNameFromAadhaarLayout(text);
        if (name != null) {
            fields.put("name", name);
        }
        
        // Extract DOB using multiple patterns
        String dob = extractDobFromText(text);
        if (dob != null) {
            fields.put("dob", dob);
        }
        
        // Extract gender
        String gender = extractGenderFromText(text);
        if (gender != null) {
            fields.put("gender", gender);
        }
        
        // Extract address using advanced layout recognition
        String address = textCorrectionService.extractAddressFromAadhaarLayout(text);
        if (address != null) {
            fields.put("address", address);
        } else {
            // Fallback: try to extract address manually
            String fallbackAddress = extractAddressFallback(text);
            if (fallbackAddress != null) {
                fields.put("address", fallbackAddress);
            }
        }
        
        return fields;
    }
    
    /**
     * Extract Aadhaar number using improved patterns and validation
     */
    private String extractAadhaarFromText(String text) {
        // Use the enhanced extraction from TextCorrectionService
        String aadhaarFromService = textCorrectionService.extractAadhaarFromText(text);
        if (aadhaarFromService != null) {
            return aadhaarFromService;
        }
        
        // Fallback to basic patterns
        Pattern[] aadhaarPatterns = {
            Pattern.compile("\\b(\\d{4})\\s+(\\d{4})\\s+(\\d{4})\\b"),
            Pattern.compile("\\b(\\d{4})[-](\\d{4})[-](\\d{4})\\b"),
            Pattern.compile("\\b(\\d{12})\\b")
        };
        
        for (Pattern pattern : aadhaarPatterns) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String number;
                if (matcher.groupCount() == 3) {
                    number = matcher.group(1) + matcher.group(2) + matcher.group(3);
                } else if (matcher.groupCount() == 1) {
                    number = matcher.group(1);
                } else {
                    number = matcher.group().replaceAll("[\\s-]", "");
                }
                
                // Validate the number
                if (number.length() == 12 && !number.startsWith("0") && !number.startsWith("1")) {
                    return number.substring(0, 4) + " " + number.substring(4, 8) + " " + number.substring(8, 12);
                }
            }
        }
        return null;
    }
    
    /**
     * Extract date of birth using multiple patterns
     */
    private String extractDobFromText(String text) {
        Pattern[] dobPatterns = {
            Pattern.compile("\\b\\d{2}[/-]\\d{2}[/-]\\d{4}\\b"),
            Pattern.compile("\\b\\d{1,2}\\s+(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d{4}\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?i)(?:dob|date of birth|birth date)\\s*:?\\s*(\\d{2}[/-]\\d{2}[/-]\\d{4})")
        };
        
        for (Pattern pattern : dobPatterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
            }
        }
        return null;
    }
    
    /**
     * Extract gender using multiple patterns
     */
    private String extractGenderFromText(String text) {
        Pattern genderPattern = Pattern.compile("(?i)(?:gender|sex|लिंग)\\s*:?\\s*(male|female|m|f|पुरुष|महिला)|\\b(male|female)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = genderPattern.matcher(text);
        if (matcher.find()) {
            String gender = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            return gender.toLowerCase().startsWith("m") || gender.equals("पुरुष") ? "MALE" : "FEMALE";
        }
        return null;
    }
    
    /**
     * Enhanced extraction with comprehensive text correction
     */
    public ExtractedData extractWithComprehensiveCorrection(BufferedImage image) {
        System.out.println("Starting fast OCR extraction...");
        
        // Step 1: Quick preprocessing (skip complex analysis for speed)
        BufferedImage processed;
        try {
            processed = imageProcessor.preprocessImage(image);
        } catch (Exception e) {
            System.err.println("Preprocessing failed, using original image: " + e.getMessage());
            processed = image;
        }
        
        // Step 2: Single OCR pass with best configuration
        ExtractedData bestData = new ExtractedData();
        
        try {
            String ocrText = performOcr(processed, ocrConfigurations.get(0));
            
            if (ocrText != null && !ocrText.trim().isEmpty()) {
                // Extract fields directly
                Map<String, String> fields = extractFieldsFromText(ocrText);
                
                // Apply basic corrections
                if (fields.containsKey("aadhaar")) {
                    String aadhaar = textCorrectionService.correctAadhaarNumber(fields.get("aadhaar"));
                    bestData.setAadhaarNumber(aadhaar);
                }
                
                if (fields.containsKey("name")) {
                    String name = textCorrectionService.correctName(fields.get("name"));
                    bestData.setName(name);
                }
                
                if (fields.containsKey("dob")) {
                    String dob = textCorrectionService.correctDateOfBirth(fields.get("dob"));
                    bestData.setDateOfBirth(dob);
                }
                
                if (fields.containsKey("gender")) {
                    String gender = textCorrectionService.correctGender(fields.get("gender"));
                    bestData.setGender(gender);
                }
                
                if (fields.containsKey("address")) {
                    String address = textCorrectionService.correctAddress(fields.get("address"));
                    bestData.setAddress(address);
                }
            }
        } catch (Exception e) {
            System.err.println("OCR extraction error: " + e.getMessage());
        }
        
        // Quick confidence score
        double confidence = calculateQuickConfidence(bestData);
        bestData.setConfidenceScore(confidence);
        
        System.out.println("Fast extraction completed with confidence: " + confidence);
        
        return bestData;
    }
    
    /**
     * Quick confidence calculation
     */
    private double calculateQuickConfidence(ExtractedData data) {
        double confidence = 0.0;
        
        if (data.getName() != null && data.getName().length() > 2) {
            confidence += 20.0;
        }
        
        if (data.getAadhaarNumber() != null && data.getAadhaarNumber().matches("\\d{4} \\d{4} \\d{4}")) {
            confidence += 30.0;
        }
        
        if (data.getDateOfBirth() != null && data.getDateOfBirth().length() > 5) {
            confidence += 20.0;
        }
        
        if (data.getGender() != null) {
            confidence += 15.0;
        }
        
        if (data.getAddress() != null && data.getAddress().length() > 5) {
            confidence += 15.0;
        }
        
        return confidence;
    }

    /**
     * Create specialized preprocessing variants for Aadhaar cards
     */
    private List<BufferedImage> createAadhaarSpecificVariants(BufferedImage original) {
        List<BufferedImage> variants = new ArrayList<>();
        
        try {
            // Original with basic preprocessing
            BufferedImage basic = imageProcessor.preprocessImage(original);
            variants.add(basic);
            
            // High contrast for text clarity
            BufferedImage highContrast = enhanceContrastSpecialized(original, 2.0);
            variants.add(highContrast);
            
            // Gamma correction for better visibility
            BufferedImage gammaCorrected = applyGammaCorrection(original, 1.2);
            variants.add(gammaCorrected);
            
            // Noise reduction with edge preservation
            BufferedImage denoised = applyNoiseReduction(original);
            variants.add(denoised);
            
            // Text region enhancement
            BufferedImage textEnhanced = enhanceTextRegions(original);
            variants.add(textEnhanced);
            
            // Binarization with adaptive threshold
            BufferedImage adaptiveBinary = applyAdaptiveBinarization(original);
            variants.add(adaptiveBinary);
            
        } catch (Exception e) {
            System.err.println("Error creating Aadhaar-specific variants: " + e.getMessage());
            variants.add(original); // Fallback
        }
        
        return variants;
    }
    
    /**
     * Enhanced contrast specifically for Aadhaar cards
     */
    private BufferedImage enhanceContrastSpecialized(BufferedImage image, double factor) {
        BufferedImage enhanced = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color original = new Color(image.getRGB(x, y));
                
                // Convert to HSV for better control
                float[] hsv = Color.RGBtoHSB(original.getRed(), original.getGreen(), original.getBlue(), null);
                
                // Enhance only the brightness/value component
                float newValue = Math.min(1.0f, Math.max(0.0f, (hsv[2] - 0.5f) * (float)factor + 0.5f));
                
                int rgb = Color.HSBtoRGB(hsv[0], hsv[1], newValue);
                enhanced.setRGB(x, y, rgb);
            }
        }
        
        return enhanced;
    }
    
    /**
     * Apply gamma correction for better text visibility
     */
    private BufferedImage applyGammaCorrection(BufferedImage image, double gamma) {
        BufferedImage corrected = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        // Create gamma lookup table
        int[] gammaLUT = new int[256];
        for (int i = 0; i < 256; i++) {
            gammaLUT[i] = (int) (Math.pow(i / 255.0, 1.0 / gamma) * 255);
        }
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color original = new Color(image.getRGB(x, y));
                
                int r = gammaLUT[original.getRed()];
                int g = gammaLUT[original.getGreen()];
                int b = gammaLUT[original.getBlue()];
                
                corrected.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        
        return corrected;
    }
    
    /**
     * Apply noise reduction while preserving edges
     */
    private BufferedImage applyNoiseReduction(BufferedImage image) {
        // Simple median filter for noise reduction
        BufferedImage denoised = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 1; y < image.getHeight() - 1; y++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                // Get 3x3 neighborhood
                int[] reds = new int[9];
                int[] greens = new int[9];
                int[] blues = new int[9];
                
                int idx = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        Color c = new Color(image.getRGB(x + dx, y + dy));
                        reds[idx] = c.getRed();
                        greens[idx] = c.getGreen();
                        blues[idx] = c.getBlue();
                        idx++;
                    }
                }
                
                // Sort and take median
                java.util.Arrays.sort(reds);
                java.util.Arrays.sort(greens);
                java.util.Arrays.sort(blues);
                
                Color median = new Color(reds[4], greens[4], blues[4]);
                denoised.setRGB(x, y, median.getRGB());
            }
        }
        
        return denoised;
    }
    
    /**
     * Enhance text regions specifically
     */
    private BufferedImage enhanceTextRegions(BufferedImage image) {
        BufferedImage enhanced = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color original = new Color(image.getRGB(x, y));
                
                // Convert to grayscale
                int gray = (int) (0.299 * original.getRed() + 0.587 * original.getGreen() + 0.114 * original.getBlue());
                
                // Enhance text-like regions (dark text on light background)
                if (gray < 128) {
                    // Make text darker
                    gray = Math.max(0, gray - 30);
                } else {
                    // Make background lighter
                    gray = Math.min(255, gray + 20);
                }
                
                enhanced.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        
        return enhanced;
    }
    
    /**
     * Apply adaptive binarization for better text extraction
     */
    private BufferedImage applyAdaptiveBinarization(BufferedImage image) {
        BufferedImage binary = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        
        // Convert to grayscale first
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = gray.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        // Apply adaptive threshold
        int windowSize = 15;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Calculate local mean
                int sum = 0;
                int count = 0;
                
                for (int dy = -windowSize/2; dy <= windowSize/2; dy++) {
                    for (int dx = -windowSize/2; dx <= windowSize/2; dx++) {
                        int nx = Math.max(0, Math.min(image.getWidth() - 1, x + dx));
                        int ny = Math.max(0, Math.min(image.getHeight() - 1, y + dy));
                        
                        Color c = new Color(gray.getRGB(nx, ny));
                        sum += c.getRed(); // Since it's grayscale, R=G=B
                        count++;
                    }
                }
                
                int localMean = sum / count;
                Color pixel = new Color(gray.getRGB(x, y));
                
                // Threshold with some bias towards white
                if (pixel.getRed() > localMean - 10) {
                    binary.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    binary.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        
        return binary;
    }

    /**
     * Specialized preprocessing for Aadhaar cards to improve text extraction
     */
    private BufferedImage preprocessForAadhaarCard(BufferedImage image) {
        try {
            // Create multiple processed versions and select the best one
            List<BufferedImage> variants = new ArrayList<>();
            
            // Version 1: High contrast for better text separation
            BufferedImage highContrast = enhanceTextContrast(image);
            variants.add(highContrast);
            
            // Version 2: Noise reduction while preserving text
            BufferedImage denoised = reduceNoisePreserveText(image);
            variants.add(denoised);
            
            // Version 3: Optimized for small text (ID numbers)
            BufferedImage smallTextOptimized = optimizeForSmallText(image);
            variants.add(smallTextOptimized);
            
            // Select the best variant based on text clarity score
            return selectBestVariant(variants);
            
        } catch (Exception e) {
            System.err.println("Aadhaar preprocessing failed: " + e.getMessage());
            return image;
        }
    }
    
    /**
     * Enhance text contrast specifically for Aadhaar cards
     */
    private BufferedImage enhanceTextContrast(BufferedImage image) {
        BufferedImage enhanced = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color original = new Color(image.getRGB(x, y));
                int gray = (int)(0.299 * original.getRed() + 0.587 * original.getGreen() + 0.114 * original.getBlue());
                
                // Apply strong contrast enhancement
                int newGray = gray < 128 ? Math.max(0, gray - 30) : Math.min(255, gray + 30);
                Color newColor = new Color(newGray, newGray, newGray);
                enhanced.setRGB(x, y, newColor.getRGB());
            }
        }
        
        return enhanced;
    }
    
    /**
     * Reduce noise while preserving text edges
     */
    private BufferedImage reduceNoisePreserveText(BufferedImage image) {
        // Convert to grayscale first
        BufferedImage grayscale = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = grayscale.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        // Apply median filter to reduce noise
        int[][] pixels = new int[image.getHeight()][image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                pixels[y][x] = new Color(grayscale.getRGB(x, y)).getRed();
            }
        }
        
        // Apply median filter
        BufferedImage filtered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 1; y < image.getHeight() - 1; y++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                int[] neighbors = {
                    pixels[y-1][x-1], pixels[y-1][x], pixels[y-1][x+1],
                    pixels[y][x-1], pixels[y][x], pixels[y][x+1],
                    pixels[y+1][x-1], pixels[y+1][x], pixels[y+1][x+1]
                };
                Arrays.sort(neighbors);
                int median = neighbors[4]; // Middle value
                Color color = new Color(median, median, median);
                filtered.setRGB(x, y, color.getRGB());
            }
        }
        
        return filtered;
    }
    
    /**
     * Optimize image processing for small text like ID numbers
     */
    private BufferedImage optimizeForSmallText(BufferedImage image) {
        // Scale up the image for better small text recognition
        int newWidth = (int)(image.getWidth() * 1.5);
        int newHeight = (int)(image.getHeight() * 1.5);
        
        BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        // Apply sharpening filter
        float[] sharpenKernel = {
            0, -1, 0,
            -1, 5, -1,
            0, -1, 0
        };
        Kernel kernel = new Kernel(3, 3, sharpenKernel);
        ConvolveOp sharpen = new ConvolveOp(kernel);
        return sharpen.filter(scaled, null);
    }
    
    /**
     * Select the best image variant based on text clarity
     */
    private BufferedImage selectBestVariant(List<BufferedImage> variants) {
        // Simple heuristic: choose the variant with the best contrast ratio
        BufferedImage best = variants.get(0);
        double bestScore = calculateTextClarityScore(best);
        
        for (BufferedImage variant : variants) {
            double score = calculateTextClarityScore(variant);
            if (score > bestScore) {
                bestScore = score;
                best = variant;
            }
        }
        
        return best;
    }
    
    /**
     * Calculate a text clarity score for image quality assessment
     */
    private double calculateTextClarityScore(BufferedImage image) {
        int totalPixels = 0;
        int edgePixels = 0;
        
        // Count edge pixels (indicates text boundaries)
        for (int y = 1; y < image.getHeight() - 1; y++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                Color center = new Color(image.getRGB(x, y));
                Color right = new Color(image.getRGB(x + 1, y));
                Color bottom = new Color(image.getRGB(x, y + 1));
                
                int centerGray = (center.getRed() + center.getGreen() + center.getBlue()) / 3;
                int rightGray = (right.getRed() + right.getGreen() + right.getBlue()) / 3;
                int bottomGray = (bottom.getRed() + bottom.getGreen() + bottom.getBlue()) / 3;
                
                if (Math.abs(centerGray - rightGray) > 30 || Math.abs(centerGray - bottomGray) > 30) {
                    edgePixels++;
                }
                totalPixels++;
            }
        }
        
        return totalPixels > 0 ? (double) edgePixels / totalPixels : 0.0;
    }

    private static class OcrConfiguration {
        String name;
        int pageSegMode;
        int ocrEngineMode;
        String whitelist;
        
        OcrConfiguration(String name, int pageSegMode, int ocrEngineMode, String whitelist) {
            this.name = name;
            this.pageSegMode = pageSegMode;
            this.ocrEngineMode = ocrEngineMode;
            this.whitelist = whitelist;
        }
    }

    private static class ConfidenceScore {
        String field;
        String value;
        double confidence;
        
        ConfidenceScore(String field, String value, double confidence) {
            this.field = field;
            this.value = value;
            this.confidence = confidence;
        }
    }
    
    /**
     * Fallback address extraction method
     */
    private String extractAddressFallback(String text) {
        String[] lines = text.split("\\n");
        StringBuilder addressBuilder = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.length() < 3) continue;
            
            // Skip obvious non-address lines
            if (line.toLowerCase().contains("government") || 
                line.toLowerCase().contains("aadhaar") ||
                line.toLowerCase().contains("authority") ||
                line.matches(".*\\d{4}\\s*\\d{4}\\s*\\d{4}.*") || // Aadhaar number
                line.matches(".*\\d{2}[/-]\\d{2}[/-]\\d{4}.*") || // Date
                line.toLowerCase().matches(".*(male|female).*")) { // Gender
                continue;
            }
            
            // Look for address indicators or numeric patterns
            String lowerLine = line.toLowerCase();
            if (lowerLine.contains("nagar") || lowerLine.contains("colony") || 
                lowerLine.contains("delhi") || lowerLine.contains("raj") ||
                line.matches(".*\\d+.*")) { // Contains numbers
                
                if (addressBuilder.length() > 0) {
                    addressBuilder.append(", ");
                }
                addressBuilder.append(line);
            }
        }
        
        return addressBuilder.length() > 0 ? addressBuilder.toString() : null;
    }
}
