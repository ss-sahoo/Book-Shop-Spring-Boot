package com.library.management.controller;

import com.library.management.dto.BookDTO;
import com.library.management.service.BookService;
import com.library.management.model.Book;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    /**
     * Create a new book
     * 
     * @param bookDTO the book data to create
     * @return the created book DTO
     */
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        log.info("Creating new book: {}", bookDTO.getTitle());
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Get a book by ID
     * 
     * @param id the book ID
     * @return the book DTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        log.info("Fetching book with ID: {}", id);
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Get all books with pagination
     * 
     * @param pageable pagination information
     * @return page of book DTOs
     */
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        log.info("Fetching all books with pagination: {}", pageable);
        Page<BookDTO> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Update a book
     * 
     * @param id the book ID
     * @param bookDTO the updated book data
     * @return the updated book DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, 
                                            @Valid @RequestBody BookDTO bookDTO) {
        log.info("Updating book with ID: {}", id);
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Delete a book
     * 
     * @param id the book ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Deleting book with ID: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search books by title
     * 
     * @param title the title to search for
     * @return list of matching book DTOs
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String title) {
        log.info("Searching books by title: {}", title);
        List<BookDTO> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by author
     * 
     * @param author the author to search for
     * @return list of matching book DTOs
     */
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO>> searchBooksByAuthor(@RequestParam String author) {
        log.info("Searching books by author: {}", author);
        List<BookDTO> books = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by category
     * 
     * @param category the category to search for
     * @return list of matching book DTOs
     */
    @GetMapping("/search/category")
    public ResponseEntity<List<BookDTO>> searchBooksByCategory(@RequestParam String category) {
        log.info("Searching books by category: {}", category);
        List<BookDTO> books = bookService.searchBooksByCategory(category);
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by ISBN
     * 
     * @param isbn the ISBN to search for
     * @return the book DTO if found
     */
    @GetMapping("/search/isbn")
    public ResponseEntity<BookDTO> getBookByIsbn(@RequestParam String isbn) {
        log.info("Searching book by ISBN: {}", isbn);
        BookDTO book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    /**
     * Get available books
     * 
     * @return list of available book DTOs
     */
    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        log.info("Fetching available books");
        List<BookDTO> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Get books with low availability
     * 
     * @param minCopies minimum number of copies
     * @return list of books with low availability
     */
    @GetMapping("/low-availability")
    public ResponseEntity<List<BookDTO>> getBooksWithLowAvailability(
            @RequestParam(defaultValue = "5") Integer minCopies) {
        log.info("Fetching books with low availability (less than {} copies)", minCopies);
        List<BookDTO> books = bookService.getBooksWithLowAvailability(minCopies);
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by multiple criteria with pagination
     * 
     * @param title title to search for (optional)
     * @param author author to search for (optional)
     * @param category category to search for (optional)
     * @param pageable pagination information
     * @return page of matching book DTOs
     */
    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchBooksByCriteria(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        log.info("Searching books by criteria - title: {}, author: {}, category: {}", title, author, category);
        Page<BookDTO> books = bookService.searchBooksByCriteria(title, author, category, pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Get most popular books
     * 
     * @param pageable pagination information
     * @return page of most popular book DTOs
     */
    @GetMapping("/popular")
    public ResponseEntity<Page<BookDTO>> getMostPopularBooks(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        log.info("Fetching most popular books");
        Page<BookDTO> books = bookService.getMostPopularBooks(pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Get recently added books
     * 
     * @param pageable pagination information
     * @return page of recently added book DTOs
     */
    @GetMapping("/recent")
    public ResponseEntity<Page<BookDTO>> getRecentlyAddedBooks(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        log.info("Fetching recently added books");
        Page<BookDTO> books = bookService.getRecentlyAddedBooks(pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books by language
     * 
     * @param language the language to search for
     * @return list of books in the specified language
     */
    @GetMapping("/language/{language}")
    public ResponseEntity<List<BookDTO>> getBooksByLanguage(@PathVariable String language) {
        log.info("Fetching books by language: {}", language);
        List<BookDTO> books = bookService.getBooksByLanguage(language);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books by price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of books within the price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<BookDTO>> getBooksByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        log.info("Fetching books by price range: {} - {}", minPrice, maxPrice);
        List<BookDTO> books = bookService.getBooksByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books by publication date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return list of books published within the date range
     */
    @GetMapping("/publication-date-range")
    public ResponseEntity<List<BookDTO>> getBooksByPublicationDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Fetching books by publication date range: {} - {}", startDate, endDate);
        List<BookDTO> books = bookService.getBooksByPublicationDateRange(startDate, endDate);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books by status
     * 
     * @param status the book status
     * @return list of books with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookDTO>> getBooksByStatus(@PathVariable Book.BookStatus status) {
        log.info("Fetching books by status: {}", status);
        List<BookDTO> books = bookService.getBooksByStatus(status);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books that need restocking
     * 
     * @return list of books that need restocking
     */
    @GetMapping("/needing-restock")
    public ResponseEntity<List<BookDTO>> getBooksNeedingRestock() {
        log.info("Fetching books that need restocking");
        List<BookDTO> books = bookService.getBooksNeedingRestock();
        return ResponseEntity.ok(books);
    }

    /**
     * Get overdue books
     * 
     * @return list of books that are currently overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<BookDTO>> getOverdueBooks() {
        log.info("Fetching overdue books");
        List<BookDTO> books = bookService.getOverdueBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Full-text search books
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of books matching the search term
     */
    @GetMapping("/search/full-text")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam String searchTerm,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        log.info("Full-text searching books with term: {}", searchTerm);
        Page<BookDTO> books = bookService.searchBooks(searchTerm, pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Get book statistics
     * 
     * @return book statistics DTO
     */
    @GetMapping("/statistics")
    public ResponseEntity<BookService.BookStatisticsDTO> getBookStatistics() {
        log.info("Fetching book statistics");
        BookService.BookStatisticsDTO statistics = bookService.getBookStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Add copies to a book
     * 
     * @param id the book ID
     * @param additionalCopies number of additional copies to add
     * @return the updated book DTO
     */
    @PostMapping("/{id}/add-copies")
    public ResponseEntity<BookDTO> addCopies(@PathVariable Long id, 
                                           @RequestParam Integer additionalCopies) {
        log.info("Adding {} copies to book with ID: {}", additionalCopies, id);
        BookDTO updatedBook = bookService.addCopies(id, additionalCopies);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Remove copies from a book
     * 
     * @param id the book ID
     * @param copiesToRemove number of copies to remove
     * @return the updated book DTO
     */
    @PostMapping("/{id}/remove-copies")
    public ResponseEntity<BookDTO> removeCopies(@PathVariable Long id, 
                                              @RequestParam Integer copiesToRemove) {
        log.info("Removing {} copies from book with ID: {}", copiesToRemove, id);
        BookDTO updatedBook = bookService.removeCopies(id, copiesToRemove);
        return ResponseEntity.ok(updatedBook);
    }
}

