# 🚀 Complete Java Spring Boot Learning Guide

## 📚 Table of Contents
1. [Java Basics for Beginners](#java-basics-for-beginners)
2. [Spring Boot Introduction](#spring-boot-introduction)
3. [Project Setup](#project-setup)
4. [Building Our Library Management System](#building-our-library-management-system)
5. [Advanced Concepts](#advanced-concepts)
6. [Testing](#testing)
7. [Deployment](#deployment)
8. [Interview Preparation](#interview-preparation)

---

## 🎯 Learning Objectives
By the end of this guide, you will:
- ✅ Understand Java fundamentals
- ✅ Master Spring Boot concepts
- ✅ Build a complete REST API
- ✅ Work with databases using JPA
- ✅ Implement security
- ✅ Write comprehensive tests
- ✅ Deploy applications
- ✅ Be ready for real-world projects like DSpace

---

## 🛠️ Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)
- Git

---

## 📖 Java Basics for Beginners

### What is Java?
Java is a high-level, object-oriented programming language that runs on the Java Virtual Machine (JVM). It's platform-independent, meaning "write once, run anywhere."

### Key Java Concepts

#### 1. Classes and Objects
```java
// A class is a blueprint for creating objects
public class Book {
    // Fields (attributes)
    private String title;
    private String author;
    private int pages;
    
    // Constructor - special method to create objects
    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }
    
    // Methods (behaviors)
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    // toString method for string representation
    @Override
    public String toString() {
        return "Book{title='" + title + "', author='" + author + "', pages=" + pages + "}";
    }
}
```

#### 2. Inheritance
```java
// Base class
public class Person {
    protected String name;
    protected int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void introduce() {
        System.out.println("Hi, I'm " + name);
    }
}

// Derived class
public class Student extends Person {
    private String studentId;
    
    public Student(String name, int age, String studentId) {
        super(name, age); // Call parent constructor
        this.studentId = studentId;
    }
    
    @Override
    public void introduce() {
        System.out.println("Hi, I'm " + name + ", student ID: " + studentId);
    }
}
```

#### 3. Interfaces
```java
// Interface defines a contract
public interface Borrowable {
    void borrow();
    void returnItem();
    boolean isAvailable();
}

// Class implements interface
public class Book implements Borrowable {
    private boolean borrowed = false;
    
    @Override
    public void borrow() {
        if (!borrowed) {
            borrowed = true;
            System.out.println("Book borrowed successfully");
        } else {
            System.out.println("Book is already borrowed");
        }
    }
    
    @Override
    public void returnItem() {
        borrowed = false;
        System.out.println("Book returned successfully");
    }
    
    @Override
    public boolean isAvailable() {
        return !borrowed;
    }
}
```

#### 4. Collections
```java
import java.util.*;

public class Library {
    private List<Book> books = new ArrayList<>();
    private Map<String, Book> bookMap = new HashMap<>();
    private Set<String> categories = new HashSet<>();
    
    public void addBook(Book book) {
        books.add(book);
        bookMap.put(book.getTitle(), book);
        categories.add(book.getCategory());
    }
    
    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                   .filter(book -> book.getAuthor().equals(author))
                   .collect(Collectors.toList());
    }
}
```

---

## 🌱 Spring Boot Introduction

### What is Spring Boot?
Spring Boot is a framework that simplifies Spring application development by providing:
- **Auto-configuration**: Automatically configures your application
- **Starter dependencies**: Pre-configured dependency sets
- **Embedded servers**: No need for external servers
- **Production-ready features**: Monitoring, metrics, health checks

### Key Spring Boot Concepts

#### 1. Dependency Injection (DI)
```java
// Service interface
public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book saveBook(Book book);
}

// Service implementation
@Service // Spring annotation for service layer
public class BookServiceImpl implements BookService {
    
    @Autowired // Spring injects the dependency
    private BookRepository bookRepository;
    
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                           .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }
    
    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
}
```

#### 2. Annotations Explained
- `@SpringBootApplication`: Main application class
- `@RestController`: REST API controller
- `@Service`: Business logic layer
- `@Repository`: Data access layer
- `@Autowired`: Dependency injection
- `@Component`: Generic Spring component
- `@Configuration`: Configuration class
- `@Bean`: Method that returns a Spring bean

---

## 🏗️ Project Setup

Let's create our Library Management System step by step!

### Step 1: Create Maven Project Structure
```
library-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── library/
│   │   │           └── management/
│   │   │               ├── LibraryManagementApplication.java
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── repository/
│   │   │               ├── model/
│   │   │               ├── dto/
│   │   │               └── config/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
└── pom.xml
```

---

## 🎯 Building Our Library Management System

Our project will include:
- **Books Management**: CRUD operations for books
- **Users Management**: Student and librarian management
- **Borrowing System**: Track book loans
- **Authentication**: Secure access
- **REST API**: Complete API endpoints
- **Database**: MySQL integration
- **Testing**: Unit and integration tests

Let's start building! 🚀

---

## 🏗️ Project Structure Explained

Our Library Management System follows the **Layered Architecture** pattern:

```
src/main/java/com/library/management/
├── LibraryManagementApplication.java    # Main application class
├── controller/                          # REST API endpoints
│   ├── BookController.java             # Book-related API endpoints
│   ├── UserController.java             # User-related API endpoints
│   └── BorrowingController.java        # Borrowing-related API endpoints
├── service/                            # Business logic layer
│   ├── BookService.java               # Book service interface
│   ├── UserService.java               # User service interface
│   ├── BorrowingService.java          # Borrowing service interface
│   └── impl/                          # Service implementations
│       ├── BookServiceImpl.java       # Book service implementation
│       ├── UserServiceImpl.java       # User service implementation
│       └── BorrowingServiceImpl.java  # Borrowing service implementation
├── repository/                         # Data access layer
│   ├── BookRepository.java            # Book data access
│   ├── UserRepository.java            # User data access
│   └── BorrowingRecordRepository.java # Borrowing record data access
├── model/                             # Entity classes (Database tables)
│   ├── BaseEntity.java               # Base entity with common fields
│   ├── Book.java                     # Book entity
│   ├── User.java                     # User entity
│   └── BorrowingRecord.java          # Borrowing record entity
├── dto/                              # Data Transfer Objects
│   ├── BookDTO.java                  # Book data transfer object
│   ├── UserDTO.java                  # User data transfer object
│   └── BorrowingRecordDTO.java       # Borrowing record data transfer object
├── exception/                        # Custom exceptions
│   ├── BookNotFoundException.java    # Book not found exception
│   ├── UserNotFoundException.java    # User not found exception
│   └── BorrowingRecordNotFoundException.java # Borrowing record not found exception
└── config/                           # Configuration classes
    ├── WebConfig.java                # Web configuration
    ├── JpaConfig.java                # JPA configuration
    └── SecurityConfig.java           # Security configuration
```

---

## 📚 Key Spring Boot Concepts Explained

### 1. **Dependency Injection (DI)**
```java
@Service
public class BookServiceImpl implements BookService {
    
    @Autowired  // Spring automatically injects this dependency
    private BookRepository bookRepository;
    
    // Spring creates and manages the BookRepository instance
    // You don't need to create it manually
}
```

**Why DI is important:**
- **Loose Coupling**: Classes don't create their own dependencies
- **Testability**: Easy to mock dependencies for testing
- **Flexibility**: Can easily swap implementations
- **Maintainability**: Changes in one class don't affect others

### 2. **Annotations Explained**

#### **@SpringBootApplication**
```java
@SpringBootApplication  // Combines 3 annotations:
public class LibraryManagementApplication {
    // @Configuration: Marks this as a configuration class
    // @EnableAutoConfiguration: Enables Spring Boot auto-configuration
    // @ComponentScan: Scans for components in the package
}
```

#### **@RestController**
```java
@RestController  // Combines @Controller + @ResponseBody
@RequestMapping("/books")  // Maps all endpoints to /books
public class BookController {
    
    @GetMapping("/{id}")  // Maps GET /books/{id}
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        // @PathVariable: Extracts {id} from URL
        // ResponseEntity: Wraps response with HTTP status
    }
}
```

#### **@Service**
```java
@Service  // Marks this as a service component
@Transactional  // All methods run in a database transaction
public class BookServiceImpl implements BookService {
    // Business logic goes here
}
```

#### **@Repository**
```java
@Repository  // Marks this as a data access component
public interface BookRepository extends JpaRepository<Book, Long> {
    // JpaRepository provides CRUD operations automatically
    // You can add custom query methods
}
```

#### **@Entity**
```java
@Entity  // Marks this as a JPA entity (database table)
@Table(name = "books")  // Specifies table name
public class Book extends BaseEntity {
    
    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(name = "title", nullable = false)  // Column properties
    @NotBlank(message = "Title is required")  // Validation
    private String title;
}
```

### 3. **JPA/Hibernate Concepts**

#### **Entity Relationships**
```java
// One-to-Many relationship
@Entity
public class Book {
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowingRecord> borrowingRecords;
}

@Entity
public class BorrowingRecord {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
```

#### **Repository Methods**
```java
public interface BookRepository extends JpaRepository<Book, Long> {
    // Spring Data JPA automatically generates implementations
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    // Generates: SELECT * FROM books WHERE LOWER(title) LIKE LOWER('%?%')
    
    @Query("SELECT b FROM Book b WHERE b.status = 'AVAILABLE'")
    List<Book> findAvailableBooks();
    // Custom JPQL query
}
```

### 4. **REST API Design**

#### **HTTP Methods**
- **GET**: Retrieve data (read)
- **POST**: Create new data
- **PUT**: Update existing data
- **DELETE**: Remove data

#### **Response Status Codes**
- **200 OK**: Success
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid input
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

#### **API Endpoints Example**
```java
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    
    @GetMapping                    // GET /api/v1/books
    public Page<BookDTO> getAllBooks(Pageable pageable) { }
    
    @GetMapping("/{id}")          // GET /api/v1/books/1
    public BookDTO getBookById(@PathVariable Long id) { }
    
    @PostMapping                  // POST /api/v1/books
    public BookDTO createBook(@RequestBody BookDTO bookDTO) { }
    
    @PutMapping("/{id}")          // PUT /api/v1/books/1
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) { }
    
    @DeleteMapping("/{id}")       // DELETE /api/v1/books/1
    public void deleteBook(@PathVariable Long id) { }
}
```

---

## 🚀 How to Run the Project

### **Step 1: Prerequisites**
Make sure you have installed:
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (optional, H2 is used by default)

### **Step 2: Clone and Setup**
```bash
# Navigate to the project directory
cd "/home/diracai/Music/java Spring boot"

# The project is already set up! Let's run it
```

### **Step 3: Run the Application**
```bash
# Run with Maven
mvn spring-boot:run

# Or run the JAR file
mvn clean package
java -jar target/library-management-system-1.0.0.jar
```

### **Step 4: Test the API**
The application will start on `http://localhost:8080/api/v1`

**Test endpoints:**
```bash
# Get all books
curl http://localhost:8080/api/v1/books

# Create a new book
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java Spring Boot Guide",
    "author": "John Doe",
    "isbn": "978-0-123456-78-9",
    "publisher": "Tech Publisher",
    "publicationDate": "2023-01-01",
    "category": "Programming",
    "pages": 500,
    "price": 49.99,
    "description": "A comprehensive guide to Spring Boot",
    "totalCopies": 10,
    "language": "English"
  }'

# Get a specific book
curl http://localhost:8080/api/v1/books/1
```

---

## 🧪 Testing the Application

### **Unit Tests**
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BookServiceTest
```

### **Integration Tests**
```bash
# Run integration tests
mvn test -Dtest=BookControllerIntegrationTest
```

### **Test Coverage**
```bash
# Generate test coverage report
mvn jacoco:report
```

---

## 📊 Database Schema

### **Books Table**
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    category VARCHAR(100) NOT NULL,
    pages INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    copies_available INT NOT NULL DEFAULT 1,
    total_copies INT NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    language VARCHAR(50) NOT NULL DEFAULT 'English',
    cover_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);
```

### **Users Table**
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    date_of_birth DATE NOT NULL,
    address VARCHAR(500) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    student_id VARCHAR(50),
    department VARCHAR(100),
    profile_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);
```

### **Borrowing Records Table**
```sql
CREATE TABLE borrowing_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrowed_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    actual_return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'BORROWED',
    fine_amount DECIMAL(10,2) DEFAULT 0.0,
    fine_paid_date DATE,
    notes TEXT,
    renewal_count INT DEFAULT 0,
    max_renewals INT DEFAULT 2,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

---

## 🔧 Configuration Explained

### **Application Properties**
```properties
# Server Configuration
server.port=8080                    # Port where the application runs
server.servlet.context-path=/api/v1 # Base path for all endpoints

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=library_user
spring.datasource.password=library_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update  # Automatically update database schema
spring.jpa.show-sql=true              # Show SQL queries in logs
spring.jpa.properties.hibernate.format_sql=true  # Format SQL queries

# Logging Configuration
logging.level.com.library.management=DEBUG  # Debug level for our package
logging.level.org.springframework.security=DEBUG  # Debug level for security
```

### **Profile-based Configuration**
- **application.properties**: Default configuration
- **application-dev.properties**: Development configuration (H2 database)
- **application-prod.properties**: Production configuration (MySQL database)
- **application-test.properties**: Test configuration

---

## 🎯 Business Logic Examples

### **Book Availability Check**
```java
public boolean isAvailable() {
    return status == BookStatus.AVAILABLE && copiesAvailable > 0;
}
```

### **Borrowing Logic**
```java
public boolean borrowCopy() {
    if (copiesAvailable > 0) {
        copiesAvailable--;
        if (copiesAvailable == 0) {
            status = BookStatus.BORROWED;
        }
        return true;
    }
    return false;
}
```

### **Fine Calculation**
```java
public double calculateFine(double dailyFineRate) {
    if (!isOverdue()) {
        return 0.0;
    }
    return getDaysOverdue() * dailyFineRate;
}
```

---

## 🔍 API Documentation

### **Book Endpoints**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/books` | Get all books | - | Page<BookDTO> |
| GET | `/books/{id}` | Get book by ID | - | BookDTO |
| POST | `/books` | Create new book | BookDTO | BookDTO |
| PUT | `/books/{id}` | Update book | BookDTO | BookDTO |
| DELETE | `/books/{id}` | Delete book | - | 204 No Content |
| GET | `/books/search/title?title=java` | Search by title | - | List<BookDTO> |
| GET | `/books/search/author?author=john` | Search by author | - | List<BookDTO> |
| GET | `/books/available` | Get available books | - | List<BookDTO> |
| GET | `/books/statistics` | Get book statistics | - | BookStatisticsDTO |

### **Request/Response Examples**

#### **Create Book Request**
```json
POST /api/v1/books
Content-Type: application/json

{
  "title": "Spring Boot in Action",
  "author": "Craig Walls",
  "isbn": "978-1-61729-254-5",
  "publisher": "Manning Publications",
  "publicationDate": "2021-03-15",
  "category": "Programming",
  "pages": 400,
  "price": 49.99,
  "description": "A comprehensive guide to Spring Boot development",
  "totalCopies": 5,
  "language": "English"
}
```

#### **Create Book Response**
```json
{
  "id": 1,
  "title": "Spring Boot in Action",
  "author": "Craig Walls",
  "isbn": "978-1-61729-254-5",
  "publisher": "Manning Publications",
  "publicationDate": "2021-03-15",
  "category": "Programming",
  "pages": 400,
  "price": 49.99,
  "description": "A comprehensive guide to Spring Boot development",
  "copiesAvailable": 5,
  "totalCopies": 5,
  "status": "AVAILABLE",
  "language": "English",
  "createdAt": "2024-01-15",
  "updatedAt": "2024-01-15",
  "availabilityPercentage": 100.0
}
```

---

## 🎓 Learning Exercises

### **Beginner Level**
1. **Add a new field to Book entity** (e.g., `edition`)
2. **Create a new endpoint** to get books by publisher
3. **Add validation** to ensure price is positive
4. **Write a unit test** for BookService

### **Intermediate Level**
1. **Implement soft delete** for books
2. **Add pagination** to search endpoints
3. **Create a custom exception** for business logic errors
4. **Add logging** to service methods

### **Advanced Level**
1. **Implement caching** for frequently accessed books
2. **Add rate limiting** to API endpoints
3. **Create a scheduled task** to check for overdue books
4. **Implement audit logging** for all operations

---

## 🚀 Next Steps

### **What You've Learned**
✅ Java fundamentals and OOP concepts  
✅ Spring Boot framework and auto-configuration  
✅ JPA/Hibernate for database operations  
✅ REST API design and implementation  
✅ Dependency injection and inversion of control  
✅ Testing with Spring Boot  
✅ Configuration management  
✅ Exception handling  
✅ Validation and error handling  

### **Ready for Real Projects**
With this knowledge, you can now:
- Contribute to open-source projects like DSpace
- Build enterprise applications
- Understand Spring Boot best practices
- Design and implement REST APIs
- Work with databases using JPA
- Write comprehensive tests
- Handle production deployments

### **Further Learning**
1. **Spring Security**: Authentication and authorization
2. **Spring Data**: Advanced querying and custom repositories
3. **Spring Cloud**: Microservices architecture
4. **Docker**: Containerization
5. **Kubernetes**: Container orchestration
6. **Monitoring**: Application performance monitoring

---

## 🎯 Interview Preparation

### **Common Spring Boot Interview Questions**

#### **1. What is Spring Boot?**
Spring Boot is a framework that simplifies Spring application development by providing:
- Auto-configuration
- Starter dependencies
- Embedded servers
- Production-ready features

#### **2. What is Dependency Injection?**
Dependency Injection is a design pattern where dependencies are provided to a class rather than the class creating them itself. This promotes loose coupling and makes code more testable.

#### **3. What is the difference between @Component, @Service, and @Repository?**
- `@Component`: Generic Spring component
- `@Service`: Business logic layer
- `@Repository`: Data access layer

All are stereotypes of `@Component`, but they provide semantic meaning.

#### **4. What is JPA?**
JPA (Java Persistence API) is a specification for object-relational mapping in Java. Hibernate is the most popular implementation of JPA.

#### **5. What is the difference between @Entity and @Table?**
- `@Entity`: Marks a class as a JPA entity
- `@Table`: Specifies the database table name (optional)

#### **6. What is the purpose of @Transactional?**
`@Transactional` ensures that methods are executed within a database transaction. If an exception occurs, the transaction is rolled back.

#### **7. What is the difference between @RequestParam and @PathVariable?**
- `@RequestParam`: Extracts query parameters from URL
- `@PathVariable`: Extracts path variables from URL

#### **8. What is the purpose of @Valid?**
`@Valid` triggers validation of the request body using Bean Validation annotations.

#### **9. What is the difference between @GetMapping and @PostMapping?**
- `@GetMapping`: Maps HTTP GET requests
- `@PostMapping`: Maps HTTP POST requests

#### **10. What is the purpose of ResponseEntity?**
`ResponseEntity` allows you to control the HTTP response, including status code, headers, and body.

---

## 🏆 Congratulations!

You've successfully learned Java Spring Boot from scratch! This comprehensive project covers all the essential concepts you need to:

- **Build real-world applications**
- **Contribute to open-source projects like DSpace**
- **Pass Spring Boot interviews**
- **Understand enterprise Java development**

Keep practicing, building projects, and exploring advanced topics. The journey to becoming a Spring Boot expert continues! 🚀

---

## 📞 Support

If you have any questions or need help:
1. Check the code comments for detailed explanations
2. Run the tests to understand expected behavior
3. Experiment with the API endpoints
4. Read the Spring Boot documentation
5. Join Spring Boot communities

**Happy Coding!** 🎉
