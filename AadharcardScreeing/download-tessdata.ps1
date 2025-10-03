#!/usr/bin/env pwsh
# Script to download Tesseract language data files

Write-Host "Downloading Tesseract language data..." -ForegroundColor Green

# Create tessdata directory if it doesn't exist
if (!(Test-Path "tessdata")) {
    New-Item -ItemType Directory -Path "tessdata"
    Write-Host "Created tessdata directory" -ForegroundColor Yellow
}

# Download English language data
$engUrl = "https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata"
$engPath = "tessdata/eng.traineddata"

try {
    Write-Host "Downloading eng.traineddata..." -ForegroundColor Blue
    Invoke-WebRequest -Uri $engUrl -OutFile $engPath -UseBasicParsing
    Write-Host "✓ Successfully downloaded eng.traineddata" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to download eng.traineddata: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Verify file was downloaded
if (Test-Path $engPath) {
    $fileSize = (Get-Item $engPath).Length / 1MB
    Write-Host "✓ File verified. Size: $([math]::Round($fileSize, 2)) MB" -ForegroundColor Green
} else {
    Write-Host "✗ File verification failed" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Tesseract setup complete!" -ForegroundColor Green
Write-Host "You can now run: mvn spring-boot:run" -ForegroundColor Cyan
