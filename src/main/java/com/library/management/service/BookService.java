package com.library.management.service;

import com.library.management.dto.BookDTO;
import com.library.management.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Book Service Interface
 * 
 * This interface defines the business logic operations for Book entities.
 * The service layer contains the business logic and acts as a bridge between
 * the controller (presentation layer) and the repository (data access layer).
 * 
 * Key responsibilities:
 * - Business logic validation
 * - Data transformation
 * - Transaction management
 * - Error handling
 * - Business rules enforcement
 */
public interface BookService {

    /**
     * Create a new book
     * 
     * @param bookDTO the book data to create
     * @return the created book DTO
     * @throws IllegalArgumentException if book data is invalid
     * @throws org.springframework.dao.DataIntegrityViolationException if ISBN already exists
     */
    BookDTO createBook(BookDTO bookDTO);

    /**
     * Get a book by ID
     * 
     * @param id the book ID
     * @return the book DTO if found
     * @throws com.library.management.exception.BookNotFoundException if book not found
     */
    BookDTO getBookById(Long id);

    /**
     * Get all books with pagination
     * 
     * @param pageable pagination information
     * @return page of book DTOs
     */
    Page<BookDTO> getAllBooks(Pageable pageable);

    /**
     * Update a book
     * 
     * @param id the book ID
     * @param bookDTO the updated book data
     * @return the updated book DTO
     * @throws com.library.management.exception.BookNotFoundException if book not found
     * @throws IllegalArgumentException if book data is invalid
     */
    BookDTO updateBook(Long id, BookDTO bookDTO);

    /**
     * Delete a book (soft delete)
     * 
     * @param id the book ID
     * @throws com.library.management.exception.BookNotFoundException if book not found
     */
    void deleteBook(Long id);

    /**
     * Search books by title
     * 
     * @param title the title to search for
     * @return list of matching book DTOs
     */
    List<BookDTO> searchBooksByTitle(String title);

    /**
     * Search books by author
     * 
     * @param author the author to search for
     * @return list of matching book DTOs
     */
    List<BookDTO> searchBooksByAuthor(String author);

    /**
     * Search books by category
     * 
     * @param category the category to search for
     * @return list of matching book DTOs
     */
    List<BookDTO> searchBooksByCategory(String category);

    /**
     * Search books by ISBN
     * 
     * @param isbn the ISBN to search for
     * @return the book DTO if found
     * @throws com.library.management.exception.BookNotFoundException if book not found
     */
    BookDTO getBookByIsbn(String isbn);

    /**
     * Get available books (status = AVAILABLE and copies > 0)
     * 
     * @return list of available book DTOs
     */
    List<BookDTO> getAvailableBooks();

    /**
     * Get books with low availability
     * 
     * @param minCopies minimum number of copies
     * @return list of books with low availability
     */
    List<BookDTO> getBooksWithLowAvailability(Integer minCopies);

    /**
     * Search books by multiple criteria with pagination
     * 
     * @param title title to search for (can be null)
     * @param author author to search for (can be null)
     * @param category category to search for (can be null)
     * @param pageable pagination information
     * @return page of matching book DTOs
     */
    Page<BookDTO> searchBooksByCriteria(String title, String author, String category, Pageable pageable);

    /**
     * Get most popular books
     * 
     * @param pageable pagination information
     * @return page of most popular book DTOs
     */
    Page<BookDTO> getMostPopularBooks(Pageable pageable);

    /**
     * Get recently added books
     * 
     * @param pageable pagination information
     * @return page of recently added book DTOs
     */
    Page<BookDTO> getRecentlyAddedBooks(Pageable pageable);

    /**
     * Get books by language
     * 
     * @param language the language to search for
     * @return list of books in the specified language
     */
    List<BookDTO> getBooksByLanguage(String language);

    /**
     * Get books by price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within the price range
     */
    List<BookDTO> getBooksByPriceRange(Double minPrice, Double maxPrice);

    /**
     * Get books by publication date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return list of books published within the date range
     */
    List<BookDTO> getBooksByPublicationDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);

    /**
     * Get books by status
     * 
     * @param status the book status
     * @return list of books with the specified status
     */
    List<BookDTO> getBooksByStatus(Book.BookStatus status);

    /**
     * Get books that need restocking
     * 
     * @return list of books that need restocking
     */
    List<BookDTO> getBooksNeedingRestock();

    /**
     * Get overdue books
     * 
     * @return list of books that are currently overdue
     */
    List<BookDTO> getOverdueBooks();

    /**
     * Full-text search books
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of books matching the search term
     */
    Page<BookDTO> searchBooks(String searchTerm, Pageable pageable);

    /**
     * Get book statistics
     * 
     * @return book statistics DTO
     */
    BookStatisticsDTO getBookStatistics();

    /**
     * Add copies to a book
     * 
     * @param bookId the book ID
     * @param additionalCopies number of additional copies to add
     * @return the updated book DTO
     * @throws com.library.management.exception.BookNotFoundException if book not found
     * @throws IllegalArgumentException if additional copies is negative
     */
    BookDTO addCopies(Long bookId, Integer additionalCopies);

    /**
     * Remove copies from a book
     * 
     * @param bookId the book ID
     * @param copiesToRemove number of copies to remove
     * @return the updated book DTO
     * @throws com.library.management.exception.BookNotFoundException if book not found
     * @throws IllegalArgumentException if copies to remove is invalid
     */
    BookDTO removeCopies(Long bookId, Integer copiesToRemove);

    /**
     * Book Statistics DTO
     */
    class BookStatisticsDTO {
        private long totalBooks;
        private long availableBooks;
        private long borrowedBooks;
        private long overdueBooks;
        private long booksNeedingRestock;
        private double averageAvailabilityPercentage;

        // Constructors, getters, and setters
        public BookStatisticsDTO() {}

        public BookStatisticsDTO(long totalBooks, long availableBooks, long borrowedBooks, 
                               long overdueBooks, long booksNeedingRestock, double averageAvailabilityPercentage) {
            this.totalBooks = totalBooks;
            this.availableBooks = availableBooks;
            this.borrowedBooks = borrowedBooks;
            this.overdueBooks = overdueBooks;
            this.booksNeedingRestock = booksNeedingRestock;
            this.averageAvailabilityPercentage = averageAvailabilityPercentage;
        }

        // Getters and setters
        public long getTotalBooks() { return totalBooks; }
        public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }

        public long getAvailableBooks() { return availableBooks; }
        public void setAvailableBooks(long availableBooks) { this.availableBooks = availableBooks; }

        public long getBorrowedBooks() { return borrowedBooks; }
        public void setBorrowedBooks(long borrowedBooks) { this.borrowedBooks = borrowedBooks; }

        public long getOverdueBooks() { return overdueBooks; }
        public void setOverdueBooks(long overdueBooks) { this.overdueBooks = overdueBooks; }

        public long getBooksNeedingRestock() { return booksNeedingRestock; }
        public void setBooksNeedingRestock(long booksNeedingRestock) { this.booksNeedingRestock = booksNeedingRestock; }

        public double getAverageAvailabilityPercentage() { return averageAvailabilityPercentage; }
        public void setAverageAvailabilityPercentage(double averageAvailabilityPercentage) { 
            this.averageAvailabilityPercentage = averageAvailabilityPercentage; 
        }
    }
}

