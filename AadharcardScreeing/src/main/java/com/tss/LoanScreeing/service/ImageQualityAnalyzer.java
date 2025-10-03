package com.tss.LoanScreeing.service;

import org.springframework.stereotype.Service;
import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ImageQualityAnalyzer {

    public ImageQualityMetrics analyzeImage(BufferedImage image) {
        ImageQualityMetrics metrics = new ImageQualityMetrics();
        
        // Calculate brightness metrics
        metrics.brightness = calculateBrightness(image);
        metrics.contrast = calculateContrast(image);
        metrics.sharpness = calculateSharpness(image);
        metrics.noise = calculateNoise(image);
        metrics.resolution = image.getWidth() * image.getHeight();
        metrics.aspectRatio = (double) image.getWidth() / image.getHeight();
        
        // Determine image quality score (0-100)
        metrics.qualityScore = calculateQualityScore(metrics);
        
        return metrics;
    }

    private double calculateBrightness(BufferedImage image) {
        long totalBrightness = 0;
        int pixels = 0;
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                totalBrightness += (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                pixels++;
            }
        }
        
        return (double) totalBrightness / pixels;
    }

    private double calculateContrast(BufferedImage image) {
        double mean = calculateBrightness(image);
        double variance = 0;
        int pixels = 0;
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                double brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                variance += Math.pow(brightness - mean, 2);
                pixels++;
            }
        }
        
        return Math.sqrt(variance / pixels); // Standard deviation as contrast measure
    }

    private double calculateSharpness(BufferedImage image) {
        // Laplacian operator for edge detection
        double sharpness = 0;
        int count = 0;
        
        for (int y = 1; y < image.getHeight() - 1; y++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                int center = new Color(image.getRGB(x, y)).getRed();
                int top = new Color(image.getRGB(x, y - 1)).getRed();
                int bottom = new Color(image.getRGB(x, y + 1)).getRed();
                int left = new Color(image.getRGB(x - 1, y)).getRed();
                int right = new Color(image.getRGB(x + 1, y)).getRed();
                
                double laplacian = Math.abs(4 * center - top - bottom - left - right);
                sharpness += laplacian;
                count++;
            }
        }
        
        return sharpness / count;
    }

    private double calculateNoise(BufferedImage image) {
        // Simple noise estimation using local variance
        double totalVariance = 0;
        int regions = 0;
        int blockSize = 8;
        
        for (int y = 0; y < image.getHeight() - blockSize; y += blockSize) {
            for (int x = 0; x < image.getWidth() - blockSize; x += blockSize) {
                double blockVariance = calculateBlockVariance(image, x, y, blockSize);
                totalVariance += blockVariance;
                regions++;
            }
        }
        
        return totalVariance / regions;
    }

    private double calculateBlockVariance(BufferedImage image, int startX, int startY, int blockSize) {
        double sum = 0;
        double sumSquared = 0;
        int pixels = 0;
        
        for (int y = startY; y < startY + blockSize && y < image.getHeight(); y++) {
            for (int x = startX; x < startX + blockSize && x < image.getWidth(); x++) {
                int gray = new Color(image.getRGB(x, y)).getRed();
                sum += gray;
                sumSquared += gray * gray;
                pixels++;
            }
        }
        
        double mean = sum / pixels;
        return (sumSquared / pixels) - (mean * mean);
    }

    private double calculateQualityScore(ImageQualityMetrics metrics) {
        double score = 100;
        
        // Penalize poor brightness (too dark or too bright)
        if (metrics.brightness < 50 || metrics.brightness > 200) {
            score -= 20;
        }
        
        // Penalize low contrast
        if (metrics.contrast < 30) {
            score -= 15;
        }
        
        // Penalize low sharpness
        if (metrics.sharpness < 10) {
            score -= 15;
        }
        
        // Penalize high noise
        if (metrics.noise > 100) {
            score -= 10;
        }
        
        // Penalize low resolution
        if (metrics.resolution < 300000) { // Less than ~500x600
            score -= 20;
        }
        
        return Math.max(0, score);
    }

    public PreprocessingStrategy recommendStrategy(ImageQualityMetrics metrics) {
        PreprocessingStrategy strategy = new PreprocessingStrategy();
        
        // Brightness adjustments
        if (metrics.brightness < 100) {
            strategy.brightnessAdjustment = 1.2;
        } else if (metrics.brightness > 180) {
            strategy.brightnessAdjustment = 0.8;
        }
        
        // Contrast enhancement
        if (metrics.contrast < 40) {
            strategy.contrastEnhancement = true;
            strategy.claheEnabled = true;
        }
        
        // Sharpening
        if (metrics.sharpness < 15) {
            strategy.sharpeningStrength = 1.5;
        }
        
        // Noise reduction
        if (metrics.noise > 80) {
            strategy.noiseReductionLevel = 2;
        } else if (metrics.noise > 40) {
            strategy.noiseReductionLevel = 1;
        }
        
        // Upscaling
        if (metrics.resolution < 500000) {
            strategy.upscaleFactor = 2.0;
        } else if (metrics.resolution < 1000000) {
            strategy.upscaleFactor = 1.5;
        }
        
        return strategy;
    }

    public static class ImageQualityMetrics {
        public double brightness;
        public double contrast;
        public double sharpness;
        public double noise;
        public long resolution;
        public double aspectRatio;
        public double qualityScore;
        
        @Override
        public String toString() {
            return String.format("Quality Score: %.1f, Brightness: %.1f, Contrast: %.1f, Sharpness: %.1f, Noise: %.1f",
                    qualityScore, brightness, contrast, sharpness, noise);
        }
    }

    public static class PreprocessingStrategy {
        public double brightnessAdjustment = 1.0;
        public boolean contrastEnhancement = false;
        public boolean claheEnabled = false;
        public double sharpeningStrength = 1.0;
        public int noiseReductionLevel = 0;
        public double upscaleFactor = 1.0;
        
        @Override
        public String toString() {
            return String.format("Brightness: %.2f, Contrast: %b, CLAHE: %b, Sharpening: %.2f, Noise Reduction: %d, Upscale: %.2fx",
                    brightnessAdjustment, contrastEnhancement, claheEnabled, sharpeningStrength, noiseReductionLevel, upscaleFactor);
        }
    }
}
