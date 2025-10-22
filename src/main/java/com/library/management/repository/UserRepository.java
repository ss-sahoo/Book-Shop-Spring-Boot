package com.library.management.repository;

import com.library.management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface
 * 
 * This interface extends JpaRepository to provide CRUD operations for User entities.
 * JpaRepository provides methods like save(), findById(), findAll(), delete(), etc.
 * 
 * @Repository: Marks this interface as a repository component
 * JpaRepository<User, Long>: User is the entity type, Long is the ID type
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     * 
     * @param email the email to search for
     * @return optional user with the specified email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username
     * 
     * @param username the username to search for
     * @return optional user with the specified username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find users by role
     * 
     * @param role the user role to search for
     * @return list of users with the specified role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Find users by status
     * 
     * @param status the user status to search for
     * @return list of users with the specified status
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * Find users by first name (case-insensitive)
     * 
     * @param firstName the first name to search for
     * @return list of users with matching first name
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Find users by last name (case-insensitive)
     * 
     * @param lastName the last name to search for
     * @return list of users with matching last name
     */
    List<User> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Find users by department
     * 
     * @param department the department to search for
     * @return list of users in the specified department
     */
    List<User> findByDepartment(String department);

    /**
     * Find users by student ID
     * 
     * @param studentId the student ID to search for
     * @return optional user with the specified student ID
     */
    Optional<User> findByStudentId(String studentId);

    /**
     * Find active users
     * 
     * @return list of active users
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();

    /**
     * Find users who can borrow books
     * 
     * @return list of users who can borrow books
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND (u.role = 'STUDENT' OR u.role = 'FACULTY')")
    List<User> findUsersWhoCanBorrow();

    /**
     * Find users with overdue books
     * 
     * @return list of users who have overdue books
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.borrowingRecords br WHERE " +
           "br.status = 'BORROWED' AND br.expectedReturnDate < CURRENT_DATE")
    List<User> findUsersWithOverdueBooks();

    /**
     * Find users by age range
     * 
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of users within the age range
     */
    @Query("SELECT u FROM User u WHERE YEAR(CURRENT_DATE) - YEAR(u.dateOfBirth) BETWEEN :minAge AND :maxAge")
    List<User> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    /**
     * Find users by multiple criteria with pagination
     * 
     * @param firstName first name to search for (can be null)
     * @param lastName last name to search for (can be null)
     * @param role role to search for (can be null)
     * @param status status to search for (can be null)
     * @param pageable pagination information
     * @return page of users matching the criteria
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:status IS NULL OR u.status = :status)")
    Page<User> findByCriteria(@Param("firstName") String firstName,
                             @Param("lastName") String lastName,
                             @Param("role") User.UserRole role,
                             @Param("status") User.UserStatus status,
                             Pageable pageable);

    /**
     * Find recently registered users
     * 
     * @param pageable pagination information
     * @return page of recently registered users
     */
    Page<User> findByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Count users by role
     * 
     * @param role the role to count
     * @return number of users with the specified role
     */
    long countByRole(User.UserRole role);

    /**
     * Count users by status
     * 
     * @param status the status to count
     * @return number of users with the specified status
     */
    long countByStatus(User.UserStatus status);

    /**
     * Find users by phone number
     * 
     * @param phoneNumber the phone number to search for
     * @return optional user with the specified phone number
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Find users who registered in a specific date range
     * 
     * @param startDate start date of the range
     * @param endDate end date of the range
     * @return list of users registered within the date range
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByRegistrationDateRange(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);

    /**
     * Find users with the most borrowing activity
     * 
     * @param pageable pagination information
     * @return page of users with most borrowing activity
     */
    @Query("SELECT u FROM User u ORDER BY SIZE(u.borrowingRecords) DESC")
    Page<User> findMostActiveUsers(Pageable pageable);

    /**
     * Search users by multiple fields (full-text search)
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of users matching the search term
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.studentId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find users who have never borrowed a book
     * 
     * @return list of users who have never borrowed a book
     */
    @Query("SELECT u FROM User u WHERE SIZE(u.borrowingRecords) = 0")
    List<User> findUsersWhoNeverBorrowed();

    /**
     * Find users with outstanding fines
     * 
     * @return list of users with outstanding fines
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.borrowingRecords br WHERE " +
           "br.fineAmount > 0 AND br.finePaidDate IS NULL")
    List<User> findUsersWithOutstandingFines();

    /**
     * Check if email exists
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if student ID exists
     * 
     * @param studentId the student ID to check
     * @return true if student ID exists, false otherwise
     */
    boolean existsByStudentId(String studentId);
}

