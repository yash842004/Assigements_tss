package com.tss.LoanScreeing.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

@Service
public class ImageProcessor {

    static {
        try {
            nu.pattern.OpenCV.loadLocally();
            System.out.println("OpenCV loaded successfully");
        } catch (Exception e) {
            System.err.println("Warning: Could not load OpenCV library: " + e.getMessage());
        }
    }

    public BufferedImage preprocessImage(BufferedImage originalImage) throws IOException {
        try {
            // Apply multiple preprocessing steps for maximum accuracy
            BufferedImage processed = originalImage;
            
            // Step 1: Upscale image for better OCR (minimum 300 DPI equivalent)
            processed = upscaleImage(processed);
            
            // Step 2: Advanced OpenCV preprocessing
            processed = advancedOpenCVPreprocessing(processed);
            
            // Step 3: Noise reduction
            processed = reduceNoise(processed);
            
            // Step 4: Enhance text regions
            processed = enhanceTextRegions(processed);
            
            return processed;
            
        } catch (Exception e) {
            System.err.println("Advanced preprocessing failed, using fallback: " + e.getMessage());
            return advancedSimplePreprocess(originalImage);
        }
    }

    private BufferedImage upscaleImage(BufferedImage image) {
        try {
            // Calculate target size (minimum 2000px width for good OCR)
            int targetWidth = Math.max(2000, image.getWidth() * 2);
            int targetHeight = (int) ((double) targetWidth / image.getWidth() * image.getHeight());
            
            ResampleOp resampleOp = new ResampleOp(targetWidth, targetHeight);
            resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
            return resampleOp.filter(image, null);
            
        } catch (Exception e) {
            System.err.println("Upscaling failed: " + e.getMessage());
            return image;
        }
    }

    private BufferedImage advancedOpenCVPreprocessing(BufferedImage image) throws IOException {
        try {
            Mat src = bufferedImageToMat(image);
            
            // Convert to grayscale
            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
            
            // Apply CLAHE (Contrast Limited Adaptive Histogram Equalization)
            org.opencv.imgproc.CLAHE clahe = Imgproc.createCLAHE();
            clahe.setClipLimit(3.0);
            clahe.setTilesGridSize(new Size(8, 8));
            Mat enhanced = new Mat();
            clahe.apply(gray, enhanced);
            
            // Gaussian blur to reduce noise
            Mat blurred = new Mat();
            Imgproc.GaussianBlur(enhanced, blurred, new Size(1, 1), 0);
            
            // Adaptive threshold for better text separation
            Mat thresh = new Mat();
            Imgproc.adaptiveThreshold(blurred, thresh, 255, 
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);
            
            // Morphological operations to clean up text
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));
            Mat cleaned = new Mat();
            Imgproc.morphologyEx(thresh, cleaned, Imgproc.MORPH_CLOSE, kernel);
            
            // Dilation to make text thicker
            Mat dilated = new Mat();
            Imgproc.dilate(cleaned, dilated, kernel, new org.opencv.core.Point(-1, -1), 1);
            
            return matToBufferedImage(dilated);
            
        } catch (Exception e) {
            System.err.println("OpenCV preprocessing failed: " + e.getMessage());
            return image;
        }
    }

    private BufferedImage reduceNoise(BufferedImage image) {
        try {
            // Apply median filter to reduce noise
            float[] medianKernel = {
                0.0f, 0.2f, 0.0f,
                0.2f, 0.2f, 0.2f,
                0.0f, 0.2f, 0.0f
            };
            
            Kernel kernel = new Kernel(3, 3, medianKernel);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            return op.filter(image, null);
            
        } catch (Exception e) {
            System.err.println("Noise reduction failed: " + e.getMessage());
            return image;
        }
    }

    private BufferedImage enhanceTextRegions(BufferedImage image) {
        BufferedImage enhanced = new BufferedImage(
            image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2d = enhanced.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int gray = (rgb >> 16) & 0xFF; // Already grayscale
                
                // Enhanced binary threshold with hysteresis
                if (gray > 180) {
                    enhanced.setRGB(x, y, Color.WHITE.getRGB());
                } else if (gray < 80) {
                    enhanced.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    // Check neighbors for edge enhancement
                    int neighborSum = 0;
                    int neighborCount = 0;
                    
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            int nx = x + dx;
                            int ny = y + dy;
                            if (nx >= 0 && nx < image.getWidth() && ny >= 0 && ny < image.getHeight()) {
                                int neighborRgb = image.getRGB(nx, ny);
                                int neighborGray = (neighborRgb >> 16) & 0xFF;
                                neighborSum += neighborGray;
                                neighborCount++;
                            }
                        }
                    }
                    
                    int avgNeighbor = neighborSum / neighborCount;
                    if (gray > avgNeighbor + 10) {
                        enhanced.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        enhanced.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }
            }
        }
        
        g2d.dispose();
        return enhanced;
    }

    private BufferedImage advancedSimplePreprocess(BufferedImage originalImage) {
        // Fallback preprocessing when OpenCV is not available
        BufferedImage processed = new BufferedImage(
            originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int rgb = originalImage.getRGB(x, y);
                
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                // Enhanced grayscale conversion
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                
                // Advanced contrast enhancement with gamma correction
                double gamma = 1.2;
                gray = (int) (255 * Math.pow(gray / 255.0, gamma));
                
                // Adaptive thresholding
                if (gray > 140) {
                    gray = 255;
                } else if (gray < 90) {
                    gray = 0;
                } else {
                    // Use Otsu-like threshold
                    gray = gray > 115 ? 255 : 0;
                }
                
                int newRgb = (gray << 16) | (gray << 8) | gray;
                processed.setRGB(x, y, newRgb);
            }
        }
        
        return processed;
    }

    private Mat bufferedImageToMat(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        Mat mat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_GRAYSCALE);
        
        return mat;
    }

    private BufferedImage matToBufferedImage(Mat mat) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        
        byte[] imageBytes = matOfByte.toArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        
        return ImageIO.read(byteArrayInputStream);
    }

    public BufferedImage enhanceContrast(BufferedImage originalImage) {
        return advancedSimplePreprocess(originalImage);
    }

    // Simple fallback for compatibility
    public BufferedImage simplePreprocess(BufferedImage originalImage) {
        return advancedSimplePreprocess(originalImage);
    }
}
