# Product Management API

A RESTful API for managing store products. This application provides endpoints to create, retrieve, and update product information.

## Features

- Get product details by SKU
- Get multiple products by list of SKUs
- Add new products
- Partially update existing products
- Product stock information management

## Technology Stack

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- H2 Database (in-memory)
- Flyway for database migrations
- Lombok for reducing boilerplate code
- Docker for containerization
- GitLab CI/CD for continuous integration

## API Endpoints

### Get Product by SKU
```
GET /product/{sku}
```

### Get Multiple Products by SKUs
```
GET /products?skus=123,4567,8901
```

### Create a New Product
```
POST /product
Content-Type: application/json

{
  "sku": "XYZ123",
  "name": "New Product",
  "description": "Product description",
  "price": 29.99,
  "stockQuantity": 100
}
```

### Update Product Partially
```
PATCH /product/{sku}
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated Description",
  "price": 34.99
}
```

## Running the Application

### Running Locally with Maven

1. Ensure you have Java 17 and Maven installed
2. Clone the repository
3. Build the application:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. The API will be available at http://localhost:8080

### Running with Docker

#### Building the Docker Image
```bash
docker build -t store-product-api .
```

#### Running the Container
```bash
docker run -p 8080:8080 store-product-api
```

### Environment Configuration to use for accessing Database in H2Console

The application uses the following environment variables that you can override:

| Variable | Description | Default             |
|----------|-------------|---------------------|
| `SPRING_DATASOURCE_URL` | Database connection URL | jdbc:h2:mem:storedb |
| `SPRING_DATASOURCE_USERNAME` | Database username | store               |
| `SPRING_DATASOURCE_PASSWORD` | Database password | password            |


## CI/CD Pipeline

The project includes GitLab CI/CD configuration in `.gitlab-ci.yml` that:

1. Builds the application
2. Runs unit tests
3. Packages the application as a JAR file
4. Builds a Docker image

The pipeline runs automatically when changes are pushed to the repository.

## Development

### Database Migrations

The application uses Flyway for database migrations. Migration files are located in:
- Java migrations: `src/main/java/org/imedia/store/db/migration/`

### Testing

Run unit tests with:
```bash
mvn test
```

## API Documentation

Access the API documentation by running the application and navigating to:
http://localhost:8080/swagger-ui.html 