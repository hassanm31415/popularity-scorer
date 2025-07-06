
# GitHub Repository Popularity Scoring Service

This service provides a RESTful API to score GitHub repositories based on a search query, popularity scores based on:

- Stars ⭐
- Forks 🍴
- Recency ⏳

Built with **Spring Boot**, **WebClient**, and **non-blocking** architecture.


## 📖 API Documentation

Swagger UI:  
http://localhost:8080/swagger-ui/index.html

## ⚡ Scoring Strategies

Two scoring strategies are currently supported:

### 1️⃣ Default Scorer

Each factor is scaled from **0 to 100** based on observed repository data.
Final score ranges from **0 (lowest)** to **100 (highest)**.

**Weight Breakdown:**

- ⭐ Stars: 35%
- 🍴 Forks: 35%
- 🔄 Recent Push Activity: 20%
- 🛠️ Recent Updates: 10%


Where recentUpdateBonus is applied for repositories updated within the last year.

### 2️⃣ Simple Scorer

score = stars + forks + 100 (if updated within a year)

Specify scorer with scorer=default or scorer=simple query parameter.

## 🛠️ Build and Run Locally with Maven

Ensure you have **Maven 3.9+** and **Java 24** installed.

### Build the Project

```bash
mvn clean package -DskipTests
````
This creates the executable JAR in the target/ directory.

Run the Application

```bash
java -jar target/popularity-scorer.jar
````

## 🧩 Example Request

GET /api/repositories/search?query=spring&language=Java&createdAfter=2023-01-01


## ✨ Future Improvements

- Score repositories in batches
- Caching popular results
- GitHub authentication for higher rate limits

