package com.library.management.repository;

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
 * Book Repository Interface
 * 
 * This interface extends JpaRepository to provide CRUD operations for Book entities.
 * JpaRepository provides methods like save(), findById(), findAll(), delete(), etc.
 * 
 * @Repository: Marks this interface as a repository component
 * JpaRepository<Book, Long>: Book is the entity type, Long is the ID type
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find books by title (case-insensitive)
     * Spring Data JPA automatically generates the implementation based on method name
     * 
     * @param title the title to search for
     * @return list of books with matching title
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find books by author (case-insensitive)
     * 
     * @param author the author to search for
     * @return list of books by the specified author
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Find books by category (case-insensitive)
     * 
     * @param category the category to search for
     * @return list of books in the specified category
     */
    List<Book> findByCategoryContainingIgnoreCase(String category);

    /**
     * Find books by ISBN
     * 
     * @param isbn the ISBN to search for
     * @return optional book with the specified ISBN
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find books by publication date range
     * 
     * @param startDate start date of the range
     * @param endDate end date of the range
     * @return list of books published within the date range
     */
    List<Book> findByPublicationDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find books by status
     * 
     * @param status the book status to search for
     * @return list of books with the specified status
     */
    List<Book> findByStatus(Book.BookStatus status);

    /**
     * Find available books (status = AVAILABLE and copies > 0)
     * 
     * @return list of available books
     */
    @Query("SELECT b FROM Book b WHERE b.status = 'AVAILABLE' AND b.copiesAvailable > 0")
    List<Book> findAvailableBooks();

    /**
     * Find books with low availability (less than specified copies)
     * 
     * @param minCopies minimum number of copies
     * @return list of books with low availability
     */
    @Query("SELECT b FROM Book b WHERE b.copiesAvailable < :minCopies")
    List<Book> findBooksWithLowAvailability(@Param("minCopies") Integer minCopies);

    /**
     * Find books by multiple criteria with pagination
     * 
     * @param title title to search for (can be null)
     * @param author author to search for (can be null)
     * @param category category to search for (can be null)
     * @param pageable pagination information
     * @return page of books matching the criteria
     */
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:category IS NULL OR LOWER(b.category) LIKE LOWER(CONCAT('%', :category, '%')))")
    Page<Book> findByCriteria(@Param("title") String title, 
                             @Param("author") String author, 
                             @Param("category") String category, 
                             Pageable pageable);

    /**
     * Find most popular books (most borrowed)
     * 
     * @param limit maximum number of results
     * @return list of most popular books
     */
    @Query("SELECT b FROM Book b ORDER BY SIZE(b.borrowingRecords) DESC")
    List<Book> findMostPopularBooks(Pageable pageable);

    /**
     * Find recently added books
     * 
     * @param pageable pagination information
     * @return page of recently added books
     */
    Page<Book> findByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find books by language
     * 
     * @param language the language to search for
     * @return list of books in the specified language
     */
    List<Book> findByLanguage(String language);

    /**
     * Find books by price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within the price range
     */
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Book> findByPriceRange(@Param("minPrice") Double minPrice, 
                               @Param("maxPrice") Double maxPrice);

    /**
     * Count books by category
     * 
     * @param category the category to count
     * @return number of books in the category
     */
    long countByCategory(String category);

    /**
     * Count books by status
     * 
     * @param status the status to count
     * @return number of books with the specified status
     */
    long countByStatus(Book.BookStatus status);

    /**
     * Find books that need restocking (copies available = 0)
     * 
     * @return list of books that need restocking
     */
    @Query("SELECT b FROM Book b WHERE b.copiesAvailable = 0 AND b.status = 'BORROWED'")
    List<Book> findBooksNeedingRestock();

    /**
     * Find books by publisher
     * 
     * @param publisher the publisher to search for
     * @return list of books by the specified publisher
     */
    List<Book> findByPublisherContainingIgnoreCase(String publisher);

    /**
     * Find books with overdue status
     * 
     * @return list of books that are currently overdue
     */
    @Query("SELECT DISTINCT b FROM Book b JOIN b.borrowingRecords br WHERE " +
           "br.status = 'BORROWED' AND br.expectedReturnDate < CURRENT_DATE")
    List<Book> findOverdueBooks();

    /**
     * Search books by multiple fields (full-text search)
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of books matching the search term
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Book> searchBooks(@Param("searchTerm") String searchTerm, Pageable pageable);
}

