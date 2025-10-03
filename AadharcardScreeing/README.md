# Aadhaar Card OCR Project

This Spring Boot application provides advanced OCR (Optical Character Recognition) capabilities for extracting text from identity documents like Aadhaar cards, PAN cards, and other ID documents.

## Features

- 🔍 **Advanced OCR**: Uses Tesseract 4.x with LSTM neural networks
- 🖼️ **Image Preprocessing**: OpenCV-based image enhancement for better accuracy
- 🎯 **Zonal OCR**: Targeted text extraction from specific document regions
- 🧠 **Intelligent Post-processing**: Regex patterns and validation for structured data
- 🌐 **Web Interface**: Modern, responsive UI for easy image upload
- 📱 **Mobile Friendly**: Works on desktop and mobile devices
- 🚀 **RESTful API**: Easy integration with other applications

## Project Structure

```
AadharcardScreeing/
├── src/
│   ├── main/
│   │   ├── java/com/tss/LoanScreeing/
│   │   │   ├── controller/
│   │   │   │   └── OcrController.java
│   │   │   ├── model/
│   │   │   │   └── ExtractedData.java
│   │   │   ├── service/
│   │   │   │   ├── ImageProcessor.java
│   │   │   │   ├── PostProcessor.java
│   │   │   │   └── ZonalOcrService.java
│   │   │   └── AadharcardScreeingApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html
│   │       └── application.properties
├── tessdata/
│   └── eng.traineddata (to be downloaded)
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Tesseract OCR language data files

## Setup Instructions

### 1. Download Tesseract Language Data

You need to download the English language data file for Tesseract:

1. Go to: https://github.com/tesseract-ocr/tessdata/blob/main/eng.traineddata
2. Download the `eng.traineddata` file
3. Place it in the `tessdata/` folder in your project root

Alternatively, you can use this direct download command:

**Windows (PowerShell):**
```powershell
Invoke-WebRequest -Uri "https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata" -OutFile "tessdata/eng.traineddata"
```

**Linux/Mac:**
```bash
wget -O tessdata/eng.traineddata https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Extract Structured Data
- **URL**: `POST /api/extract-text`
- **Content-Type**: `multipart/form-data`
- **Parameter**: `file` (image file)
- **Response**: JSON with extracted structured data

Example response:
```json
{
  "name": "John Doe",
  "id_number": "ABCDE1234F",
  "date_of_birth": "01/01/1990",
  "address": "123 Main Street, City, State, 123456",
  "document_type": "PAN",
  "raw_text": "Full extracted text...",
  "confidence_score": 85.5
}
```

### 2. Extract Simple Text
- **URL**: `POST /api/extract-text-simple`
- **Content-Type**: `multipart/form-data`
- **Parameter**: `file` (image file)
- **Response**: Plain text

## Web Interface

Open `http://localhost:8080` in your browser to access the web interface where you can:

1. Upload images by clicking or drag & drop
2. View real-time processing status
3. See extracted structured data
4. View confidence scores
5. Access raw extracted text

## Supported Document Types

- ✅ **Aadhaar Card**: Extracts name, Aadhaar number, DOB, address
- ✅ **PAN Card**: Extracts name, PAN number, DOB
- ✅ **Driving License**: Basic text extraction
- ✅ **Voter ID**: Basic text extraction
- ✅ **Passport**: Basic text extraction
- ✅ **Generic ID Cards**: Text extraction with pattern matching

## Image Requirements

- **Formats**: JPG, PNG, GIF
- **Size**: Maximum 10MB
- **Quality**: Higher resolution images (300+ DPI) work better
- **Lighting**: Well-lit images with good contrast
- **Orientation**: Straight, non-skewed documents

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# OCR configuration
ocr.tessdata.path=tessdata
```

## Technology Stack

- **Backend**: Spring Boot 3.x, Java 17
- **OCR Engine**: Tesseract 4.x with LSTM
- **Image Processing**: OpenCV 4.x
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Build Tool**: Maven
- **Dependencies**: 
  - Tess4J 5.9.0
  - OpenCV 4.9.0
  - Apache Commons IO

## Troubleshooting

### Common Issues

1. **"Could not initialize Tesseract"**
   - Make sure `eng.traineddata` is in the `tessdata/` folder
   - Check file permissions

2. **"OpenCV library not found"**
   - The application will fallback to basic image processing
   - OpenCV is optional but recommended for better accuracy

3. **Poor OCR accuracy**
   - Ensure good image quality (300+ DPI)
   - Use well-lit, high-contrast images
   - Make sure documents are straight (not skewed)

4. **File upload errors**
   - Check file size (max 10MB)
   - Ensure file format is supported (JPG, PNG, GIF)

### Performance Tips

1. **Image Quality**: Use high-resolution scans (300+ DPI)
2. **Preprocessing**: The app automatically enhances images
3. **Document Alignment**: Straight documents work better
4. **Lighting**: Even, bright lighting improves accuracy
5. **File Size**: Larger files take more time to process

## Development

### Adding New Document Types

1. Update `PostProcessor.detectDocumentType()` method
2. Add specific extraction patterns in `PostProcessor`
3. Update zonal coordinates in `ZonalOcrService.getNameZone()`

### Improving Accuracy

1. Fine-tune image preprocessing parameters in `ImageProcessor`
2. Add more regex patterns in `PostProcessor`
3. Adjust zonal coordinates for specific document layouts
4. Add custom OCR configurations for different document types

## License

This project is for educational and development purposes.

## Support

For issues and questions, please check:
1. Ensure all dependencies are properly installed
2. Verify tessdata files are in the correct location
3. Check application logs for detailed error messages
