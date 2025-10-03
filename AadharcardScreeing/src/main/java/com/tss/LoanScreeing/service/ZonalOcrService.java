package com.tss.LoanScreeing.service;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tss.LoanScreeing.model.ExtractedData;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class ZonalOcrService {

    @Autowired
    private ImageProcessor imageProcessor;

    @Autowired
    private PostProcessor postProcessor;

    private final Tesseract tesseract;

    public ZonalOcrService() {
        tesseract = new Tesseract();
        try {
            // Set the tessdata path - you might need to adjust this path
            tesseract.setDatapath("tessdata");
            tesseract.setLanguage("eng");
            tesseract.setPageSegMode(6); // Uniform block of text
            tesseract.setOcrEngineMode(1); // Neural nets LSTM OCR engine
            
            // Additional configurations for better accuracy
            tesseract.setVariable("tessedit_char_whitelist", 
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .-/()");
            tesseract.setVariable("tessedit_do_invert", "0");
            tesseract.setVariable("textord_really_old_xheight", "1");
            tesseract.setVariable("preserve_interword_spaces", "1");
            
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize Tesseract properly: " + e.getMessage());
            System.err.println("Make sure tessdata folder with eng.traineddata is in the project root");
        }
    }

    public ExtractedData extractDataFromImage(MultipartFile file) throws TesseractException, IOException {
        // Read the uploaded image
        BufferedImage originalImage;
        try (InputStream inputStream = file.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        }

        if (originalImage == null) {
            throw new IOException("Could not read image file");
        }

        // Preprocess the image
        BufferedImage processedImage = imageProcessor.preprocessImage(originalImage);

        // Extract full text first
        String rawText = tesseract.doOCR(processedImage);

        // Detect document type
        String documentType = postProcessor.detectDocumentType(rawText);

        // Extract specific data based on document type
        ExtractedData extractedData = new ExtractedData();
        extractedData.setRawText(rawText);
        extractedData.setDocumentType(documentType);

        // Extract name using improved text analysis
        String extractedName = postProcessor.extractNameFromRawText(rawText);
        extractedData.setName(extractedName);

        // Extract ID number
        String extractedId = extractIdFromText(rawText, documentType);
        extractedData.setIdNumber(extractedId);

        // Extract date of birth
        String dateOfBirth = postProcessor.extractDateOfBirth(rawText);
        extractedData.setDateOfBirth(dateOfBirth);

        // Extract address
        String address = postProcessor.extractAddress(rawText);
        extractedData.setAddress(address);

        // Calculate confidence score
        double confidenceScore = postProcessor.calculateConfidenceScore(
            rawText, extractedData.getName(), extractedData.getIdNumber(), documentType);
        extractedData.setConfidenceScore(confidenceScore);

        return extractedData;
    }

    public String extractSimpleText(MultipartFile file) throws TesseractException, IOException {
        BufferedImage originalImage;
        try (InputStream inputStream = file.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        }

        if (originalImage == null) {
            throw new IOException("Could not read image file");
        }

        // Simple preprocessing
        BufferedImage processedImage = imageProcessor.simplePreprocess(originalImage);

        // Extract text
        return tesseract.doOCR(processedImage);
    }

    private String extractNameFromZones(BufferedImage image, String documentType) {
        try {
            Rectangle nameZone = getNameZone(image, documentType);
            if (nameZone != null) {
                // Create a subimage for the zone
                BufferedImage subImage = image.getSubimage(
                    nameZone.x, nameZone.y, nameZone.width, nameZone.height);
                return tesseract.doOCR(subImage);
            }
        } catch (Exception e) {
            System.err.println("Zonal extraction failed, using full text: " + e.getMessage());
        }
        return "";
    }

    private Rectangle getNameZone(BufferedImage image, String documentType) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Define approximate zones based on document type
        // These are rough estimates and may need adjustment based on actual card layouts
        switch (documentType) {
            case "AADHAAR":
                // Aadhaar name is typically in the upper-middle section
                return new Rectangle(width / 8, height / 4, width * 3 / 4, height / 8);
            
            case "PAN":
                // PAN name is typically in the middle section
                return new Rectangle(width / 6, height * 2 / 5, width * 2 / 3, height / 6);
            
            default:
                // Generic zone for unknown document types
                return new Rectangle(width / 10, height / 5, width * 4 / 5, height / 3);
        }
    }

    private String extractIdFromText(String rawText, String documentType) {
        switch (documentType) {
            case "PAN":
                return postProcessor.cleanPanNumber(rawText);
            case "AADHAAR":
                return postProcessor.cleanAadhaarNumber(rawText);
            default:
                // Try both patterns for unknown documents
                String panResult = postProcessor.cleanPanNumber(rawText);
                if (!"Not Found".equals(panResult)) {
                    return panResult;
                }
                return postProcessor.cleanAadhaarNumber(rawText);
        }
    }
}
