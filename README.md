# Wallet API

Wallet API is a simple RESTful service for managing wallets and transactions. It enables users to create wallets, add funds, and perform transactions.

This project was designed as a synchronous REST API backed by a relational database, taking into account the limited development time. This approach reduces complexity in both development and transaction control.

PostgreSQL is used as the primary database to ensure transactional integrity, leveraging its robustness. As the application grows, it can be scaled into a distributed, PostgreSQL-based solution, facilitating horizontal scaling. Redis is employed for idempotency and request control, preventing extra processing on duplicate requests.

## Installation

### Requirements

* Java 21
* Gradle
* PostgreSQL
* Redis
* Docker (optional, for running PostgreSQL and Redis in containers)

## Running the Application

The easiest way to run the application is via Docker.

> **If you have `make` installed**, run:

```bash
$ make build
$ make run
```

> **If you don't have `make`**, run:

```bash
$ ./gradlew bootJar --no-daemon --info
$ docker build -t wallet-app .
$ docker-compose -f docker-compose-all.yml up -d
```

## Testing

To test the API, navigate to the [Swagger UI](http://localhost:8080/swagger-ui.html), where you’ll find a list of all endpoints along with descriptions of each operation.
