package com.tss.security.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * RSA Key Configuration for JWT Token signing and verification
 * Uses asymmetric cryptography for enhanced security
 */
@Configuration
public class RSAKeyConfig {

    @Value("${app.jwt.rsa.key-size:2048}")
    private int keySize;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    /**
     * Initialize RSA key pair on application startup
     * In production, you should load keys from secure storage
     */
    @PostConstruct
    public void initKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            this.publicKey = (RSAPublicKey) keyPair.getPublic();
            
            System.out.println("RSA Key Pair generated successfully");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() {
        return this.privateKey;
    }

    @Bean
    public RSAPublicKey rsaPublicKey() {
        return this.publicKey;
    }

    // Utility methods for accessing keys
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
