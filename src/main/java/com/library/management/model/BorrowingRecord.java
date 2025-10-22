package com.library.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Borrowing Record Entity
 * 
 * Represents a record of a book being borrowed by a user.
 * This entity tracks the borrowing transaction details.
 * 
 * @Entity: Marks this class as a JPA entity
 * @Table: Specifies the database table name
 * @Data: Lombok annotation for getters, setters, toString, etc.
 * @EqualsAndHashCode: Lombok annotation for equals and hashCode methods
 * @NoArgsConstructor: Lombok annotation for no-args constructor
 * @AllArgsConstructor: Lombok annotation for all-args constructor
 */
@Entity
@Table(name = "borrowing_records")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecord extends BaseEntity {

    /**
     * The User who borrowed the book
     * @ManyToOne: Many borrowing records can belong to one user
     * @JoinColumn: Specifies the foreign key column name
     * @NotNull: Ensures the field is not null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    /**
     * The Book that was borrowed
     * @ManyToOne: Many borrowing records can belong to one book
     * @JoinColumn: Specifies the foreign key column name
     * @NotNull: Ensures the field is not null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Book is required")
    private Book book;

    /**
     * Date when the book was borrowed
     * @Column: Specifies column properties
     */
    @Column(name = "borrowed_date", nullable = false)
    private LocalDate borrowedDate;

    /**
     * Expected return date
     * @Column: Specifies column properties
     */
    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    /**
     * Actual return date (null if not returned yet)
     * @Column: Specifies column properties
     */
    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    /**
     * Status of the borrowing record
     * @Enumerated: Maps enum to database
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BorrowingStatus status = BorrowingStatus.BORROWED;

    /**
     * Fine amount (if any)
     * @Column: Specifies column properties with precision and scale
     */
    @Column(name = "fine_amount")
    private Double fineAmount = 0.0;

    /**
     * Fine paid date
     * @Column: Specifies column properties
     */
    @Column(name = "fine_paid_date")
    private LocalDate finePaidDate;

    /**
     * Notes about the borrowing record
     * @Column: Specifies column properties
     */
    @Column(name = "notes", length = 1000)
    private String notes;

    /**
     * Renewal count (how many times the book was renewed)
     * @Column: Specifies column properties
     */
    @Column(name = "renewal_count")
    private Integer renewalCount = 0;

    /**
     * Maximum renewals allowed
     * @Column: Specifies column properties
     */
    @Column(name = "max_renewals")
    private Integer maxRenewals = 2;

    /**
     * Borrowing Status Enumeration
     */
    public enum BorrowingStatus {
        BORROWED("Borrowed"),
        RETURNED("Returned"),
        OVERDUE("Overdue"),
        LOST("Lost"),
        DAMAGED("Damaged");

        private final String displayName;

        BorrowingStatus(String displayName) {
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
     * Check if the book is overdue
     * @return true if the book is overdue, false otherwise
     */
    public boolean isOverdue() {
        if (actualReturnDate != null) {
            return false; // Book has been returned
        }
        return LocalDate.now().isAfter(expectedReturnDate);
    }

    /**
     * Calculate the number of days overdue
     * @return number of days overdue (0 if not overdue)
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - expectedReturnDate.toEpochDay();
    }

    /**
     * Calculate the fine amount based on overdue days
     * @param dailyFineRate rate per day for overdue books
     * @return calculated fine amount
     */
    public double calculateFine(double dailyFineRate) {
        if (!isOverdue()) {
            return 0.0;
        }
        return getDaysOverdue() * dailyFineRate;
    }

    /**
     * Check if the book can be renewed
     * @return true if the book can be renewed, false otherwise
     */
    public boolean canBeRenewed() {
        return status == BorrowingStatus.BORROWED && 
               renewalCount < maxRenewals && 
               !isOverdue();
    }

    /**
     * Renew the book borrowing
     * @param additionalDays number of days to extend the borrowing period
     * @return true if renewal was successful, false otherwise
     */
    public boolean renew(int additionalDays) {
        if (canBeRenewed()) {
            expectedReturnDate = expectedReturnDate.plusDays(additionalDays);
            renewalCount++;
            return true;
        }
        return false;
    }

    /**
     * Return the book
     * @return true if return was successful, false otherwise
     */
    public boolean returnBook() {
        if (status == BorrowingStatus.BORROWED) {
            actualReturnDate = LocalDate.now();
            status = BorrowingStatus.RETURNED;
            
            // Calculate fine if overdue
            if (isOverdue()) {
                fineAmount = calculateFine(1.0); // $1 per day
            }
            
            return true;
        }
        return false;
    }

    /**
     * Get the borrowing duration in days
     * @return number of days the book was borrowed
     */
    public long getBorrowingDurationDays() {
        LocalDate endDate = actualReturnDate != null ? actualReturnDate : LocalDate.now();
        return endDate.toEpochDay() - borrowedDate.toEpochDay();
    }

    /**
     * Check if the fine has been paid
     * @return true if fine has been paid, false otherwise
     */
    public boolean isFinePaid() {
        return finePaidDate != null;
    }

    /**
     * Pay the fine
     * @return true if payment was successful, false otherwise
     */
    public boolean payFine() {
        if (fineAmount > 0 && !isFinePaid()) {
            finePaidDate = LocalDate.now();
            return true;
        }
        return false;
    }
}
