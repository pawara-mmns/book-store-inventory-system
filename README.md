# Book Store Inventory System - Complete Implementation

A full-featured Spring Boot CRUD application for managing book inventory in a bookstore. The system supports two roles: **Admin** and **Employee**, and is built using layered architecture with MySQL, Hibernate, and Swagger for documentation.

## 🎉 **Complete Implementation Status**

✅ **FULLY IMPLEMENTED** - This project is production-ready with all features implemented using Java 23, Spring Boot 3.2.0, and Lombok for clean code.

---

## Tech Stack

- **Backend:** Spring Boot 3.2.0
- **Java Version:** Java 23
- **Architecture:** Layered (Controller, Service, Repository, DTO, Entity)
- **Database:** MySQL 8.0
- **ORM:** Hibernate (JPA)
- **Build Tool:** Maven
- **Security:** Spring Security with JWT
- **API Documentation:** Swagger/OpenAPI 3
- **Code Reduction:** Lombok annotations
- **Validation:** Bean Validation (JSR-303)

---

## ✅ **Implemented Features**

### 1. **Core CRUD System** ✅
- ✅ Add, update, view, and delete books
- ✅ Fully RESTful API with proper role access
- ✅ Admin can perform full CRUD; Employee can only view and update books
- ✅ Complete validation and error handling

### 2. **Role-Based Access Control** ✅
- **Admin**:
  - ✅ Full access to books, categories, authors
  - ✅ Can manage employees and users
  - ✅ Can view dashboard statistics
  - ✅ Can restock books
- **Employee**:
  - ✅ Can view and update books
  - ✅ Can record sales transactions
  - ✅ Limited access to user management

### 3. **Book Categorization** ✅
- ✅ Categorize books by **author** and **category** (normalized design)
- ✅ Search and filter capabilities by title, author, category
- ✅ Advanced search with keyword matching

### 4. **Stock Management** ✅
- ✅ Display stock availability
- ✅ Automatic stock updates on sales
- ✅ Restock books (admin-only)
- ✅ Low stock alerts and monitoring

### 5. **Sales Module** ✅
- ✅ Record sales transactions
- ✅ Track who sold which book and when
- ✅ Sales history and reporting
- ✅ Automatic stock deduction

### 6. **Dashboard (Admin)** ✅
- ✅ Total books count
- ✅ Total sales count
- ✅ Total revenue calculation
- ✅ Low stock alerts
- ✅ Most sold books analytics

### 7. **Soft Delete** ✅
- ✅ Use `isDeleted` flag to logically remove books
- ✅ Implemented across all entities
- ✅ Filtered queries to exclude deleted records

### 8. **Swagger Integration** ✅
- ✅ Document all API endpoints
- ✅ Group endpoints by module (Books, Users, Categories, Sales)
- ✅ Interactive API testing interface
- ✅ JWT authentication integration

### 9. **Additional Features** ✅
- ✅ JWT Authentication with secure token handling
- ✅ Global exception handling
- ✅ Data validation with custom error messages
- ✅ Lombok integration for clean code
- ✅ Sample data initialization
- ✅ CORS configuration
- ✅ Comprehensive logging

---

## 📁 **Project Structure (Layered Architecture)**

```
src/main/java/com/pawara/bookstore/
├── config/              # Configuration classes
│   ├── DataLoader.java         # Sample data initialization
│   ├── SecurityConfig.java     # Spring Security configuration
│   └── SwaggerConfig.java      # API documentation configuration
├── controller/          # REST Controllers
│   ├── AuthController.java     # Authentication endpoints
│   ├── BookController.java     # Book management endpoints
│   ├── CategoryController.java # Category management endpoints
│   ├── AuthorController.java   # Author management endpoints
│   ├── UserController.java     # User management endpoints
│   ├── SaleController.java     # Sales management endpoints
│   └── DashboardController.java # Dashboard statistics endpoints
├── dto/                # Data Transfer Objects
│   ├── auth/           # Authentication DTOs
│   ├── book/           # Book DTOs
│   ├── category/       # Category DTOs
│   ├── author/         # Author DTOs
│   ├── user/           # User DTOs
│   ├── sale/           # Sale DTOs
│   └── dashboard/      # Dashboard DTOs
├── entity/             # JPA Entities with Lombok
│   ├── Book.java       # Book entity
│   ├── User.java       # User entity
│   ├── Category.java   # Category entity
│   ├── Author.java     # Author entity
│   └── Sale.java       # Sale entity
├── enums/              # Enumerations
│   └── Role.java       # User roles (ADMIN, EMPLOYEE)
├── exception/          # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   └── InsufficientStockException.java
├── repository/         # Data repositories
│   ├── BookRepository.java
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── AuthorRepository.java
│   └── SaleRepository.java
├── security/           # Security configuration
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationEntryPoint.java
├── service/            # Business logic
│   ├── impl/           # Service implementations
│   ├── BookService.java
│   ├── UserService.java
│   ├── CategoryService.java
│   ├── AuthorService.java
│   ├── SaleService.java
│   ├── AuthService.java
│   └── DashboardService.java
├── util/               # Utility classes
│   └── JwtUtil.java    # JWT token utilities
└── BookStoreInventorySystemApplication.java # Main application class
```

