package com.library.management.dto;

import com.library.management.model.BorrowingRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Borrowing Record Data Transfer Object (DTO)
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
public class BorrowingRecordDTO {

    /**
     * Borrowing Record ID
     */
    private Long id;

    /**
     * User ID who borrowed the book
     * @NotNull: Ensures the field is not null
     */
    @NotNull(message = "User ID is required")
    private Long userId;

    /**
     * Book ID that was borrowed
     * @NotNull: Ensures the field is not null
     */
    @NotNull(message = "Book ID is required")
    private Long bookId;

    /**
     * User's full name (for display purposes)
     */
    private String userFullName;

    /**
     * Book title (for display purposes)
     */
    private String bookTitle;

    /**
     * Date when the book was borrowed
     */
    private LocalDate borrowedDate;

    /**
     * Expected return date
     */
    private LocalDate expectedReturnDate;

    /**
     * Actual return date (null if not returned yet)
     */
    private LocalDate actualReturnDate;

    /**
     * Status of the borrowing record
     */
    private BorrowingRecord.BorrowingStatus status;

    /**
     * Fine amount (if any)
     */
    private Double fineAmount;

    /**
     * Fine paid date
     */
    private LocalDate finePaidDate;

    /**
     * Notes about the borrowing record
     */
    private String notes;

    /**
     * Renewal count (how many times the book was renewed)
     */
    private Integer renewalCount;

    /**
     * Maximum renewals allowed
     */
    private Integer maxRenewals;

    /**
     * Creation Timestamp
     */
    private LocalDate createdAt;

    /**
     * Last Update Timestamp
     */
    private LocalDate updatedAt;

    /**
     * Days overdue (calculated field)
     */
    private Long daysOverdue;

    /**
     * Borrowing duration in days (calculated field)
     */
    private Long borrowingDurationDays;

    /**
     * Whether the book is overdue (calculated field)
     */
    private Boolean isOverdue;

    /**
     * Whether the book can be renewed (calculated field)
     */
    private Boolean canBeRenewed;

    /**
     * Whether the fine has been paid (calculated field)
     */
    private Boolean isFinePaid;

    /**
     * Constructor for creating BorrowingRecordDTO from BorrowingRecord entity
     * 
     * @param borrowingRecord the BorrowingRecord entity to convert
     */
    public BorrowingRecordDTO(BorrowingRecord borrowingRecord) {
        this.id = borrowingRecord.getId();
        this.userId = borrowingRecord.getUser().getId();
        this.bookId = borrowingRecord.getBook().getId();
        this.userFullName = borrowingRecord.getUser().getFullName();
        this.bookTitle = borrowingRecord.getBook().getTitle();
        this.borrowedDate = borrowingRecord.getBorrowedDate();
        this.expectedReturnDate = borrowingRecord.getExpectedReturnDate();
        this.actualReturnDate = borrowingRecord.getActualReturnDate();
        this.status = borrowingRecord.getStatus();
        this.fineAmount = borrowingRecord.getFineAmount();
        this.finePaidDate = borrowingRecord.getFinePaidDate();
        this.notes = borrowingRecord.getNotes();
        this.renewalCount = borrowingRecord.getRenewalCount();
        this.maxRenewals = borrowingRecord.getMaxRenewals();
        this.createdAt = borrowingRecord.getCreatedAt() != null ? borrowingRecord.getCreatedAt().toLocalDate() : null;
        this.updatedAt = borrowingRecord.getUpdatedAt() != null ? borrowingRecord.getUpdatedAt().toLocalDate() : null;
        this.daysOverdue = borrowingRecord.getDaysOverdue();
        this.borrowingDurationDays = borrowingRecord.getBorrowingDurationDays();
        this.isOverdue = borrowingRecord.isOverdue();
        this.canBeRenewed = borrowingRecord.canBeRenewed();
        this.isFinePaid = borrowingRecord.isFinePaid();
    }

    /**
     * Convert BorrowingRecordDTO to BorrowingRecord entity
     * Note: This method requires User and Book entities to be set separately
     * 
     * @return BorrowingRecord entity
     */
    public BorrowingRecord toEntity() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(this.id);
        borrowingRecord.setBorrowedDate(this.borrowedDate);
        borrowingRecord.setExpectedReturnDate(this.expectedReturnDate);
        borrowingRecord.setActualReturnDate(this.actualReturnDate);
        borrowingRecord.setStatus(this.status != null ? this.status : BorrowingRecord.BorrowingStatus.BORROWED);
        borrowingRecord.setFineAmount(this.fineAmount);
        borrowingRecord.setFinePaidDate(this.finePaidDate);
        borrowingRecord.setNotes(this.notes);
        borrowingRecord.setRenewalCount(this.renewalCount);
        borrowingRecord.setMaxRenewals(this.maxRenewals);
        return borrowingRecord;
    }

    /**
     * DTO for creating a new borrowing record
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDTO {
        @NotNull(message = "User ID is required")
        private Long userId;

        @NotNull(message = "Book ID is required")
        private Long bookId;

        @NotNull(message = "Expected return date is required")
        private LocalDate expectedReturnDate;

        private String notes;
    }

    /**
     * DTO for renewing a borrowing record
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RenewDTO {
        @NotNull(message = "Borrowing record ID is required")
        private Long borrowingRecordId;

        @NotNull(message = "Additional days is required")
        private Integer additionalDays;
    }

    /**
     * DTO for returning a book
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnDTO {
        @NotNull(message = "Borrowing record ID is required")
        private Long borrowingRecordId;

        private String notes;
    }

    /**
     * DTO for paying a fine
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayFineDTO {
        @NotNull(message = "Borrowing record ID is required")
        private Long borrowingRecordId;

        @NotNull(message = "Fine amount is required")
        private Double fineAmount;
    }
}

