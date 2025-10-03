package com.tss.LoanScreeing.service;

import com.tss.LoanScreeing.model.ExtractedData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class FastAadhaarOcrServiceTest {

    @InjectMocks
    private FastAadhaarOcrService fastAadhaarOcrService;

    @Mock
    private TextCorrectionService textCorrectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testImageCroppingForLandscapeOrientation() {
        // Create a test landscape image (1000x600)
        BufferedImage testImage = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = testImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 1000, 600);
        g2d.dispose();

        // Test extraction (this will test the cropping logic)
        ExtractedData result = fastAadhaarOcrService.extractFromAadhaarCard(testImage);
        
        // Should not throw exception and return a result object
        assertNotNull(result);
    }

    @Test
    public void testImageCroppingForPortraitOrientation() {
        // Create a test portrait image (600x1000)
        BufferedImage testImage = new BufferedImage(600, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = testImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 600, 1000);
        g2d.dispose();

        // Test extraction (this will test the cropping logic)
        ExtractedData result = fastAadhaarOcrService.extractFromAadhaarCard(testImage);
        
        // Should not throw exception and return a result object
        assertNotNull(result);
    }

    @Test
    public void testExtractionWithEmptyImage() {
        // Create a small empty image
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = testImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();

        // Test extraction
        ExtractedData result = fastAadhaarOcrService.extractFromAadhaarCard(testImage);
        
        // Should handle gracefully and return a result object
        assertNotNull(result);
    }

    @Test
    public void testNullImageHandling() {
        // Test with null image - should handle gracefully
        try {
            ExtractedData result = fastAadhaarOcrService.extractFromAadhaarCard(null);
            // Should either return null or handle gracefully
            // Implementation specific behavior
        } catch (Exception e) {
            // Expected behavior for null input
            assertTrue(e instanceof NullPointerException || e instanceof IllegalArgumentException);
        }
    }
}