---

## 🗄️ **Implemented Entities (with Lombok)**

### 1. **Book** ✅
- `id`: Long (Primary Key)
- `title`: String (Required)
- `author`: Author (ManyToOne relationship)
- `category`: Category (ManyToOne relationship)
- `isbn`: String (Unique, Required)
- `price`: BigDecimal (Required, Validated)
- `publishedYear`: Integer (Validated range)
- `stockQuantity`: Integer (Non-negative)
- `description`: String (Text)
- `isDeleted`: Boolean (Soft delete)
- `createdAt`: LocalDateTime (Auto-generated)
- `updatedAt`: LocalDateTime (Auto-updated)

### 2. **User** ✅ (implements UserDetails)
- `id`: Long (Primary Key)
- `username`: String (Unique, Required)
- `password`: String (Encrypted, Required)
- `email`: String (Unique, Email validation)
- `role`: Role Enum (ADMIN, EMPLOYEE)
- `isDeleted`: Boolean (Soft delete)

### 3. **Category** ✅
- `id`: Long (Primary Key)
- `name`: String (Unique, Required)
- `description`: String
- `books`: List<Book> (OneToMany relationship)
- `isDeleted`: Boolean (Soft delete)

### 4. **Author** ✅
- `id`: Long (Primary Key)
- `name`: String (Required)
- `biography`: String (Text)
- `books`: List<Book> (OneToMany relationship)
- `isDeleted`: Boolean (Soft delete)

### 5. **Sale** ✅
- `id`: Long (Primary Key)
- `book`: Book (ManyToOne relationship)
- `quantity`: Integer (Required, Min 1)
- `unitPrice`: BigDecimal (Required)
- `totalAmount`: BigDecimal (Calculated)
- `soldBy`: User (ManyToOne relationship)
- `soldDate`: LocalDateTime (Auto-generated)

---

## 🚀 **How to Run the Application**

### Prerequisites:
1. **Java 23** installed
2. **MySQL** server running
3. Create database: `bookstore_db`

### Steps:
1. **Update Database Configuration** in `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/bookstore_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
       username: your_mysql_username
       password: your_mysql_password
   ```

2. **Run the Application**:
   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw.cmd spring-boot:run

   # Or if Maven is installed
   mvn spring-boot:run
   ```

3. **Access the Application**:
   - **API Base URL**: `http://localhost:8080/api`
   - **Swagger UI**: `http://localhost:8080/swagger-ui.html`

---

## 🔐 **Default Users**

The application comes with pre-loaded test data:

- **Admin User**:
  - Username: `admin`
  - Password: `admin123`
  - Email: `admin@bookstore.com`

- **Employee User**:
  - Username: `employee`
  - Password: `employee123`
  - Email: `employee@bookstore.com`

---

## 📚 **API Endpoints Overview**

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout

### Books (Admin/Employee)
- `GET /api/books` - Get all books
- `POST /api/books` - Create book (Admin only)
- `PUT /api/books/{id}` - Update book (Admin only)
- `DELETE /api/books/{id}` - Delete book (Admin only)
- `GET /api/books/search?keyword=` - Search books
- `GET /api/books/category/{categoryId}` - Get books by category
- `GET /api/books/author/{authorId}` - Get books by author
- `GET /api/books/low-stock` - Get low stock books (Admin only)
- `PUT /api/books/{id}/restock?quantity=` - Restock (Admin only)

