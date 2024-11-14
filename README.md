
# URL Shortener Challenge API with Spring Boot, Docker & MongoDB

Welcome to the **URL Shortener API** project! This project showcases a simple URL shortener service built with **Spring Boot**, using **MongoDB** for storage. The application is fully containerized with **Docker** for both the backend API and MongoDB database.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green) ![Java 17](https://img.shields.io/badge/Java-17-orange) [![Docker Hub Repo](https://img.shields.io/docker/pulls/fernandesrh/urlshortener-api.svg)](https://hub.docker.com/r/fernandesrh/urlshortener-api)


## 🚀 Getting Started

Follow these steps to run the application on your local machine. The process is similar for both Linux and Windows environments, but I'll detail them separately for clarity.

### 🔥 Prerequisites

1. **Docker** - You'll need Docker installed on your machine to run the containers.
2. **Docker Compose** - Used for managing multi-container Docker applications (API + MongoDB).
3. **Git** - To clone the repository from GitHub.
4. **Java JDK 17** - Installed
5.  Have a **Windows / Linux** machine

If you haven't installed Docker yet, follow the [installation guide for Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/).

### ✔️ Cloning the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/RodrigoFernandes79/url-shortener-backend-challenge.git
cd url-shortener-backend-challenge
```

---

## 📜 Environment Variables

Make sure to set the following environment variables before running the application:

- **MONGODB_USER**: MongoDB username (e.g., `admin`)
- **MONGODB_PASSWORD**: MongoDB password
- **MONGODB_DATABASE**: The name of the database (e.g., `url-shortener-db`)

You can create a `.env` file in the project root to manage these variables.

Example `.env`:

```
MONGODB_USER=your_user
MONGODB_PASSWORD=your_password
MONGODB_DATABASE=url-shortener-db
```

---

## 🐳 Running the Application with Docker

Once you've cloned the project, follow the steps below to run the application.

### 🐧 On Linux

1. **Navigate to the project folder**:

```bash
cd path/to/url-shortener-backend-challenge
```
2. **Build the project** with Maven:

```bash
mvn clean package
```
3. **Build the Docker containers**:

Run the following command to build your containers and start the application:

```bash
docker-compose up --build
```

4. **Access the application**:

Once the containers are up and running, you can access the **URL Shortener API** by visiting:

- To access the API endpoints, go to the URL: `http://localhost:8080/swagger-ui/index.html`

### 💻 On Windows

1. **Install Docker Desktop for Windows** (if not already installed).

2. **Clone the repository**:

```bash
git clone https://github.com/RodrigoFernandes79/url-shortener-backend-challenge.git
cd url-shortener-backend-challenge
```
3. **Build the project** with Maven:

```bash
mvn clean package
```
4. **Build the Docker containers**:

Open **PowerShell** or **Command Prompt** and run the following:

```bash
docker-compose up --build
```

5. **Access the application**:

Once the containers are running, you can access the **URL Shortener API** at:

- To access the API endpoints, go to the URL: `http://localhost:8080/swagger-ui/index.html`

---

## 🛠️ How It Works

- The **MongoDB** container stores the URL data, and the **URL Shortener API** interacts with it.
- The **API** exposes endpoints to create shortened URLs, redirect to original URLs, and view statistics.

---

## 🗃️ Dockerfile Explained

The Dockerfile used for the **URL Shortener API** container is as follows:

```dockerfile
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

This Dockerfile does the following:

- **FROM**: Uses `openjdk:17-jdk-slim` as the base image.
- **COPY**: Copies the JAR file (built by Maven) into the container.
- **ENTRYPOINT**: Defines the command to run the Spring Boot application inside the container.

---

## 🧑‍💻 API Endpoints

Here are some of the key endpoints of the **URL Shortener API**:

- `POST /api/v1/urls/shorten`: Create a new shortened URL.
- `GET /api/v1/urls/{shortUrl}`: Redirect to the original URL.
- `GET /api/v1/urls/{shortUrl}/statistics`: View statistics for the shortened URL.

---

## 💡 Troubleshooting

- If you encounter issues with MongoDB not starting, ensure that Docker is running correctly.
- If you see errors related to `docker-compose`, try running `docker-compose down` and then `docker-compose up --build` again.

---

## 🎓 Contribute

If you'd like to contribute, feel free to fork this repository, submit an issue, or create a pull request. Contributions are always welcome!

---

## 📝 About Me

[![GitHub](https://img.shields.io/badge/GitHub-RodrigoFernandes79-24292F?logo=github&logoColor=white)](https://github.com/RodrigoFernandes79)

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Rodrigo%20Fernandes-0077B5?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rodrigo-fernandes-b12b7a169/)

[![Email](https://img.shields.io/badge/Email-rodrigohf79%40hotmail.com-D14836?logo=gmail&logoColor=white)](mailto:rodrigohf79@hotmail.com)

Thank you for reviewing my project!

---

**Made with ❤️ by Rodrigo Fernandes**
