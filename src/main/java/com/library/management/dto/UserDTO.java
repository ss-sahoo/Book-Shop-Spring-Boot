package com.library.management.dto;

import com.library.management.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * User Data Transfer Object (DTO)
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
public class UserDTO {

    /**
     * User ID (for updates, not required for creation)
     */
    private Long id;

    /**
     * User's First Name
     * @NotBlank: Ensures the field is not null, empty, or whitespace
     * @Size: Specifies minimum and maximum length
     */
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;

    /**
     * User's Last Name
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;

    /**
     * User's Email Address
     * @Email: Validates email format
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    /**
     * User's Phone Number
     * @Pattern: Validates phone number format
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    /**
     * User's Date of Birth
     * @Past: Ensures the date is in the past
     */
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * User's Address
     */
    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 500, message = "Address must be between 10 and 500 characters")
    private String address;

    /**
     * User's Username
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    /**
     * User's Password (will be hashed)
     * @Size: Ensures minimum password length
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /**
     * User's Role
     */
    private User.UserRole role;

    /**
     * User's Status
     */
    private User.UserStatus status;

    /**
     * User's Student ID (if applicable)
     */
    @Size(max = 50, message = "Student ID cannot exceed 50 characters")
    private String studentId;

    /**
     * User's Department (if applicable)
     */
    @Size(max = 100, message = "Department cannot exceed 100 characters")
    private String department;

    /**
     * User's Profile Image URL
     */
    @Size(max = 500, message = "Profile image URL cannot exceed 500 characters")
    private String profileImageUrl;

    /**
     * Creation Timestamp
     */
    private LocalDate createdAt;

    /**
     * Last Update Timestamp
     */
    private LocalDate updatedAt;

    /**
     * User's Age (calculated field)
     */
    private Integer age;

    /**
     * User's Full Name (calculated field)
     */
    private String fullName;

    /**
     * Maximum Books Allowed (calculated field)
     */
    private Integer maxBooksAllowed;

    /**
     * Borrowing Period in Days (calculated field)
     */
    private Integer borrowingPeriodDays;

    /**
     * Constructor for creating UserDTO from User entity
     * 
     * @param user the User entity to convert
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.dateOfBirth = user.getDateOfBirth();
        this.address = user.getAddress();
        this.username = user.getUsername();
        // Note: Password is not included in DTO for security reasons
        this.role = user.getRole();
        this.status = user.getStatus();
        this.studentId = user.getStudentId();
        this.department = user.getDepartment();
        this.profileImageUrl = user.getProfileImageUrl();
        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : null;
        this.updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().toLocalDate() : null;
        this.age = user.getAge();
        this.fullName = user.getFullName();
        this.maxBooksAllowed = user.getMaxBooksAllowed();
        this.borrowingPeriodDays = user.getBorrowingPeriodDays();
    }

    /**
     * Convert UserDTO to User entity
     * 
     * @return User entity
     */
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPhoneNumber(this.phoneNumber);
        user.setDateOfBirth(this.dateOfBirth);
        user.setAddress(this.address);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setRole(this.role != null ? this.role : User.UserRole.STUDENT);
        user.setStatus(this.status != null ? this.status : User.UserStatus.ACTIVE);
        user.setStudentId(this.studentId);
        user.setDepartment(this.department);
        user.setProfileImageUrl(this.profileImageUrl);
        return user;
    }

    /**
     * DTO for user registration (without ID and timestamps)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationDTO {
        @NotBlank(message = "First name is required")
        @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        private String email;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
        private String phoneNumber;

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        @NotBlank(message = "Address is required")
        @Size(min = 10, max = 500, message = "Address must be between 10 and 500 characters")
        private String address;

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
        private String username;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String password;

        @Size(max = 50, message = "Student ID cannot exceed 50 characters")
        private String studentId;

        @Size(max = 100, message = "Department cannot exceed 100 characters")
        private String department;
    }

    /**
     * DTO for user login
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        @NotBlank(message = "Username or email is required")
        private String usernameOrEmail;

        @NotBlank(message = "Password is required")
        private String password;
    }

    /**
     * DTO for user profile update (without password)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileUpdateDTO {
        @NotBlank(message = "First name is required")
        @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
        private String lastName;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
        private String phoneNumber;

        @NotBlank(message = "Address is required")
        @Size(min = 10, max = 500, message = "Address must be between 10 and 500 characters")
        private String address;

        @Size(max = 100, message = "Department cannot exceed 100 characters")
        private String department;

        @Size(max = 500, message = "Profile image URL cannot exceed 500 characters")
        private String profileImageUrl;
    }
}

