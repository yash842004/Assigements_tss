package com.tss.LoanScreeing.service;

import com.tss.LoanScreeing.model.ExtractedData;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@Service
public class AdvancedOcrService {

    @Autowired
    private ImageProcessor imageProcessor;

    @Autowired
    private PostProcessor postProcessor;
    
    @Autowired
    private UltraHighAccuracyOcrService ultraHighAccuracyOcrService;

    private final List<Tesseract> ocrEngines;

    public AdvancedOcrService() {
        ocrEngines = new ArrayList<>();
        initializeOcrEngines();
    }

    private void initializeOcrEngines() {
        try {
            // Engine 1: Standard configuration
            Tesseract engine1 = new Tesseract();
            engine1.setDatapath("tessdata");
            engine1.setLanguage("eng");
            engine1.setPageSegMode(6); // Uniform block of text
            engine1.setOcrEngineMode(1); // LSTM OCR engine
            ocrEngines.add(engine1);

            // Engine 2: Character-focused configuration
            Tesseract engine2 = new Tesseract();
            engine2.setDatapath("tessdata");
            engine2.setLanguage("eng");
            engine2.setPageSegMode(8); // Single word
            engine2.setOcrEngineMode(1);
            engine2.setVariable("tessedit_char_whitelist", 
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .-/");
            ocrEngines.add(engine2);

            // Engine 3: Line-focused configuration
            Tesseract engine3 = new Tesseract();
            engine3.setDatapath("tessdata");
            engine3.setLanguage("eng");
            engine3.setPageSegMode(7); // Single text line
            engine3.setOcrEngineMode(1);
            engine3.setVariable("preserve_interword_spaces", "1");
            ocrEngines.add(engine3);

            // Engine 4: Document-focused configuration
            Tesseract engine4 = new Tesseract();
            engine4.setDatapath("tessdata");
            engine4.setLanguage("eng");
            engine4.setPageSegMode(1); // Auto with OSD
            engine4.setOcrEngineMode(1);
            engine4.setVariable("tessedit_do_invert", "0");
            ocrEngines.add(engine4);

        } catch (Exception e) {
            System.err.println("Warning: Could not initialize all OCR engines: " + e.getMessage());
        }
    }

    public ExtractedData extractDataFromImage(MultipartFile file) throws TesseractException, IOException {
        BufferedImage originalImage;
        try (InputStream inputStream = file.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        }

        if (originalImage == null) {
            throw new IOException("Could not read image file");
        }

        // First try ultra high accuracy approach
        try {
            ExtractedData ultraResult = ultraHighAccuracyOcrService.extractWithUltraHighAccuracy(originalImage);
            if (ultraResult != null && 
                (ultraResult.getName() != null || ultraResult.getAadhaarNumber() != null)) {
                System.out.println("Ultra high accuracy extraction successful");
                return ultraResult;
            }
        } catch (Exception e) {
            System.err.println("Ultra high accuracy extraction failed, falling back to standard approach: " + e.getMessage());
        }

        // Fallback to standard multi-engine approach
        List<BufferedImage> processedImages = createMultipleProcessedVersions(originalImage);
        
        // Extract text using multiple engines and images
        Map<String, Integer> textCandidates = new HashMap<>();
        String bestRawText = "";
        double bestConfidence = 0.0;

        for (BufferedImage processedImage : processedImages) {
            for (Tesseract engine : ocrEngines) {
                try {
                    String rawText = engine.doOCR(processedImage);
                    if (rawText != null && !rawText.trim().isEmpty()) {
                        textCandidates.put(rawText, textCandidates.getOrDefault(rawText, 0) + 1);
                        
                        // Simple confidence based on text length and character variety
                        double confidence = calculateTextConfidence(rawText);
                        if (confidence > bestConfidence) {
                            bestConfidence = confidence;
                            bestRawText = rawText;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("OCR engine failed: " + e.getMessage());
                }
            }
        }

        // Use the most frequent text result or the highest confidence one
        String finalRawText = getMostReliableText(textCandidates, bestRawText);

        // Extract structured data
        ExtractedData extractedData = new ExtractedData();
        extractedData.setRawText(finalRawText);

        // Detect document type
        String documentType = postProcessor.detectDocumentType(finalRawText);
        extractedData.setDocumentType(documentType);

        // Enhanced name extraction with multiple approaches
        String extractedName = extractNameWithMultipleApproaches(finalRawText, processedImages);
        extractedData.setName(extractedName);

        // Extract other data
        String extractedId = extractIdFromText(finalRawText, documentType);
        extractedData.setIdNumber(extractedId);

        String dateOfBirth = postProcessor.extractDateOfBirth(finalRawText);
        extractedData.setDateOfBirth(dateOfBirth);

        String address = postProcessor.extractAddress(finalRawText);
        extractedData.setAddress(address);

        // Calculate final confidence score
        double confidenceScore = calculateOverallConfidence(
            finalRawText, extractedData.getName(), extractedData.getIdNumber(), documentType);
        extractedData.setConfidenceScore(confidenceScore);

        return extractedData;
    }

    private List<BufferedImage> createMultipleProcessedVersions(BufferedImage originalImage) throws IOException {
        List<BufferedImage> versions = new ArrayList<>();
        
        // Version 1: Advanced preprocessing
        versions.add(imageProcessor.preprocessImage(originalImage));
        
        // Version 2: High contrast version
        versions.add(createHighContrastVersion(originalImage));
        
        // Version 3: Enlarged version
        versions.add(createEnlargedVersion(originalImage));
        
        // Version 4: Sharpened version
        versions.add(createSharpenedVersion(originalImage));
        
        return versions;
    }

    private BufferedImage createHighContrastVersion(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                
                // Extreme contrast
                gray = gray > 127 ? 255 : 0;
                
                int newRgb = (gray << 16) | (gray << 8) | gray;
                result.setRGB(x, y, newRgb);
            }
        }
        
        return result;
    }

    private BufferedImage createEnlargedVersion(BufferedImage image) {
        int newWidth = image.getWidth() * 3;
        int newHeight = image.getHeight() * 3;
        
        BufferedImage enlarged = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = enlarged.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        return enlarged;
    }

    private BufferedImage createSharpenedVersion(BufferedImage image) {
        float[] sharpenKernel = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
        };
        
        java.awt.image.Kernel kernel = new java.awt.image.Kernel(3, 3, sharpenKernel);
        java.awt.image.ConvolveOp op = new java.awt.image.ConvolveOp(kernel);
        return op.filter(image, null);
    }

