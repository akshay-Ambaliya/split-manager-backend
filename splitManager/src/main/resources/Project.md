# PROJECT.md

# Expense Sharing & Settlement System (Splitwise Clone)

**Version:** 1.0.0

**Project Type:** Backend REST API

**Architecture:** Layered (Controller → Service → Repository → Database)

**Author:** Akshay

**Technology Stack:**

* Java 21
* Spring Boot 3.x
* Spring Security
* JWT Authentication
* Spring Data JPA (Hibernate)
* MySQL
* Maven
* Swagger/OpenAPI
* Lombok
* Bean Validation
* JUnit 5
* Mockito
* Docker (Future Enhancement)

**Project Duration:** 3 Days (MVP)

**Document Version:** 1.0

**Last Updated:** June 2026

---

# Table of Contents

1. Project Overview
2. Problem Statement
3. Objectives
4. Scope
5. Technology Stack
6. Project Features
7. Project Modules

---

# 1. Project Overview

## 1.1 Introduction

The **Expense Sharing & Settlement System** is a RESTful backend application inspired by applications like **Splitwise**. The primary goal of the system is to simplify the process of tracking shared expenses among multiple users and automatically calculate who owes whom after every expense.

In everyday life, people frequently share expenses while traveling, living together, organizing events, or working on projects. Calculating each person's contribution manually often leads to confusion and errors. This application automates expense tracking, balance calculation, and settlement management through secure REST APIs.

The project focuses entirely on backend development using **Spring Boot**, following enterprise software development practices including authentication, validation, layered architecture, transaction management, exception handling, logging, and API documentation.

The system is designed to be scalable, maintainable, and production-ready, allowing future enhancements such as notifications, recurring expenses, payment gateway integration, and microservices.

---

## 1.2 Purpose

The purpose of this project is to build a production-quality backend service that demonstrates modern Java backend development practices while solving a real-world expense sharing problem.

This project is intended to:

* Demonstrate enterprise backend architecture.
* Showcase clean code and layered design.
* Provide secure REST APIs.
* Implement complex business logic.
* Demonstrate transaction management.
* Showcase Spring Security with JWT.
* Serve as a portfolio project for interviews.

---

## 1.3 Target Users

The system is designed for:

* Friends sharing travel expenses
* Roommates sharing household expenses
* Families managing common expenses
* Office teams sharing project expenses
* Event organizers
* Student groups
* Clubs and communities

---

## 1.4 Business Value

The application provides several business benefits:

* Eliminates manual expense calculations.
* Reduces calculation errors.
* Maintains complete expense history.
* Provides transparent financial records.
* Simplifies settlements.
* Encourages accountability among group members.
* Can be extended into a commercial expense management platform.

---

# 2. Problem Statement

Managing shared expenses manually is difficult and error-prone. People often struggle to remember:

* Who paid for what?
* How much did each person contribute?
* Who owes money to whom?
* How much remains unpaid?
* Which expenses belong to which group?

For example, during a trip with five friends:

* One person pays for hotel.
* Another pays for food.
* Someone else pays for fuel.

At the end of the trip, calculating the final balances manually becomes tedious.

The absence of a centralized system leads to:

* Incorrect calculations
* Missed payments
* Duplicate payments
* Lack of transparency
* Disputes among members

The Expense Sharing & Settlement System addresses these challenges by automatically recording expenses, calculating balances, and maintaining a complete settlement history.

---

# 3. Objectives

The primary objectives of the project are:

## Functional Objectives

* Provide secure user authentication using JWT.
* Allow users to create and manage expense groups.
* Enable users to add and manage group members.
* Record shared expenses.
* Support multiple expense splitting strategies.
* Automatically calculate balances.
* Allow users to settle outstanding dues.
* Maintain complete expense and settlement history.
* Provide searchable and filterable expense records.

---

## Technical Objectives

* Build RESTful APIs following industry standards.
* Implement layered architecture.
* Apply SOLID principles.
* Use Data Transfer Objects (DTOs).
* Perform server-side validation.
* Implement centralized exception handling.
* Use transactional database operations.
* Secure APIs using Spring Security.
* Generate API documentation using Swagger.
* Write unit tests for business logic.

---

## Learning Objectives

This project demonstrates practical experience in:

* Spring Boot
* Spring Security
* JWT Authentication
* Hibernate/JPA
* Database Design
* REST API Development
* Bean Validation
* Exception Handling
* Logging
* Transaction Management
* Strategy Design Pattern
* Repository Pattern
* Clean Architecture

---

# 4. Scope

## In Scope

The first version (MVP) includes:

### User Management

* User Registration
* User Login
* JWT Authentication
* User Profile Management

---

### Group Management

* Create Group
* Update Group
* Delete Group
* View Groups
* Add Members
* Remove Members
* View Group Members

---

### Expense Management

