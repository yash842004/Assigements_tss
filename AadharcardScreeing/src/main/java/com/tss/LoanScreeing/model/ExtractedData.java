package com.tss.LoanScreeing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtractedData {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("id_number")
    private String idNumber;
    
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("document_type")
    private String documentType;
    
    @JsonProperty("raw_text")
    private String rawText;
    
    @JsonProperty("confidence_score")
    private double confidenceScore;
    
    @JsonProperty("gender")
    private String gender;

    // Default constructor
    public ExtractedData() {}

    // Constructor with all fields
    public ExtractedData(String name, String idNumber, String dateOfBirth, String address, 
                        String documentType, String rawText, double confidenceScore) {
        this.name = name;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.documentType = documentType;
        this.rawText = rawText;
        this.confidenceScore = confidenceScore;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Convenience methods for Aadhaar number (using idNumber field)
    public String getAadhaarNumber() {
        return idNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.idNumber = aadhaarNumber;
    }

    @Override
    public String toString() {
        return "ExtractedData{" +
                "name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", documentType='" + documentType + '\'' +
                ", confidenceScore=" + confidenceScore +
                '}';
    }
}