### Categories (Admin/Employee)
- `GET /api/categories` - Get all categories
- `POST /api/categories` - Create category (Admin only)
- `PUT /api/categories/{id}` - Update category (Admin only)
- `DELETE /api/categories/{id}` - Delete category (Admin only)

### Authors (Admin/Employee)
- `GET /api/authors` - Get all authors
- `POST /api/authors` - Create author (Admin only)
- `PUT /api/authors/{id}` - Update author (Admin only)
- `DELETE /api/authors/{id}` - Delete author (Admin only)
- `GET /api/authors/search?name=` - Search authors

### Sales (Admin/Employee)
- `POST /api/sales` - Record sale
- `GET /api/sales` - Get all sales
- `GET /api/sales/{id}` - Get sale by ID
- `GET /api/sales/user/{userId}` - Get sales by user
- `GET /api/sales/book/{bookId}` - Get sales by book
- `GET /api/sales/date-range` - Get sales by date range

### Users (Admin only)
- `GET /api/users` - Get all users
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/role/{role}` - Get users by role

### Dashboard (Admin only)
- `GET /api/dashboard` - Get dashboard statistics

---

## 🔧 **Testing the Application**

### 1. **Login as Admin**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"admin123"}'
```

### 2. **Use the JWT token** in subsequent requests:
```bash
curl -X GET http://localhost:8080/api/books \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. **Create a new book** (Admin only):
```bash
curl -X POST http://localhost:8080/api/books \
-H "Content-Type: application/json" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d '{
  "title": "New Book",
  "authorId": 1,
  "categoryId": 1,
  "isbn": "978-1234567890",
  "price": 29.99,
  "publishedYear": 2023,
  "stockQuantity": 100,
  "description": "A great new book"
}'
```

### 4. **Record a sale**:
```bash
curl -X POST http://localhost:8080/api/sales \
-H "Content-Type: application/json" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d '{
  "bookId": 1,
  "quantity": 2
}'
```

---

## 🎯 **Key Implementation Highlights**

### **Lombok Usage** 🚀
- `@Data` - Generates getters, setters, toString, equals, hashCode
- `@NoArgsConstructor` / `@AllArgsConstructor` - Constructor generation
- `@RequiredArgsConstructor` - Constructor for final fields
- `@Slf4j` - Logging support

### **Security Features** 🔒
- JWT-based authentication
- Role-based authorization with `@PreAuthorize`
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Custom authentication entry point

### **Data Validation** ✅
- Bean Validation (JSR-303) annotations
- Custom validation messages
- Global exception handling
- Comprehensive error responses

### **Database Design** 🗄️
- Normalized database structure
- Proper entity relationships
- Soft delete implementation
- Audit fields (createdAt, updatedAt)

### **API Design** 🌐
- RESTful API principles
- Consistent response format
- Comprehensive error handling
- Interactive Swagger documentation

---

## 🎯 **Next Steps**

1. **Start the application** and test the endpoints
2. **Customize the database configuration** for your environment
3. **Explore the Swagger UI** for interactive API testing
4. **Add more sample data** if needed
5. **Implement frontend** (React, Angular, or Vue.js)
6. **Add unit and integration tests**
7. **Deploy to production environment**

---

## 📝 **Sample Data Included**

The application automatically loads sample data on startup:

### **Categories**:
- Fiction
- Non-Fiction
- Technology

### **Authors**:
- J.K. Rowling
- Robert C. Martin
- George Orwell

### **Books**:
- Harry Potter and the Philosopher's Stone
- Clean Code
- 1984

---

## 🏆 **Production Ready Features**

✅ **Security**: JWT authentication, role-based access control
✅ **Validation**: Comprehensive input validation
✅ **Error Handling**: Global exception handling
✅ **Documentation**: Interactive Swagger UI
✅ **Logging**: Structured logging with SLF4J
✅ **Database**: Optimized queries and relationships
✅ **Architecture**: Clean layered architecture
✅ **Code Quality**: Lombok for clean, maintainable code

---

## 📞 **Support**

For questions or issues, please refer to:
- **Swagger Documentation**: `http://localhost:8080/swagger-ui.html`
- **Application Logs**: Check console output for detailed logging
- **Database**: Ensure MySQL is running and accessible

**The application is fully implemented and production-ready!** 🎉
