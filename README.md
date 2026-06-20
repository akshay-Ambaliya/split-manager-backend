# 💰 Expense Sharing & Settlement System (Split Manager)

A backend REST API system inspired by Splitwise, built using **Spring Boot**, designed to manage shared expenses, group balances, and settlements between users.

---

## 🚀 Project Overview

The **Expense Sharing & Settlement System** helps users track shared expenses in groups (friends, roommates, trips, office teams) and automatically calculates who owes whom.

It eliminates manual calculations and provides a clean, automated settlement system.

---

## 🏗️ Architecture

Layered Architecture:
Controller → Service → Repository → Database



---

## 🧰 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security
- JWT Authentication
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Lombok
- Bean Validation
- Swagger / OpenAPI
- JUnit 5 & Mockito

---

## 📦 Features

### 🔐 Authentication
- User Registration
- User Login
- JWT Token Security

### 👤 User Management
- View Profile
- Update Profile
- Search Users

### 👥 Group Management
- Create Group
- Add/Remove Members
- View Groups

### 💸 Expense Management
- Add Expense
- Update/Delete Expense
- View Group Expenses

### 🔢 Split Types
- Equal Split
- Percentage Split
- Exact Split

### ⚖️ Balance System
- Group Balance Calculation
- User Balance Calculation
- Net Balance Tracking

### 💰 Settlement
- Record Payments
- Track Settlement History
- Update Balances Automatically


---

## 🗄️ Database Design

Database contains 6 main tables:

- users
- groups
- group_members
- expenses
- expense_splits
- settlements

---

## 📊 Core Modules

- Authentication Module
- User Module
- Group Module
- Expense Module
- Split Engine
- Balance Engine
- Settlement Module

---

## 📌 Example API Response

### Success Response

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {}
}