    private String extractNameWithMultipleApproaches(String rawText, List<BufferedImage> processedImages) {
        // Approach 1: Standard text analysis
        String nameFromText = postProcessor.extractNameFromRawText(rawText);
        
        // Approach 2: Zonal OCR on name regions
        String nameFromZones = extractNameFromSpecificZones(processedImages);
        
        // Approach 3: Pattern matching
        String nameFromPatterns = extractNameUsingPatterns(rawText);
        
        // Choose the best result
        Map<String, Integer> nameCandidates = new HashMap<>();
        if (!"Not Found".equals(nameFromText)) {
            nameCandidates.put(nameFromText, nameCandidates.getOrDefault(nameFromText, 0) + 3);
        }
        if (!"Not Found".equals(nameFromZones)) {
            nameCandidates.put(nameFromZones, nameCandidates.getOrDefault(nameFromZones, 0) + 2);
        }
        if (!"Not Found".equals(nameFromPatterns)) {
            nameCandidates.put(nameFromPatterns, nameCandidates.getOrDefault(nameFromPatterns, 0) + 1);
        }
        
        return nameCandidates.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Not Found");
    }

    private String extractNameFromSpecificZones(List<BufferedImage> images) {
        for (BufferedImage image : images) {
            try {
                // Define name zone for Aadhaar (approximate coordinates)
                int width = image.getWidth();
                int height = image.getHeight();
                
                Rectangle nameZone = new Rectangle(
                    width / 8, 
                    height * 2 / 5, 
                    width * 3 / 4, 
                    height / 8
                );
                
                if (nameZone.x + nameZone.width <= width && nameZone.y + nameZone.height <= height) {
                    BufferedImage nameRegion = image.getSubimage(
                        nameZone.x, nameZone.y, nameZone.width, nameZone.height);
                    
                    String nameText = ocrEngines.get(0).doOCR(nameRegion);
                    String cleanedName = postProcessor.correctName(nameText);
                    
                    if (isValidName(cleanedName)) {
                        return cleanedName;
                    }
                }
            } catch (Exception e) {
                // Continue with next image
            }
        }
        
        return "Not Found";
    }

