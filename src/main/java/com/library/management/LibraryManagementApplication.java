package com.library.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application Class
 * 
 * This is the entry point of our Library Management System.
 * 
 * @SpringBootApplication is a convenience annotation that combines:
 * - @Configuration: Marks this class as a source of bean definitions
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Enables component scanning in the package
 * 
 * @EnableJpaAuditing enables automatic auditing of JPA entities
 * (like automatically setting created/updated timestamps)
 */
@SpringBootApplication
@EnableJpaAuditing
public class LibraryManagementApplication {

    /**
     * Main method - Application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Start the Spring Boot application
        SpringApplication.run(LibraryManagementApplication.class, args);
        
        // Print welcome message
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║                                                              ║\n" +
            "║        🏛️  LIBRARY MANAGEMENT SYSTEM STARTED! 🏛️            ║\n" +
            "║                                                              ║\n" +
            "║  📚 API Documentation: http://localhost:8080/api/v1/swagger  ║\n" +
            "║  🔍 H2 Console: http://localhost:8080/api/v1/h2-console     ║\n" +
            "║  📊 Actuator: http://localhost:8080/api/v1/actuator         ║\n" +
            "║                                                              ║\n" +
            "║  🚀 Ready to manage your library! 🚀                        ║\n" +
            "║                                                              ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n");
    }
}

