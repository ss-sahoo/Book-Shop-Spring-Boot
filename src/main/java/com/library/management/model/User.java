package com.library.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * User Entity
 * 
 * Represents a user in our library system (students, librarians, etc.)
 * 
 * @Entity: Marks this class as a JPA entity
 * @Table: Specifies the database table name
 * @Data: Lombok annotation for getters, setters, toString, etc.
 * @EqualsAndHashCode: Lombok annotation for equals and hashCode methods
 * @NoArgsConstructor: Lombok annotation for no-args constructor
 * @AllArgsConstructor: Lombok annotation for all-args constructor
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    /**
     * User's First Name
     * @NotBlank: Ensures the field is not null, empty, or whitespace
     * @Size: Specifies minimum and maximum length
     * @Column: Specifies column properties
     */
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * User's Last Name
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * User's Email Address
     * @Email: Validates email format
     * @Column: Specifies column as unique
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * User's Phone Number
     * @Pattern: Validates phone number format
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /**
     * User's Date of Birth
     * @Past: Ensures the date is in the past
     */
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * User's Address
     */
    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 500, message = "Address must be between 10 and 500 characters")
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    /**
     * User's Username
     * @Column: Specifies column as unique
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * User's Password (will be hashed)
     * @Size: Ensures minimum password length
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User's Role
     * @Enumerated: Maps enum to database
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.STUDENT;

    /**
     * User's Status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * User's Student ID (if applicable)
     */
    @Size(max = 50, message = "Student ID cannot exceed 50 characters")
    @Column(name = "student_id")
    private String studentId;

    /**
     * User's Department (if applicable)
     */
    @Size(max = 100, message = "Department cannot exceed 100 characters")
    @Column(name = "department")
    private String department;

    /**
     * User's Profile Image URL
     */
    @Size(max = 500, message = "Profile image URL cannot exceed 500 characters")
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    /**
     * User's Borrowing Records
     * @OneToMany: One user can have many borrowing records
     * @JoinColumn: Specifies the foreign key column
     * @OrderBy: Orders the results by borrowing date
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("borrowedDate DESC")
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();

    /**
     * User Role Enumeration
     */
    public enum UserRole {
        ADMIN("Administrator"),
        LIBRARIAN("Librarian"),
        STUDENT("Student"),
        FACULTY("Faculty"),
        GUEST("Guest");

        private final String displayName;

        UserRole(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * User Status Enumeration
     */
    public enum UserStatus {
        ACTIVE("Active"),
        INACTIVE("Inactive"),
        SUSPENDED("Suspended"),
        BANNED("Banned");

        private final String displayName;

        UserStatus(String displayName) {
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
     * Get the user's full name
     * @return concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Check if the user is active
     * @return true if user is active, false otherwise
     */
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    /**
     * Check if the user is a librarian or admin
     * @return true if user has librarian privileges, false otherwise
     */
    public boolean isLibrarian() {
        return role == UserRole.LIBRARIAN || role == UserRole.ADMIN;
    }

    /**
     * Check if the user is an admin
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    /**
     * Get the user's age
     * @return age in years
     */
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    /**
     * Check if the user can borrow books
     * @return true if user can borrow, false otherwise
     */
    public boolean canBorrowBooks() {
        return isActive() && (role == UserRole.STUDENT || role == UserRole.FACULTY);
    }

    /**
     * Get the maximum number of books this user can borrow
     * @return maximum books allowed
     */
    public int getMaxBooksAllowed() {
        switch (role) {
            case STUDENT:
                return 5;
            case FACULTY:
                return 10;
            case LIBRARIAN:
            case ADMIN:
                return 15;
            default:
                return 0;
        }
    }

    /**
     * Get the borrowing period in days for this user
     * @return borrowing period in days
     */
    public int getBorrowingPeriodDays() {
        switch (role) {
            case STUDENT:
                return 14; // 2 weeks
            case FACULTY:
                return 30; // 1 month
            case LIBRARIAN:
            case ADMIN:
                return 60; // 2 months
            default:
                return 0;
        }
    }
}

