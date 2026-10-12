package com.medicalexplainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedicalExplainerApplication {

    private static final Logger logger = LoggerFactory.getLogger(MedicalExplainerApplication.class);

    public static void main(String[] args) {
        validateEnvironment();
        SpringApplication.run(MedicalExplainerApplication.class, args);
    }

    private static void validateEnvironment() {
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");
        String serverPort = System.getenv("PORT");
        boolean isRender = System.getenv("RENDER") != null;

        logger.info("================================================================================");
        logger.info("Starting AI Medical Explainer Backend");
        logger.info("Application Port     : {}", serverPort != null ? serverPort : "8080 (default)");
        logger.info("Target DB Host       : {}", dbHost != null ? dbHost : "localhost (default)");
        logger.info("Target DB Port       : {}", dbPort != null ? dbPort : "3306 (default)");
        logger.info("Target DB Name       : {}", dbName != null ? dbName : "medical_explainer (default)");
        logger.info("Target DB User       : {}", dbUser != null ? dbUser : "root (default)");
        logger.info("Target DB Password   : {}", (dbPassword != null && !dbPassword.isBlank()) ? "[CONFIGURED SECURELY]" : "[NOT SET / DEFAULT]");
        logger.info("SSL Mode             : REQUIRED");
        logger.info("Environment Detected : {}", isRender ? "Render Cloud" : "Local / Standard");
        logger.info("================================================================================");

        // Render cloud check: if running on Render, DB_HOST must not point to localhost
        if (isRender && (dbHost == null || dbHost.isBlank() || dbHost.equalsIgnoreCase("localhost") || dbHost.equals("127.0.0.1"))) {
            logger.error("""
                
                ********************************************************************************
                RENDER DEPLOYMENT ERROR: DATABASE HOST NOT CONFIGURED!
                
                The backend detected that it is running on Render, but DB_HOST is not set or
                is pointing to localhost. Render containers do not have a local MySQL server!
                
                Please add these Environment Variables in your Render Dashboard:
                  - DB_HOST     : mysql-13f24d04-manthribindusree-fa10.b.aivencloud.com
                  - DB_PORT     : 25432
                  - DB_NAME     : defaultdb
                  - DB_USERNAME : avnadmin
                  - DB_PASSWORD : <Your Aiven MySQL Password>
                ********************************************************************************
                """);
            throw new IllegalStateException("DB_HOST is missing or points to localhost on Render. Please configure DB_HOST in your Render Dashboard.");
        }

        // Remote host check: if a remote database host (like Aiven) is configured, DB_PASSWORD must be provided
        if (dbHost != null && !dbHost.isBlank() && !dbHost.equalsIgnoreCase("localhost") && !dbHost.equals("127.0.0.1")) {
            if (dbPassword == null || dbPassword.isBlank()) {
                logger.error("""
                    
                    ********************************************************************************
                    DATABASE CONFIGURATION ERROR: MISSING DB_PASSWORD!
                    
                    A remote database host ('{}') is configured, but the DB_PASSWORD environment
                    variable is missing or empty.
                    
                    Please configure the DB_PASSWORD environment variable in your Render Dashboard.
                    ********************************************************************************
                    """, dbHost);
                throw new IllegalStateException("DB_PASSWORD is missing for remote database host: " + dbHost + ". Please set DB_PASSWORD in your environment.");
            }
        }
    }
}
