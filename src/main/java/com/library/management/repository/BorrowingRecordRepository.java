package com.library.management.repository;

import com.library.management.model.BorrowingRecord;
import com.library.management.model.User;
import com.library.management.model.Book;
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
 * Borrowing Record Repository Interface
 * 
 * This interface extends JpaRepository to provide CRUD operations for BorrowingRecord entities.
 * JpaRepository provides methods like save(), findById(), findAll(), delete(), etc.
 * 
 * @Repository: Marks this interface as a repository component
 * JpaRepository<BorrowingRecord, Long>: BorrowingRecord is the entity type, Long is the ID type
 */
@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    /**
     * Find borrowing records by user
     * 
     * @param user the user to search for
     * @return list of borrowing records for the specified user
     */
    List<BorrowingRecord> findByUser(User user);

    /**
     * Find borrowing records by book
     * 
     * @param book the book to search for
     * @return list of borrowing records for the specified book
     */
    List<BorrowingRecord> findByBook(Book book);

    /**
     * Find borrowing records by status
     * 
     * @param status the borrowing status to search for
     * @return list of borrowing records with the specified status
     */
    List<BorrowingRecord> findByStatus(BorrowingRecord.BorrowingStatus status);

    /**
     * Find active borrowing records (status = BORROWED)
     * 
     * @return list of active borrowing records
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.status = 'BORROWED'")
    List<BorrowingRecord> findActiveBorrowingRecords();

    /**
     * Find overdue borrowing records
     * 
     * @return list of overdue borrowing records
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.status = 'BORROWED' AND br.expectedReturnDate < CURRENT_DATE")
    List<BorrowingRecord> findOverdueBorrowingRecords();

    /**
     * Find borrowing records by user ID
     * 
     * @param userId the user ID to search for
     * @return list of borrowing records for the specified user
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.user.id = :userId")
    List<BorrowingRecord> findByUserId(@Param("userId") Long userId);

    /**
     * Find borrowing records by book ID
     * 
     * @param bookId the book ID to search for
     * @return list of borrowing records for the specified book
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.book.id = :bookId")
    List<BorrowingRecord> findByBookId(@Param("bookId") Long bookId);

    /**
     * Find active borrowing records by user ID
     * 
     * @param userId the user ID to search for
     * @return list of active borrowing records for the specified user
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.user.id = :userId AND br.status = 'BORROWED'")
    List<BorrowingRecord> findActiveBorrowingRecordsByUserId(@Param("userId") Long userId);

    /**
     * Find borrowing records by date range
     * 
     * @param startDate start date of the range
     * @param endDate end date of the range
     * @return list of borrowing records within the date range
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.borrowedDate BETWEEN :startDate AND :endDate")
    List<BorrowingRecord> findByBorrowedDateRange(@Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);

    /**
     * Find borrowing records by return date range
     * 
     * @param startDate start date of the range
     * @param endDate end date of the range
     * @return list of borrowing records returned within the date range
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.actualReturnDate BETWEEN :startDate AND :endDate")
    List<BorrowingRecord> findByReturnDateRange(@Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);

    /**
     * Find borrowing records with outstanding fines
     * 
     * @return list of borrowing records with outstanding fines
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.fineAmount > 0 AND br.finePaidDate IS NULL")
    List<BorrowingRecord> findBorrowingRecordsWithOutstandingFines();

    /**
     * Find borrowing records by user and status
     * 
     * @param user the user to search for
     * @param status the status to search for
     * @return list of borrowing records for the specified user and status
     */
    List<BorrowingRecord> findByUserAndStatus(User user, BorrowingRecord.BorrowingStatus status);

    /**
     * Find borrowing records by book and status
     * 
     * @param book the book to search for
     * @param status the status to search for
     * @return list of borrowing records for the specified book and status
     */
    List<BorrowingRecord> findByBookAndStatus(Book book, BorrowingRecord.BorrowingStatus status);

    /**
     * Find borrowing records by multiple criteria with pagination
     * 
     * @param userId user ID to search for (can be null)
     * @param bookId book ID to search for (can be null)
     * @param status status to search for (can be null)
     * @param pageable pagination information
     * @return page of borrowing records matching the criteria
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE " +
           "(:userId IS NULL OR br.user.id = :userId) AND " +
           "(:bookId IS NULL OR br.book.id = :bookId) AND " +
           "(:status IS NULL OR br.status = :status)")
    Page<BorrowingRecord> findByCriteria(@Param("userId") Long userId,
                                        @Param("bookId") Long bookId,
                                        @Param("status") BorrowingRecord.BorrowingStatus status,
                                        Pageable pageable);

    /**
     * Find borrowing records by user ID with pagination
     * 
     * @param userId the user ID to search for
     * @param pageable pagination information
     * @return page of borrowing records for the specified user
     */
    Page<BorrowingRecord> findByUserIdOrderByBorrowedDateDesc(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find borrowing records by book ID with pagination
     * 
     * @param bookId the book ID to search for
     * @param pageable pagination information
     * @return page of borrowing records for the specified book
     */
    Page<BorrowingRecord> findByBookIdOrderByBorrowedDateDesc(@Param("bookId") Long bookId, Pageable pageable);

    /**
     * Count borrowing records by status
     * 
     * @param status the status to count
     * @return number of borrowing records with the specified status
     */
    long countByStatus(BorrowingRecord.BorrowingStatus status);

    /**
     * Count active borrowing records by user
     * 
     * @param user the user to count for
     * @return number of active borrowing records for the specified user
     */
    @Query("SELECT COUNT(br) FROM BorrowingRecord br WHERE br.user = :user AND br.status = 'BORROWED'")
    long countActiveBorrowingRecordsByUser(@Param("user") User user);

    /**
     * Find borrowing records that are due soon (within specified days)
     * 
     * @param days number of days to look ahead
     * @return list of borrowing records due soon
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.status = 'BORROWED' AND " +
           "br.expectedReturnDate BETWEEN CURRENT_DATE AND :dueDate")
    List<BorrowingRecord> findBorrowingRecordsDueSoon(@Param("dueDate") LocalDate dueDate);

    /**
     * Find borrowing records by renewal count
     * 
     * @param renewalCount the renewal count to search for
     * @return list of borrowing records with the specified renewal count
     */
    List<BorrowingRecord> findByRenewalCount(Integer renewalCount);

    /**
     * Find borrowing records that can be renewed
     * 
     * @return list of borrowing records that can be renewed
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.status = 'BORROWED' AND " +
           "br.renewalCount < br.maxRenewals AND br.expectedReturnDate >= CURRENT_DATE")
    List<BorrowingRecord> findBorrowingRecordsThatCanBeRenewed();

    /**
     * Find borrowing records by user ID and book ID
     * 
     * @param userId the user ID to search for
     * @param bookId the book ID to search for
     * @return list of borrowing records for the specified user and book
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.user.id = :userId AND br.book.id = :bookId")
    List<BorrowingRecord> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    /**
     * Find the most recent borrowing record for a user and book
     * 
     * @param userId the user ID to search for
     * @param bookId the book ID to search for
     * @return optional borrowing record for the specified user and book
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.user.id = :userId AND br.book.id = :bookId " +
           "ORDER BY br.borrowedDate DESC")
    Optional<BorrowingRecord> findMostRecentBorrowingRecordByUserIdAndBookId(@Param("userId") Long userId, 
                                                                            @Param("bookId") Long bookId);

    /**
     * Find borrowing records with the highest fines
     * 
     * @param pageable pagination information
     * @return page of borrowing records with highest fines
     */
    Page<BorrowingRecord> findByOrderByFineAmountDesc(Pageable pageable);

    /**
     * Find borrowing records by fine amount range
     * 
     * @param minFine minimum fine amount
     * @param maxFine maximum fine amount
     * @return list of borrowing records within the fine range
     */
    @Query("SELECT br FROM BorrowingRecord br WHERE br.fineAmount BETWEEN :minFine AND :maxFine")
    List<BorrowingRecord> findByFineAmountRange(@Param("minFine") Double minFine, 
                                               @Param("maxFine") Double maxFine);
}

