# Aadhaar Card OCR - Optimized High-Accuracy Pipeline

## Overview
This application provides high-accuracy text extraction specifically optimized for Aadhaar cards. The system uses a multi-tiered approach for maximum accuracy and performance.

## Key Optimizations

### 1. FastAadhaarOcrService (Primary Extraction Engine)
- **Smart Image Cropping**: Automatically detects and crops to text regions only
  - Excludes photo area (typically left 35% of landscape cards)
  - Excludes QR code area (bottom portion)
  - Handles both landscape and portrait orientations
- **Optimized OCR Settings**: 
  - LSTM neural network engine for better accuracy
  - Character whitelist optimized for Aadhaar content
  - Reduced dictionary loading for faster processing
- **Fast Field Extraction**: Pre-compiled regex patterns for quick pattern matching

### 2. Image Processing Pipeline
- **Selective Preprocessing**: Only applies necessary enhancements
  - Grayscale conversion with contrast optimization
  - Focus on text clarity rather than photo quality
- **Region-Specific Processing**: Different strategies for landscape vs portrait cards

### 3. Multi-Tier Extraction Strategy
1. **Primary**: FastAadhaarOcrService (optimized for speed and accuracy)
2. **Fallback 1**: UltraHighAccuracyOcrService (comprehensive correction)
3. **Fallback 2**: ZonalOcrService (final attempt with different approach)

### 4. Text Correction Service
- **Fuzzy String Matching**: Corrects common OCR errors
- **Pattern-Based Corrections**: Fixes known Aadhaar-specific text patterns
- **Field-Specific Validation**: Different correction strategies for names, addresses, numbers

## Performance Benefits

### Reduced Processing Load
- **70-80% reduction** in image processing area by excluding photo/QR regions
- **Faster OCR processing** with optimized character whitelists
- **Quick field extraction** using pre-compiled patterns

### High Accuracy
- **Multi-tier fallback** ensures extraction even with poor image quality
- **Smart text correction** fixes common OCR misreadings
- **Context-aware field validation** improves data quality

## Supported Aadhaar Card Fields
- **Name**: Full name extraction with correction
- **Aadhaar Number**: 12-digit number with proper formatting
- **Date of Birth**: Multiple date format support
- **Gender**: Male/Female detection
- **Address**: Complete address extraction with validation

## Usage
1. Upload an Aadhaar card image (JPG, PNG, GIF)
2. The system automatically:
   - Detects card orientation
   - Crops to text regions
   - Applies fast OCR extraction
   - Corrects common errors
   - Returns structured data

## API Endpoint
- **POST** `/api/extract-text`
- **Input**: MultipartFile (image)
- **Output**: ExtractedData JSON with all fields

## Technical Stack
- **Backend**: Spring Boot with Java 17
- **OCR Engine**: Tesseract 4.x with LSTM models
- **Image Processing**: Java BufferedImage with custom algorithms
- **Text Correction**: Custom fuzzy matching and pattern correction

## Deployment
The application runs on `localhost:8080` with a simple, optimized web interface focused solely on Aadhaar card processing.
