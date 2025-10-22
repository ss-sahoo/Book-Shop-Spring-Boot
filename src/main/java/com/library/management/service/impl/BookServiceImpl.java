package com.library.management.service.impl;

import com.library.management.dto.BookDTO;
import com.library.management.exception.BookNotFoundException;
import com.library.management.model.Book;
import com.library.management.repository.BookRepository;
import com.library.management.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Book Service Implementation
 * 
 * This class implements the BookService interface and contains the business logic
 * for Book operations. It acts as a bridge between the controller and repository layers.
 * 
 * @Service: Marks this class as a service component
 * @RequiredArgsConstructor: Lombok annotation that generates constructor for final fields
 * @Slf4j: Lombok annotation for logging
 * @Transactional: Ensures that methods are executed within a database transaction
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    /**
     * Create a new book
     * 
     * @param bookDTO the book data to create
     * @return the created book DTO
     */
    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        log.info("Creating new book with title: {}", bookDTO.getTitle());
        
        // Validate business rules
        validateBookData(bookDTO);
        
        // Check if ISBN already exists
        if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }
        
        // Convert DTO to entity
        Book book = bookDTO.toEntity();
        
        // Set default values
        if (book.getCopiesAvailable() == null) {
            book.setCopiesAvailable(book.getTotalCopies());
        }
        if (book.getStatus() == null) {
            book.setStatus(Book.BookStatus.AVAILABLE);
        }
        if (book.getLanguage() == null || book.getLanguage().isEmpty()) {
            book.setLanguage("English");
        }
        
        // Save the book
        Book savedBook = bookRepository.save(book);
        log.info("Successfully created book with ID: {}", savedBook.getId());
        
        return new BookDTO(savedBook);
    }

    /**
     * Get a book by ID
     * 
     * @param id the book ID
     * @return the book DTO if found
     */
    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);
        
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        
        return new BookDTO(book);
    }

    /**
     * Get all books with pagination
     * 
     * @param pageable pagination information
     * @return page of book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        log.info("Fetching all books with pagination: {}", pageable);
        
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(BookDTO::new);
    }

    /**
     * Update a book
     * 
     * @param id the book ID
     * @param bookDTO the updated book data
     * @return the updated book DTO
     */
    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        log.info("Updating book with ID: {}", id);
        
        // Check if book exists
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        
        // Validate business rules
        validateBookData(bookDTO);
        
        // Check if ISBN is being changed and if it already exists
        if (!existingBook.getIsbn().equals(bookDTO.getIsbn())) {
            if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
                throw new IllegalArgumentException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
            }
        }
        
        // Update the book
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPublisher(bookDTO.getPublisher());
        existingBook.setPublicationDate(bookDTO.getPublicationDate());
        existingBook.setCategory(bookDTO.getCategory());
        existingBook.setPages(bookDTO.getPages());
        existingBook.setPrice(bookDTO.getPrice());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setTotalCopies(bookDTO.getTotalCopies());
        existingBook.setLanguage(bookDTO.getLanguage());
        existingBook.setCoverImageUrl(bookDTO.getCoverImageUrl());
        
        // Update status if needed
        if (bookDTO.getStatus() != null) {
            existingBook.setStatus(bookDTO.getStatus());
        }
        
        // Save the updated book
        Book updatedBook = bookRepository.save(existingBook);
        log.info("Successfully updated book with ID: {}", updatedBook.getId());
        
        return new BookDTO(updatedBook);
    }

    /**
     * Delete a book (soft delete)
     * 
     * @param id the book ID
     */
    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        
        // Check if book has active borrowing records
        if (!book.getBorrowingRecords().isEmpty()) {
            boolean hasActiveBorrowings = book.getBorrowingRecords().stream()
                    .anyMatch(record -> record.getStatus() == com.library.management.model.BorrowingRecord.BorrowingStatus.BORROWED);
            
            if (hasActiveBorrowings) {
                throw new IllegalStateException("Cannot delete book with active borrowing records");
            }
        }
        
        // Soft delete
        book.setDeleted(true);
        bookRepository.save(book);
        
        log.info("Successfully deleted book with ID: {}", id);
    }

    /**
     * Search books by title
     * 
     * @param title the title to search for
     * @return list of matching book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> searchBooksByTitle(String title) {
        log.info("Searching books by title: {}", title);
        
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Search books by author
     * 
     * @param author the author to search for
     * @return list of matching book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> searchBooksByAuthor(String author) {
        log.info("Searching books by author: {}", author);
        
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Search books by category
     * 
     * @param category the category to search for
     * @return list of matching book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> searchBooksByCategory(String category) {
        log.info("Searching books by category: {}", category);
        
        List<Book> books = bookRepository.findByCategoryContainingIgnoreCase(category);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Search books by ISBN
     * 
     * @param isbn the ISBN to search for
     * @return the book DTO if found
     */
    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookByIsbn(String isbn) {
        log.info("Searching book by ISBN: {}", isbn);
        
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
        
        return new BookDTO(book);
    }

    /**
     * Get available books
     * 
     * @return list of available book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAvailableBooks() {
        log.info("Fetching available books");
        
        List<Book> books = bookRepository.findAvailableBooks();
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Get books with low availability
     * 
     * @param minCopies minimum number of copies
     * @return list of books with low availability
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksWithLowAvailability(Integer minCopies) {
        log.info("Fetching books with low availability (less than {} copies)", minCopies);
        
        List<Book> books = bookRepository.findBooksWithLowAvailability(minCopies);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Search books by multiple criteria with pagination
     * 
     * @param title title to search for (can be null)
     * @param author author to search for (can be null)
     * @param category category to search for (can be null)
     * @param pageable pagination information
     * @return page of matching book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooksByCriteria(String title, String author, String category, Pageable pageable) {
        log.info("Searching books by criteria - title: {}, author: {}, category: {}", title, author, category);
        
        Page<Book> books = bookRepository.findByCriteria(title, author, category, pageable);
        return books.map(BookDTO::new);
    }

    /**
     * Get most popular books
     * 
     * @param pageable pagination information
     * @return page of most popular book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> getMostPopularBooks(Pageable pageable) {
        log.info("Fetching most popular books");
        
        List<Book> books = bookRepository.findMostPopularBooks(pageable);
        return new org.springframework.data.domain.PageImpl<>(books, pageable, books.size()).map(BookDTO::new);
    }

    /**
     * Get recently added books
     * 
     * @param pageable pagination information
     * @return page of recently added book DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> getRecentlyAddedBooks(Pageable pageable) {
        log.info("Fetching recently added books");
        
        Page<Book> books = bookRepository.findByOrderByCreatedAtDesc(pageable);
        return books.map(BookDTO::new);
    }

    /**
     * Get books by language
     * 
     * @param language the language to search for
     * @return list of books in the specified language
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByLanguage(String language) {
        log.info("Fetching books by language: {}", language);
        
        List<Book> books = bookRepository.findByLanguage(language);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Get books by price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within the price range
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Fetching books by price range: {} - {}", minPrice, maxPrice);
        
        List<Book> books = bookRepository.findByPriceRange(minPrice, maxPrice);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Get books by publication date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return list of books published within the date range
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByPublicationDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching books by publication date range: {} - {}", startDate, endDate);
        
        List<Book> books = bookRepository.findByPublicationDateBetween(startDate, endDate);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Get books by status
     * 
     * @param status the book status
     * @return list of books with the specified status
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByStatus(Book.BookStatus status) {
        log.info("Fetching books by status: {}", status);
        
        List<Book> books = bookRepository.findByStatus(status);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Get books that need restocking
     * 
     * @return list of books that need restocking
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksNeedingRestock() {
        log.info("Fetching books that need restocking");
        
        List<Book> books = bookRepository.findBooksNeedingRestock();
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Get overdue books
     * 
     * @return list of books that are currently overdue
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getOverdueBooks() {
        log.info("Fetching overdue books");
        
        List<Book> books = bookRepository.findOverdueBooks();
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    /**
     * Full-text search books
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of books matching the search term
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooks(String searchTerm, Pageable pageable) {
        log.info("Full-text searching books with term: {}", searchTerm);
        
        Page<Book> books = bookRepository.searchBooks(searchTerm, pageable);
        return books.map(BookDTO::new);
    }

    /**
     * Get book statistics
     * 
     * @return book statistics DTO
     */
    @Override
    @Transactional(readOnly = true)
    public BookStatisticsDTO getBookStatistics() {
        log.info("Fetching book statistics");
        
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.countByStatus(Book.BookStatus.AVAILABLE);
        long borrowedBooks = bookRepository.countByStatus(Book.BookStatus.BORROWED);
        long overdueBooks = bookRepository.findOverdueBooks().size();
        long booksNeedingRestock = bookRepository.findBooksNeedingRestock().size();
        
        // Calculate average availability percentage
        List<Book> allBooks = bookRepository.findAll();
        double averageAvailabilityPercentage = allBooks.stream()
                .mapToDouble(Book::getAvailabilityPercentage)
                .average()
                .orElse(0.0);
        
        return new BookStatisticsDTO(
                totalBooks,
                availableBooks,
                borrowedBooks,
                overdueBooks,
                booksNeedingRestock,
                averageAvailabilityPercentage
        );
    }

    /**
     * Add copies to a book
     * 
     * @param bookId the book ID
     * @param additionalCopies number of additional copies to add
     * @return the updated book DTO
     */
    @Override
    public BookDTO addCopies(Long bookId, Integer additionalCopies) {
        log.info("Adding {} copies to book with ID: {}", additionalCopies, bookId);
        
        if (additionalCopies <= 0) {
            throw new IllegalArgumentException("Additional copies must be positive");
        }
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        
        book.setTotalCopies(book.getTotalCopies() + additionalCopies);
        book.setCopiesAvailable(book.getCopiesAvailable() + additionalCopies);
        
        // Update status if needed
        if (book.getStatus() == Book.BookStatus.BORROWED && book.getCopiesAvailable() > 0) {
            book.setStatus(Book.BookStatus.AVAILABLE);
        }
        
        Book updatedBook = bookRepository.save(book);
        log.info("Successfully added {} copies to book with ID: {}", additionalCopies, bookId);
        
        return new BookDTO(updatedBook);
    }

    /**
     * Remove copies from a book
     * 
     * @param bookId the book ID
     * @param copiesToRemove number of copies to remove
     * @return the updated book DTO
     */
    @Override
    public BookDTO removeCopies(Long bookId, Integer copiesToRemove) {
        log.info("Removing {} copies from book with ID: {}", copiesToRemove, bookId);
        
        if (copiesToRemove <= 0) {
            throw new IllegalArgumentException("Copies to remove must be positive");
        }
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        
        if (copiesToRemove > book.getTotalCopies()) {
            throw new IllegalArgumentException("Cannot remove more copies than total copies");
        }
        
        if (copiesToRemove > book.getCopiesAvailable()) {
            throw new IllegalArgumentException("Cannot remove more copies than available copies");
        }
        
        book.setTotalCopies(book.getTotalCopies() - copiesToRemove);
        book.setCopiesAvailable(book.getCopiesAvailable() - copiesToRemove);
        
        // Update status if needed
        if (book.getCopiesAvailable() == 0) {
            book.setStatus(Book.BookStatus.BORROWED);
        }
        
        Book updatedBook = bookRepository.save(book);
        log.info("Successfully removed {} copies from book with ID: {}", copiesToRemove, bookId);
        
        return new BookDTO(updatedBook);
    }

    /**
     * Validate book data
     * 
     * @param bookDTO the book data to validate
     */
    private void validateBookData(BookDTO bookDTO) {
        if (bookDTO.getTotalCopies() != null && bookDTO.getCopiesAvailable() != null) {
            if (bookDTO.getCopiesAvailable() > bookDTO.getTotalCopies()) {
                throw new IllegalArgumentException("Available copies cannot exceed total copies");
            }
        }
        
        if (bookDTO.getPublicationDate() != null && bookDTO.getPublicationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date cannot be in the future");
        }
    }
}
