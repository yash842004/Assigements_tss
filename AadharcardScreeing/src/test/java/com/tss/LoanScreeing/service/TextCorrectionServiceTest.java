package com.tss.LoanScreeing.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TextCorrectionServiceTest {

    @Autowired
    private TextCorrectionService textCorrectionService;

    @Test
    public void testCorrectName() {
        // Test basic name correction
        String correctedName = textCorrectionService.correctName("4M1T KUM4R");
        assertEquals("AMIT KUMAR", correctedName);
        
        // Test with fuzzy matching
        String correctedName2 = textCorrectionService.correctName("PRIY4 SH4RM4");
        assertEquals("PRIYA SHARMA", correctedName2);
    }

    @Test
    public void testCorrectAadhaarNumber() {
        // Test Aadhaar number correction
        String correctedAadhaar = textCorrectionService.correctAadhaarNumber("123O 45G7 8901");
        assertEquals("123045678901", correctedAadhaar);
        
        // Test with more OCR errors
        String correctedAadhaar2 = textCorrectionService.correctAadhaarNumber("I234 5G78 90I2");
        assertEquals("123456789012", correctedAadhaar2);
    }

    @Test
    public void testCorrectDateOfBirth() {
        // Test date correction
        String correctedDob = textCorrectionService.correctDateOfBirth("I5/O8/I990");
        assertEquals("15/08/1990", correctedDob);
    }

    @Test
    public void testCorrectGender() {
        // Test gender correction
        String correctedGender = textCorrectionService.correctGender("M4LE");
        assertEquals("MALE", correctedGender);
        
        String correctedGender2 = textCorrectionService.correctGender("FEM4LE");
        assertEquals("FEMALE", correctedGender2);
    }

    @Test
    public void testCorrectTextFromMultipleResults() {
        // Test multiple result correction
        List<String> ocrResults = Arrays.asList(
            "4MIT KUM4R",
            "AMIT KUMAR",
            "AM1T KUM4R"
        );
        
        String corrected = textCorrectionService.correctTextFromMultipleResults(ocrResults);
        assertEquals("AMIT KUMAR", corrected);
    }

    @Test
    public void testCorrectSingleText() {
        // Test comprehensive single text correction
        String corrected = textCorrectionService.correctSingleText("G0VERNMENT 0F 1ND1A");
        assertTrue(corrected.contains("GOVERNMENT"));
        assertTrue(corrected.contains("INDIA"));
    }

    @Test
    public void testTextQuality() {
        // Test text quality calculation
        double quality = textCorrectionService.calculateTextQuality("AMIT KUMAR");
        assertTrue(quality > 50.0); // Should have decent quality
        
        double qualityLow = textCorrectionService.calculateTextQuality("@#$%");
        assertTrue(qualityLow < 50.0); // Should have low quality
    }

    @Test
    public void testExtractAddressFromAadhaarLayout() {
        // Test address extraction from Aadhaar layout
        String sampleText = "Government of India\nNIKHIL KUMAR\nMale\n29-07-1980\n123, Raj Nagar\nDelhi\n8364 5789 2230";
        String extractedAddress = textCorrectionService.extractAddressFromAadhaarLayout(sampleText);
        
        assertNotNull(extractedAddress);
        assertTrue(extractedAddress.contains("123") || extractedAddress.contains("Raj") || extractedAddress.contains("Delhi"));
    }
}
