-- ===========================================
-- MYSQL INITIALIZATION SCRIPT
-- ===========================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
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

-- Create books table
CREATE TABLE IF NOT EXISTS books (
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

-- Create borrowing_records table
CREATE TABLE IF NOT EXISTS borrowing_records (
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

-- Insert sample data
INSERT INTO users (first_name, last_name, email, phone_number, date_of_birth, address, username, password, role, student_id, department) VALUES
('John', 'Doe', 'john.doe@example.com', '+1234567890', '1995-01-15', '123 Main St, City, State', 'johndoe', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'STUDENT', 'STU001', 'Computer Science'),
('Jane', 'Smith', 'jane.smith@example.com', '+1234567891', '1990-05-20', '456 Oak Ave, City, State', 'janesmith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'LIBRARIAN', 'LIB001', 'Library Science'),
('Admin', 'User', 'admin@example.com', '+1234567892', '1985-12-10', '789 Pine St, City, State', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', 'ADM001', 'Administration');

INSERT INTO books (title, author, isbn, publisher, publication_date, category, pages, price, description, total_copies, copies_available, language) VALUES
('Spring Boot in Action', 'Craig Walls', '978-1-61729-254-5', 'Manning Publications', '2021-03-15', 'Programming', 400, 49.99, 'A comprehensive guide to Spring Boot development', 5, 5, 'English'),
('Java: The Complete Reference', 'Herbert Schildt', '978-1-260-44023-2', 'McGraw-Hill', '2020-11-01', 'Programming', 1200, 59.99, 'Complete reference for Java programming', 3, 3, 'English'),
('Clean Code', 'Robert C. Martin', '978-0-13-235088-4', 'Prentice Hall', '2008-08-01', 'Software Engineering', 464, 39.99, 'A Handbook of Agile Software Craftsmanship', 4, 4, 'English'),
('Design Patterns', 'Gang of Four', '978-0-201-63361-0', 'Addison-Wesley', '1994-10-21', 'Software Engineering', 395, 54.99, 'Elements of Reusable Object-Oriented Software', 2, 2, 'English'),
('Effective Java', 'Joshua Bloch', '978-0-13-468599-1', 'Addison-Wesley', '2017-12-27', 'Programming', 416, 44.99, 'Best practices for Java programming', 6, 6, 'English');

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_student_id ON users(student_id);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_borrowing_records_user_id ON borrowing_records(user_id);
CREATE INDEX idx_borrowing_records_book_id ON borrowing_records(book_id);
CREATE INDEX idx_borrowing_records_status ON borrowing_records(status);
CREATE INDEX idx_borrowing_records_expected_return_date ON borrowing_records(expected_return_date);


