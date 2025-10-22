package com.library.management.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base Entity Class
 * 
 * This abstract class provides common fields that all entities should have:
 * - id: Primary key
 * - createdAt: When the record was created
 * - updatedAt: When the record was last modified
 * 
 * @MappedSuperclass: This class is not an entity itself, but provides
 * common mapping information for its subclasses.
 * 
 * @EntityListeners: Enables automatic auditing (setting timestamps)
 * 
 * @Data: Lombok annotation that generates getters, setters, toString, etc.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class BaseEntity {

    /**
     * Primary Key
     * @Id: Marks this field as the primary key
     * @GeneratedValue: Auto-generates the ID value
     * @Column: Specifies column properties
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Creation Timestamp
     * @CreatedDate: Automatically set when entity is created
     * @Column: Specifies column name and properties
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last Update Timestamp
     * @LastModifiedDate: Automatically updated when entity is modified
     * @Column: Specifies column name and properties
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Soft Delete Flag
     * Instead of actually deleting records, we mark them as deleted
     * This allows for data recovery and audit trails
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}