    private String extractNameUsingPatterns(String rawText) {
        String[] lines = rawText.split("\\n");
        
        for (String line : lines) {
            line = line.trim();
            
            // Look for lines that match name patterns
            if (line.matches("^[A-Z][a-z]+ [A-Z][a-z]+.*") && 
                line.length() >= 6 && line.length() <= 30) {
                
                String[] words = line.split("\\s+");
                if (words.length >= 2 && words.length <= 4) {
                    boolean validName = true;
                    for (String word : words) {
                        if (word.length() < 2 || !word.matches("^[A-Za-z]+$")) {
                            validName = false;
                            break;
                        }
                    }
                    
                    if (validName) {
                        return postProcessor.correctName(line);
                    }
                }
            }
        }
        
        return "Not Found";
    }

    private boolean isValidName(String name) {
        if (name == null || "Not Found".equals(name) || name.length() < 3) {
            return false;
        }
        
        String[] words = name.split("\\s+");
        if (words.length < 1 || words.length > 4) {
            return false;
        }
        
        for (String word : words) {
            if (word.length() < 2 || !word.matches("^[A-Za-z]+$")) {
                return false;
            }
        }
        
        return true;
    }

    private double calculateTextConfidence(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        
        double score = 0.0;
        
        // Length factor
        score += Math.min(50.0, text.length() * 0.5);
        
        // Character variety
        Set<Character> uniqueChars = new HashSet<>();
        for (char c : text.toCharArray()) {
            uniqueChars.add(c);
        }
        score += Math.min(30.0, uniqueChars.size() * 2);
        
        // Alphabetic ratio
        long alphaCount = text.chars().filter(Character::isLetter).count();
        double alphaRatio = (double) alphaCount / text.length();
        score += alphaRatio * 20.0;
        
        return Math.min(100.0, score);
    }

    private String getMostReliableText(Map<String, Integer> candidates, String fallback) {
        return candidates.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(fallback);
    }

    private String extractIdFromText(String rawText, String documentType) {
        switch (documentType) {
            case "PAN":
                return postProcessor.cleanPanNumber(rawText);
            case "AADHAAR":
                return postProcessor.cleanAadhaarNumber(rawText);
            default:
                String panResult = postProcessor.cleanPanNumber(rawText);
                if (!"Not Found".equals(panResult)) {
                    return panResult;
                }
                return postProcessor.cleanAadhaarNumber(rawText);
        }
    }

    private double calculateOverallConfidence(String rawText, String name, String idNumber, String documentType) {
        double score = 0.0;
        
        // Raw text quality
        if (rawText != null && !rawText.trim().isEmpty()) {
            score += 20.0;
        }
        
        // Document type detection
        if (!"Unknown".equals(documentType)) {
            score += 25.0;
        }
        
        // Name extraction quality
        if (isValidName(name)) {
            score += 30.0;
        }
        
        // ID number extraction
        if (idNumber != null && !"Not Found".equals(idNumber)) {
            if ("PAN".equals(documentType) && idNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
                score += 25.0;
            } else if ("AADHAAR".equals(documentType) && idNumber.replaceAll("\\s", "").matches("\\d{12}")) {
                score += 25.0;
            } else {
                score += 15.0;
            }
        }
        
        return Math.min(100.0, score);
    }

    public String extractSimpleText(MultipartFile file) throws TesseractException, IOException {
        BufferedImage originalImage;
        try (InputStream inputStream = file.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        }

        if (originalImage == null) {
            throw new IOException("Could not read image file");
        }

        BufferedImage processedImage = imageProcessor.preprocessImage(originalImage);
        return ocrEngines.get(0).doOCR(processedImage);
    }
}
