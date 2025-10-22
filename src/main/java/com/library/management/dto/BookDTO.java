package com.library.management.dto;

import com.library.management.model.Book;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Book Data Transfer Object (DTO)
 * 
 * DTOs are used to transfer data between different layers of the application.
 * They help in:
 * - Decoupling the internal entity structure from the API
 * - Validating input data
 * - Controlling what data is exposed to the client
 * - Versioning APIs
 * 
 * @Data: Lombok annotation for getters, setters, toString, etc.
 * @NoArgsConstructor: Lombok annotation for no-args constructor
 * @AllArgsConstructor: Lombok annotation for all-args constructor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    /**
     * Book ID (for updates, not required for creation)
     */
    private Long id;

    /**
     * Book Title
     * @NotBlank: Ensures the field is not null, empty, or whitespace
     * @Size: Specifies minimum and maximum length
     */
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    /**
     * Book Author
     */
    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    /**
     * Book ISBN
     * @Pattern: Validates the ISBN format using regex
     */
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
             message = "Invalid ISBN format")
    private String isbn;

    /**
     * Book Publisher
     */
    @NotBlank(message = "Publisher is required")
    @Size(min = 1, max = 255, message = "Publisher must be between 1 and 255 characters")
    private String publisher;

    /**
     * Publication Date
     * @Past: Ensures the date is in the past
     */
    @NotNull(message = "Publication date is required")
    @Past(message = "Publication date must be in the past")
    private LocalDate publicationDate;

    /**
     * Book Category/Genre
     */
    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    private String category;

    /**
     * Number of Pages
     * @Min: Ensures minimum value
     * @Max: Ensures maximum value
     */
    @NotNull(message = "Number of pages is required")
    @Min(value = 1, message = "Number of pages must be at least 1")
    @Max(value = 10000, message = "Number of pages cannot exceed 10000")
    private Integer pages;

    /**
     * Book Price
     * @DecimalMin: Ensures minimum decimal value
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    /**
     * Book Description
     */
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    /**
     * Number of Copies Available
     */
    @NotNull(message = "Number of copies is required")
    @Min(value = 0, message = "Number of copies cannot be negative")
    private Integer copiesAvailable;

    /**
     * Total Number of Copies
     */
    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;

    /**
     * Book Status
     */
    private Book.BookStatus status;

    /**
     * Book Language
     */
    @NotBlank(message = "Language is required")
    @Size(min = 2, max = 50, message = "Language must be between 2 and 50 characters")
    private String language;

    /**
     * Book Cover Image URL
     */
    @Size(max = 500, message = "Cover image URL cannot exceed 500 characters")
    private String coverImageUrl;

    /**
     * Creation Timestamp
     */
    private LocalDate createdAt;

    /**
     * Last Update Timestamp
     */
    private LocalDate updatedAt;

    /**
     * Availability Percentage (calculated field)
     */
    private Double availabilityPercentage;

    /**
     * Constructor for creating BookDTO from Book entity
     * 
     * @param book the Book entity to convert
     */
    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.publisher = book.getPublisher();
        this.publicationDate = book.getPublicationDate();
        this.category = book.getCategory();
        this.pages = book.getPages();
        this.price = book.getPrice();
        this.description = book.getDescription();
        this.copiesAvailable = book.getCopiesAvailable();
        this.totalCopies = book.getTotalCopies();
        this.status = book.getStatus();
        this.language = book.getLanguage();
        this.coverImageUrl = book.getCoverImageUrl();
        this.createdAt = book.getCreatedAt() != null ? book.getCreatedAt().toLocalDate() : null;
        this.updatedAt = book.getUpdatedAt() != null ? book.getUpdatedAt().toLocalDate() : null;
        this.availabilityPercentage = book.getAvailabilityPercentage();
    }

    /**
     * Convert BookDTO to Book entity
     * 
     * @return Book entity
     */
    public Book toEntity() {
        Book book = new Book();
        book.setId(this.id);
        book.setTitle(this.title);
        book.setAuthor(this.author);
        book.setIsbn(this.isbn);
        book.setPublisher(this.publisher);
        book.setPublicationDate(this.publicationDate);
        book.setCategory(this.category);
        book.setPages(this.pages);
        book.setPrice(this.price);
        book.setDescription(this.description);
        book.setCopiesAvailable(this.copiesAvailable);
        book.setTotalCopies(this.totalCopies);
        book.setStatus(this.status != null ? this.status : Book.BookStatus.AVAILABLE);
        book.setLanguage(this.language);
        book.setCoverImageUrl(this.coverImageUrl);
        return book;
    }
}