* Create Expense
* Update Expense
* Delete Expense
* View Expenses
* Categorize Expenses

---

### Expense Splitting

The application supports three types of expense splitting:

#### Equal Split

The expense is divided equally among selected members.

Example:

Expense Amount: ₹1200

Members:

* Akshay
* Rahul
* Amit

Each member pays:

₹400

---

#### Percentage Split

The expense is divided according to percentages.

Example:

Expense Amount: ₹5000

Akshay → 40%

Rahul → 30%

Amit → 30%

---

#### Exact Split

The exact amount for each member is provided manually.

Example:

Expense Amount: ₹5000

Akshay → ₹2000

Rahul → ₹1500

Amit → ₹1500

---

### Balance Management

The application calculates:

* Total Amount Paid
* Total Amount Owed
* Group Balance
* Personal Balance
* Net Balance

---

### Settlement

Users can:

* Settle dues
* View settlement history
* Track payment records

---

## Out of Scope (Version 1)

The following features are intentionally excluded from the MVP and may be added in future releases:

* Mobile Application
* Payment Gateway Integration
* UPI Integration
* Email Notifications
* SMS Notifications
* Push Notifications
* OCR Bill Scanning
* AI Expense Categorization
* Multi-Currency Support
* Offline Synchronization
* Microservices Architecture
* Redis Caching
* Kafka/RabbitMQ Messaging
* Real-Time WebSocket Notifications

---

# 5. Technology Stack

| Layer                | Technology                  |
| -------------------- | --------------------------- |
| Programming Language | Java 21                     |
| Framework            | Spring Boot 3               |
| Build Tool           | Maven                       |
| Database             | MySQL                       |
| ORM                  | Hibernate / Spring Data JPA |
| Security             | Spring Security + JWT       |
| Validation           | Jakarta Bean Validation     |
| API Documentation    | Swagger / OpenAPI           |
| Testing              | JUnit 5 + Mockito           |
| Logging              | SLF4J + Logback             |
| IDE                  | IntelliJ IDEA / VS Code     |
| Version Control      | Git                         |
| Repository Hosting   | GitHub                      |
| Containerization     | Docker (Future)             |

---

# 6. Project Features

The Expense Sharing & Settlement System provides the following core features:

## Authentication

* User Registration
* User Login
* Password Encryption
* JWT Authentication
* Protected REST APIs

---

## User Management

* View Profile
* Update Profile
* Search Users

---

## Group Management

* Create Group
* Edit Group
* Delete Group
* Add Members
* Remove Members
* View Members

---

## Expense Management

* Add Expense
* Update Expense
* Delete Expense
* View Expense History
* Expense Categories
* Expense Description

---

## Expense Split Strategies

* Equal Split
* Percentage Split
* Exact Split

---

## Balance Calculation

* Personal Balance
* Group Balance
* Overall Balance
* Amount Owed
* Amount Receivable

---

## Settlement

* Record Settlement
* View Settlement History
* Update Outstanding Balances

---

## Security

* JWT Authentication
* Password Encryption
* Secure Endpoints
* Role-based architecture (extensible)

---

## Validation

* Input Validation
* Business Validation
* Duplicate Member Validation
* Split Validation
* Amount Validation

---

## Logging

* Request Logging
* Error Logging
* Business Event Logging

---

## Documentation

* Swagger UI
* REST API Documentation
* Project Documentation

---

# 7. Project Modules

The application is divided into the following logical modules:

## Module 1 — Authentication

Responsibilities:

* Register User
* Login User
* Generate JWT
* Authenticate Requests

---

## Module 2 — User Management

Responsibilities:

* User Profile
* Update Details
* Search Users

---

## Module 3 — Group Management

Responsibilities:

* Create Group
* Manage Members
* Update Group
* Delete Group

---

## Module 4 — Expense Management

Responsibilities:

* Add Expenses
* Edit Expenses
* Delete Expenses
* View Expenses

---

## Module 5 — Split Engine

Responsibilities:

* Equal Split Calculation
* Percentage Split Calculation
* Exact Split Calculation

This module contains the core business logic of the application and will use the **Strategy Design Pattern** to support multiple splitting algorithms.

---

## Module 6 — Balance Engine

Responsibilities:

* Calculate User Balances
* Calculate Group Balances
* Determine Outstanding Amounts
* Generate Net Balance

---

## Module 7 — Settlement Management

Responsibilities:

* Record Payments
* Update Balances
* Maintain Settlement History

---

## Module 8 — Dashboard & Reporting

Responsibilities:

* Total Expenses
* Total Groups
* Recent Expenses
* Monthly Statistics
* Outstanding Balances

---

## Summary

This project is designed as a production-ready backend application that demonstrates enterprise Java development practices while solving a real-world expense sharing problem. The architecture, modular design, and extensibility make it suitable as a portfolio project and a strong talking point during backend developer interviews.

