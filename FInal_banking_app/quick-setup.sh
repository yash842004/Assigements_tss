#!/bin/bash

# Banking Application Quick Setup Script
# This script helps you quickly set up and test the banking application

echo "🏦 Banking Application Quick Setup"
echo "=================================="

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "📋 Checking prerequisites..."

if ! command_exists java; then
    echo "❌ Java not found. Please install Java 17+"
    exit 1
fi

if ! command_exists mvn; then
    echo "❌ Maven not found. Please install Maven 3.6+"
    exit 1
fi

echo "✅ Java and Maven found"

# Check if application.properties exists
if [ ! -f "src/main/resources/application.properties" ]; then
    echo "❌ application.properties not found"
    exit 1
fi

# Build the application
echo ""
echo "🔨 Building the application..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo "✅ Build successful"

# Start the application in background
echo ""
echo "🚀 Starting the application..."
mvn spring-boot:run &
APP_PID=$!

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 30

# Check if application is running
if curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "✅ Application is running at http://localhost:8080"
    echo ""
    echo "🎯 Next Steps:"
    echo "1. Import the Postman collection: Complete_Banking_API_Collection.json"
    echo "2. Follow the testing guide: COMPLETE_API_TESTING_GUIDE.md"
    echo "3. Start with admin login: POST /api/auth/admin/login"
    echo "   Username: admin, Password: admin123"
    echo ""
    echo "📖 API Documentation: http://localhost:8080/swagger-ui.html"
    echo "🗄️ H2 Console (if using H2): http://localhost:8080/h2-console"
    echo ""
    echo "To stop the application: kill $APP_PID"
else
    echo "❌ Application failed to start"
    kill $APP_PID 2>/dev/null
    exit 1
fi
