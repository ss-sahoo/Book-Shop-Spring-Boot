# 🚀 How to Run the Library Management System

## ✅ Prerequisites Check

You already have everything installed! ✓
- **Java 17** ✓
- **Maven 3.9.6** ✓
- **PostgreSQL** ✓

## 🐘 Quick PostgreSQL Setup

### **Option 1: Use Existing PostgreSQL**
```bash
# Create database manually
psql -U postgres -h localhost
CREATE DATABASE library_db;
\q
```

### **Option 2: Use Docker (Recommended)**
```bash
# Run PostgreSQL in Docker
docker run --name library-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=library_db \
  -p 5432:5432 \
  -d postgres:15
```

### **Option 3: Use Setup Script**
```bash
./setup-postgres.sh
```

📖 **For detailed PostgreSQL setup, read:** `POSTGRESQL_SETUP.md`

---

## 📖 Step-by-Step Running Instructions

### **Method 1: Using Maven (Recommended for Development)**

#### 1. **Navigate to Project Directory**
```bash
cd "/home/diracai/Music/java Spring boot"
```

#### 2. **Clean and Build the Project**
```bash
mvn clean install
```

This will:
- Clean any previous builds
- Compile your Java code
- Download all dependencies
- Package your application into a JAR file

#### 3. **Run the Application**
```bash
mvn spring-boot:run
```

This will start the application on **http://localhost:8080**

You should see output like:
```
Started LibraryManagementApplication in X seconds
```

---

### **Method 2: Using the JAR File**

#### 1. **Build the JAR**
```bash
mvn clean package
```

#### 2. **Run the JAR**
```bash
java -jar target/library-management-system-1.0.0.jar
```

---

## 🎯 Testing the Application

### **1. Check if Application is Running**

```bash
# Check if port 8080 is listening
lsof -i :8080
```

You should see Java process running.

---

### **2. Test API Endpoints**

#### **Get All Books**
```bash
curl -X GET http://localhost:8080/api/v1/books
```

**Expected Output:** List of books (or empty array if no books exist)

---

#### **Create a New Book**
```bash
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
```

**Expected Output:** JSON response with the created book details including ID

---

#### **Get Book by ID**
```bash
# Replace {id} with actual book ID
curl -X GET http://localhost:8080/api/v1/books/{id}
```

---

#### **Update a Book**
```bash
# Replace {id} with actual book ID
curl -X PUT http://localhost:8080/api/v1/books/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Java Spring Boot Guide",
    "author": "John Doe",
    "isbn": "978-0-123456-78-9",
    "publisher": "Tech Publisher",
    "publicationDate": "2023-01-01",
    "category": "Programming",
    "pages": 600,
    "price": 59.99,
    "description": "An updated comprehensive guide to Spring Boot",
    "totalCopies": 15,
    "language": "English"
  }'
```

---

#### **Delete a Book**
```bash
# Replace {id} with actual book ID
curl -X DELETE http://localhost:8080/api/v1/books/{id}
```

---

#### **Search Books by Title**
```bash
curl -X GET "http://localhost:8080/api/v1/books/search?title=Java"
```

---

#### **Search Books by Author**
```bash
curl -X GET "http://localhost:8080/api/v1/books/search/author?author=John"
```

---

#### **Search Books by Category**
```bash
curl -X GET "http://localhost:8080/api/v1/books/search/category?category=Programming"
```

---

#### **Get Available Books**
```bash
curl -X GET http://localhost:8080/api/v1/books/available
```

---

#### **Get Most Popular Books**
```bash
curl -X GET http://localhost:8080/api/v1/books/popular
```

---

## 🗄️ Database Access

The application uses **H2 in-memory database** in development mode.

### **Access H2 Console**

1. Open your browser and go to:
   ```
   http://localhost:8080/h2-console
   ```

2. Enter connection details:
   - **JDBC URL:** `jdbc:h2:mem:library_db`
   - **Username:** `sa`
   - **Password:** (leave empty)

3. Click **Connect**

4. You can now:
   - View all tables (BOOK, USER, BORROWING_RECORD)
   - Run SQL queries
   - See data in real-time

---

## 📊 View Application Logs

The application logs will be displayed in the terminal where you ran the command.

To see more detailed logs, check:
```bash
tail -f logs/application.log
```

---

## 🛑 Stop the Application

### If running with `mvn spring-boot:run`:
Press **Ctrl + C** in the terminal

### If running as background process:
```bash
# Find the process
lsof -i :8080

# Kill the process (replace PID with actual process ID)
kill -9 <PID>
```

Or use this one-liner:
```bash
lsof -ti:8080 | xargs kill -9
```

---

## 🔄 Different Profiles

### **Development Profile (Default)**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Uses H2 in-memory database

### **Production Profile**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
Uses MySQL database (make sure MySQL is running)

---

## 🐛 Troubleshooting

### **Port 8080 Already in Use**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or change the port in application.properties
server.port=8081
```

### **Build Failed**
```bash
# Clean Maven cache and rebuild
mvn clean install -U
```

### **Database Connection Issues**
- Check if H2 console is accessible
- Verify database URL in `application-dev.properties`
- Check logs for detailed error messages

---

## 📱 Using a REST Client (Optional)

Instead of curl, you can use:

### **1. Postman**
- Download from: https://www.postman.com/downloads/
- Import API endpoints
- Visual interface for testing

### **2. Insomnia**
- Download from: https://insomnia.rest/download
- Similar to Postman

### **3. Browser Extensions**
- **Talend API Tester** (Chrome/Firefox)
- **Thunder Client** (VS Code extension)

---

## 🎓 What to Do Next

1. **Explore the Code:**
   - Start with `LibraryManagementApplication.java`
   - Check `BookController.java` for REST endpoints
   - Look at `BookService.java` for business logic

2. **Modify and Test:**
   - Add new fields to Book entity
   - Create new endpoints
   - Test your changes

3. **Check Database:**
   - Use H2 console to see data
   - Run SQL queries
   - Understand JPA mappings

4. **Read Documentation:**
   - `README.md` - Complete learning guide
   - `QUICK_START.md` - Quick reference
   - `DEPLOYMENT.md` - Deployment instructions

---

## 🎯 Practice Exercises

1. **Create 5 books** using the POST endpoint
2. **Retrieve all books** using the GET endpoint
3. **Update one book** using the PUT endpoint
4. **Delete one book** using the DELETE endpoint
5. **Search books** by different criteria
6. **Check the database** using H2 console

---

## 📞 Need Help?

If you encounter any issues:
1. Check the terminal logs
2. Check the H2 console
3. Read the README.md
4. Review the code comments

---

**Happy Coding! 🚀**

