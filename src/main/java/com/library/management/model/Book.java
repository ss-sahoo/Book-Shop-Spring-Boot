package com.library.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Book Entity
 * 
 * Represents a book in our library system.
 * 
 * @Entity: Marks this class as a JPA entity
 * @Table: Specifies the database table name
 * @Data: Lombok annotation for getters, setters, toString, etc.
 * @EqualsAndHashCode: Lombok annotation for equals and hashCode methods
 * @NoArgsConstructor: Lombok annotation for no-args constructor
 * @AllArgsConstructor: Lombok annotation for all-args constructor
 */
@Entity
@Table(name = "books")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    /**
     * Book Title
     * @NotBlank: Ensures the field is not null, empty, or whitespace
     * @Size: Specifies minimum and maximum length
     * @Column: Specifies column properties
     */
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Book Author
     */
    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    @Column(name = "author", nullable = false)
    private String author;

    /**
     * Book ISBN (International Standard Book Number)
     * @Pattern: Validates the ISBN format using regex
     */
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
             message = "Invalid ISBN format")
    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    /**
     * Book Publisher
     */
    @NotBlank(message = "Publisher is required")
    @Size(min = 1, max = 255, message = "Publisher must be between 1 and 255 characters")
    @Column(name = "publisher", nullable = false)
    private String publisher;

    /**
     * Publication Date
     * @Past: Ensures the date is in the past
     */
    @NotNull(message = "Publication date is required")
    @Past(message = "Publication date must be in the past")
    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    /**
     * Book Category/Genre
     */
    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    @Column(name = "category", nullable = false)
    private String category;

    /**
     * Number of Pages
     * @Min: Ensures minimum value
     * @Max: Ensures maximum value
     */
    @NotNull(message = "Number of pages is required")
    @Min(value = 1, message = "Number of pages must be at least 1")
    @Max(value = 10000, message = "Number of pages cannot exceed 10000")
    @Column(name = "pages", nullable = false)
    private Integer pages;

    /**
     * Book Price
     * @DecimalMin: Ensures minimum decimal value
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Book Description
     */
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Number of Copies Available
     */
    @NotNull(message = "Number of copies is required")
    @Min(value = 0, message = "Number of copies cannot be negative")
    @Column(name = "copies_available", nullable = false)
    private Integer copiesAvailable = 1;

    /**
     * Total Number of Copies
     */
    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies = 1;

    /**
     * Book Status
     * @Enumerated: Maps enum to database
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    /**
     * Book Language
     */
    @NotBlank(message = "Language is required")
    @Size(min = 2, max = 50, message = "Language must be between 2 and 50 characters")
    @Column(name = "language", nullable = false)
    private String language = "English";

    /**
     * Book Cover Image URL
     */
    @Size(max = 500, message = "Cover image URL cannot exceed 500 characters")
    @Column(name = "cover_image_url")
    private String coverImageUrl;

    /**
     * Borrowing Records for this Book
     * @OneToMany: One book can have many borrowing records
     * @JoinColumn: Specifies the foreign key column
     * @OrderBy: Orders the results by borrowing date
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("borrowedDate DESC")
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();

    /**
     * Book Status Enumeration
     */
    public enum BookStatus {
        AVAILABLE("Available"),
        BORROWED("Borrowed"),
        RESERVED("Reserved"),
        MAINTENANCE("Under Maintenance"),
        LOST("Lost"),
        DAMAGED("Damaged");

        private final String displayName;

        BookStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Business Logic Methods
     */

    /**
     * Check if the book is available for borrowing
     * @return true if book is available, false otherwise
     */
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE && copiesAvailable > 0;
    }

    /**
     * Borrow a copy of the book
     * @return true if successful, false if no copies available
     */
    public boolean borrowCopy() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
            if (copiesAvailable == 0) {
                status = BookStatus.BORROWED;
            }
            return true;
        }
        return false;
    }

    /**
     * Return a copy of the book
     * @return true if successful, false if all copies are already returned
     */
    public boolean returnCopy() {
        if (copiesAvailable < totalCopies) {
            copiesAvailable++;
            if (status == BookStatus.BORROWED && copiesAvailable > 0) {
                status = BookStatus.AVAILABLE;
            }
            return true;
        }
        return false;
    }

    /**
     * Get the availability percentage
     * @return percentage of copies available
     */
    public double getAvailabilityPercentage() {
        if (totalCopies == 0) return 0.0;
        return (double) copiesAvailable / totalCopies * 100;
    }
}
