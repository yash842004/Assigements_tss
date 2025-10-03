package com.tss.LoanScreeing.service;

import com.tss.LoanScreeing.model.ExtractedData;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FastAadhaarOcrService {

    @Autowired
    private TextCorrectionService textCorrectionService;

    // Pre-compiled patterns for faster matching
    private static final Pattern AADHAAR_PATTERN = Pattern.compile("\\b(\\d{4})\\s+(\\d{4})\\s+(\\d{4})\\b");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\b(\\d{2}[/-]\\d{2}[/-]\\d{4})\\b");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z][A-Za-z\\s]{2,40}$");

    /**
     * Fast extraction focusing only on text regions
     */
    public ExtractedData extractFromAadhaarCard(BufferedImage image) {
        System.out.println("Starting fast Aadhaar OCR extraction...");
        
        ExtractedData result = new ExtractedData();
        
        try {
            // Step 1: Crop image to text regions only (ignore photo and QR code)
            BufferedImage textRegion = cropTextRegionsOnly(image);
            
            // Step 2: Simple preprocessing for text clarity
            BufferedImage processed = simplePreprocessing(textRegion);
            
            // Step 3: Single OCR pass with optimized settings
            String rawText = performFastOcr(processed);
            
            if (rawText != null && !rawText.trim().isEmpty()) {
                System.out.println("Raw OCR Text: " + rawText);
                
                // Step 4: Extract specific fields quickly
                extractFieldsFromText(rawText, result);
                
                // Step 5: Apply basic corrections
                applyBasicCorrections(result);
                
                // Step 6: Set confidence based on extracted data
                result.setConfidenceScore(calculateSimpleConfidence(result));
            }
            
        } catch (Exception e) {
            System.err.println("Fast OCR error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Fast extraction completed");
        return result;
    }
    
    /**
     * Crop image to focus only on text regions, excluding photo and QR code
     */
    private BufferedImage cropTextRegionsOnly(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Enhanced cropping strategy for Aadhaar cards
        // Try multiple text regions to maximize extraction success
        int startX, startY, cropWidth, cropHeight;
        
        if (width > height * 1.5) {
            // Landscape orientation - typical Aadhaar card
            // Text is in the right portion, avoiding left photo area
            startX = (int)(width * 0.35);  // Start from 35% to avoid photo
            startY = (int)(height * 0.05); // Start from 5% to include name
            cropWidth = (int)(width * 0.60); // Take 60% width for text area
            cropHeight = (int)(height * 0.80); // Take 80% height (exclude QR area)
        } else {
            // Portrait or square orientation
            startX = (int)(width * 0.05);  // Start from left edge
            startY = (int)(height * 0.25); // Skip photo area at top
            cropWidth = (int)(width * 0.90); // Use most of width
            cropHeight = (int)(height * 0.60); // Take middle portion
        }
        
        // Ensure bounds are valid
        startX = Math.max(0, Math.min(startX, width - 1));
        startY = Math.max(0, Math.min(startY, height - 1));
        cropWidth = Math.min(cropWidth, width - startX);
        cropHeight = Math.min(cropHeight, height - startY);
        
        System.out.println(String.format("Cropping %dx%d image: x=%d,y=%d,w=%d,h=%d (%.1f%% of original)", 
            width, height, startX, startY, cropWidth, cropHeight, 
            100.0 * cropWidth * cropHeight / (width * height)));
        
        return image.getSubimage(startX, startY, cropWidth, cropHeight);
    }
    
    /**
     * Simple and fast preprocessing
     */
    private BufferedImage simplePreprocessing(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage processed = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color original = new Color(image.getRGB(x, y));
                
                // Convert to grayscale
                int gray = (int)(0.299 * original.getRed() + 0.587 * original.getGreen() + 0.114 * original.getBlue());
                
                // Simple contrast enhancement
                if (gray < 128) {
                    gray = Math.max(0, gray - 20); // Make dark text darker
                } else {
                    gray = Math.min(255, gray + 20); // Make light background lighter
                }
                
                Color newColor = new Color(gray, gray, gray);
                processed.setRGB(x, y, newColor.getRGB());
            }
        }
        
        return processed;
    }
    
    /**
     * Fast OCR with optimized configuration for Aadhaar cards
     */
    private String performFastOcr(BufferedImage image) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(6); // Single uniform block of text
        tesseract.setOcrEngineMode(1); // Neural network LSTM engine only
        
        // Optimized character whitelist for Aadhaar cards
        tesseract.setVariable("tessedit_char_whitelist", 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,-/():&");
        
        // Additional OCR optimizations for speed and accuracy
        tesseract.setVariable("tessedit_pageseg_mode", "6");
        tesseract.setVariable("preserve_interword_spaces", "1");
        tesseract.setVariable("load_system_dawg", "0");
        tesseract.setVariable("load_freq_dawg", "0");
        
        String result = tesseract.doOCR(image);
        System.out.println("Fast OCR completed, text length: " + (result != null ? result.length() : 0));
        
        return result;
    }
    
    /**
     * Extract fields from OCR text quickly
     */
    private void extractFieldsFromText(String text, ExtractedData result) {
        String[] lines = text.split("\\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.length() < 2) continue;
            
            // Extract Aadhaar number
            if (result.getAadhaarNumber() == null) {
                Matcher aadhaarMatcher = AADHAAR_PATTERN.matcher(line);
                if (aadhaarMatcher.find()) {
                    String aadhaar = aadhaarMatcher.group(1) + " " + aadhaarMatcher.group(2) + " " + aadhaarMatcher.group(3);
                    result.setAadhaarNumber(aadhaar);
                    continue;
                }
            }
            
            // Extract date of birth
            if (result.getDateOfBirth() == null) {
                Matcher dateMatcher = DATE_PATTERN.matcher(line);
                if (dateMatcher.find()) {
                    result.setDateOfBirth(dateMatcher.group(1));
                    continue;
                }
            }
            
            // Extract gender
            if (result.getGender() == null) {
                String lowerLine = line.toLowerCase();
                if (lowerLine.contains("male") && !lowerLine.contains("female")) {
                    result.setGender("MALE");
                    continue;
                } else if (lowerLine.contains("female")) {
                    result.setGender("FEMALE");
                    continue;
                }
            }
            
            // Extract name (look for lines that look like names)
            if (result.getName() == null && isLikelyName(line)) {
                result.setName(line.toUpperCase());
                continue;
            }
            
            // Extract address (lines with address indicators or mixed alphanumeric)
            if (result.getAddress() == null && isLikelyAddress(line)) {
                result.setAddress(line);
            }
        }
    }
    
    /**
     * Check if a line looks like a name
     */
    private boolean isLikelyName(String line) {
        if (line == null || line.length() < 3 || line.length() > 40) return false;
        
        // Should be mostly alphabetic
        long letters = line.chars().filter(Character::isLetter).count();
        if (letters < line.length() * 0.8) return false;
        
        // Should not contain numbers (except maybe in compound names)
        if (line.matches(".*\\d.*")) return false;
        
        // Should not be common non-name words
        String lower = line.toLowerCase();
        if (lower.contains("government") || lower.contains("india") || 
            lower.contains("aadhaar") || lower.contains("unique")) return false;
            
        return true;
    }
    
    /**
     * Check if a line looks like an address
     */
    private boolean isLikelyAddress(String line) {
        if (line == null || line.length() < 5) return false;
        
        String lower = line.toLowerCase();
        
        // Look for address indicators
        String[] addressWords = {"colony", "nagar", "road", "street", "delhi", "mumbai", 
                                "bangalore", "chennai", "hyderabad", "pune", "raj", "new"};
        
        for (String word : addressWords) {
            if (lower.contains(word)) return true;
        }
        
        // Look for house numbers (numbers + letters)
        if (line.matches(".*\\d+.*[a-zA-Z].*") || line.matches(".*[a-zA-Z].*\\d+.*")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Apply basic corrections to extracted data
     */
    private void applyBasicCorrections(ExtractedData result) {
        // Correct Aadhaar number
        if (result.getAadhaarNumber() != null) {
            String corrected = textCorrectionService.correctAadhaarNumber(result.getAadhaarNumber());
            result.setAadhaarNumber(corrected);
        }
        
        // Correct name
        if (result.getName() != null) {
            String corrected = textCorrectionService.correctName(result.getName());
            result.setName(corrected);
        }
        
        // Correct date
        if (result.getDateOfBirth() != null) {
            String corrected = textCorrectionService.correctDateOfBirth(result.getDateOfBirth());
            result.setDateOfBirth(corrected);
        }
        
        // Correct address
        if (result.getAddress() != null) {
            String corrected = textCorrectionService.correctAddress(result.getAddress());
            result.setAddress(corrected);
        }
    }
    
    /**
     * Simple confidence calculation
     */
    private double calculateSimpleConfidence(ExtractedData data) {
        double confidence = 0.0;
        
        if (data.getAadhaarNumber() != null && data.getAadhaarNumber().matches("\\d{4} \\d{4} \\d{4}")) {
            confidence += 30.0;
        }
        
        if (data.getName() != null && data.getName().length() > 2) {
            confidence += 25.0;
        }
        
        if (data.getDateOfBirth() != null && data.getDateOfBirth().matches("\\d{2}[/-]\\d{2}[/-]\\d{4}")) {
            confidence += 20.0;
        }
        
        if (data.getGender() != null) {
            confidence += 15.0;
        }
        
        if (data.getAddress() != null && data.getAddress().length() > 5) {
            confidence += 10.0;
        }
        
        return confidence;
    }
}
