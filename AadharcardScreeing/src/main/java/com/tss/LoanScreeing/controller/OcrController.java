package com.tss.LoanScreeing.controller;

import com.tss.LoanScreeing.model.ExtractedData;
import com.tss.LoanScreeing.service.AdvancedOcrService;
import com.tss.LoanScreeing.service.ZonalOcrService;
import com.tss.LoanScreeing.service.UltraHighAccuracyOcrService;
import com.tss.LoanScreeing.service.FastAadhaarOcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OcrController {

    @Autowired
    private AdvancedOcrService advancedOcrService;
    
    @Autowired
    private ZonalOcrService zonalOcrService;
    
    @Autowired
    private UltraHighAccuracyOcrService ultraHighAccuracyOcrService;
    
    @Autowired
    private FastAadhaarOcrService fastAadhaarOcrService;

    @PostMapping("/extract-text")
    public ResponseEntity<?> extractText(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: File is empty.");
            }

            // Step 1: Try fast Aadhaar OCR service first (optimized for Aadhaar cards)
            BufferedImage image = ImageIO.read(file.getInputStream());
            ExtractedData extractedData = fastAadhaarOcrService.extractFromAadhaarCard(image);
            
            // Step 2: Fallback to ultra-high accuracy if fast service didn't extract key fields
            if (extractedData == null || 
                (extractedData.getAadhaarNumber() == null && extractedData.getName() == null)) {
                System.out.println("Fast OCR didn't extract key fields, falling back to ultra-high accuracy...");
                extractedData = ultraHighAccuracyOcrService.extractWithUltraHighAccuracy(image);
            }
            
            // Step 3: Final fallback to zonal OCR
            if (extractedData == null || 
                (extractedData.getAadhaarNumber() == null && extractedData.getName() == null)) {
                System.out.println("Ultra-high accuracy OCR failed, using zonal OCR as final fallback...");
                extractedData = zonalOcrService.extractDataFromImage(file);
            }
            
            return ResponseEntity.ok(extractedData);
            
        } catch (TesseractException e) {
            return ResponseEntity.internalServerError().body("OCR Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("IO Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected Error: " + e.getMessage());
        }
    }

    @PostMapping("/extract-text-simple")
    public ResponseEntity<String> extractTextSimple(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: File is empty.");
            }

            String extractedText = zonalOcrService.extractSimpleText(file);
            return ResponseEntity.ok(extractedText);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/extract-text-ultra")
    public ResponseEntity<?> extractTextUltraAccuracy(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: File is empty.");
            }

            // Use the enhanced OCR with comprehensive text correction
            ExtractedData extractedData = ultraHighAccuracyOcrService.extractWithComprehensiveCorrection(
                    javax.imageio.ImageIO.read(file.getInputStream())
            );
            
            return ResponseEntity.ok(extractedData);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("IO Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/extract-text-with-correction")
    public ResponseEntity<?> extractTextWithCorrection(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: File is empty.");
            }

            // Try ultra-high accuracy first (best results)
            ExtractedData extractedData = ultraHighAccuracyOcrService.extractWithComprehensiveCorrection(
                    javax.imageio.ImageIO.read(file.getInputStream())
            );
            
            // If ultra-high accuracy doesn't provide good results, try advanced OCR
            if (extractedData.getConfidenceScore() < 60.0) {
                ExtractedData fallbackData = advancedOcrService.extractDataFromImage(file);
                if (fallbackData != null && fallbackData.getConfidenceScore() > extractedData.getConfidenceScore()) {
                    extractedData = fallbackData;
                }
            }
            
            // Last resort: zonal OCR
            if (extractedData.getConfidenceScore() < 40.0) {
                ExtractedData zonalData = zonalOcrService.extractDataFromImage(file);
                if (zonalData != null && (extractedData.getName() == null || extractedData.getAadhaarNumber() == null)) {
                    // Merge the best parts from each extraction
                    if (extractedData.getName() == null && zonalData.getName() != null) {
                        extractedData.setName(zonalData.getName());
                    }
                    if (extractedData.getAadhaarNumber() == null && zonalData.getAadhaarNumber() != null) {
                        extractedData.setAadhaarNumber(zonalData.getAadhaarNumber());
                    }
                    if (extractedData.getDateOfBirth() == null && zonalData.getDateOfBirth() != null) {
                        extractedData.setDateOfBirth(zonalData.getDateOfBirth());
                    }
                    if (extractedData.getGender() == null && zonalData.getGender() != null) {
                        extractedData.setGender(zonalData.getGender());
                    }
                    if (extractedData.getAddress() == null && zonalData.getAddress() != null) {
                        extractedData.setAddress(zonalData.getAddress());
                    }
                }
            }
            
            return ResponseEntity.ok(extractedData);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("IO Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected Error: " + e.getMessage());
        }
    }
}
