# DATABASE_DESIGN.md

# Expense Sharing & Settlement System

## Database Design Document

---

# 1. Database Overview

The Expense Sharing & Settlement System stores information about:

* Users
* Groups
* Group Members
* Expenses
* Expense Splits
* Settlements

The database is designed using **Third Normal Form (3NF)** to eliminate redundancy while maintaining performance and scalability.

Database Engine:

```
MySQL 8
```

ORM

```
Hibernate (Spring Data JPA)
```

---

# 2. Entity Relationship Diagram (ER Diagram)

```text
+-----------+
|   USERS   |
+-----------+
      |
      | 1
      |
      | N
+----------------+
| GROUP_MEMBERS  |
+----------------+
      |
      | N
      |
      | 1
+------------+
|   GROUPS   |
+------------+
      |
      | 1
      |
      | N
+------------+
|  EXPENSES  |
+------------+
      |
      | 1
      |
      | N
+----------------+
| EXPENSE_SPLITS |
+----------------+

USERS
   |
   | 1
   |
   | N
SETTLEMENTS
```

---

# 3. Tables

The system contains six primary tables.

```
1. users

2. groups

3. group_members

4. expenses

5. expense_splits

6. settlements
```

---

# 4. USERS Table

Stores all registered users.

## Table

```
users
```

| Column          | Type         | Constraint |
| --------------- | ------------ | ---------- |
| id              | BIGINT       | PK         |
| full_name       | VARCHAR(100) | NOT NULL   |
| email           | VARCHAR(150) | UNIQUE     |
| password        | VARCHAR(255) | NOT NULL   |
| profile_picture | VARCHAR(255) | NULL       |
| created_at      | TIMESTAMP    | NOT NULL   |
| updated_at      | TIMESTAMP    | NOT NULL   |

---

### Primary Key

```
id
```

---

### Unique Keys

```
email
```

---

### Indexes

```
email
```

---

### Purpose

Stores authentication and profile information of every registered user.

---

# 5. GROUPS Table

Represents an expense sharing group.

Example

```
Goa Trip

Roommates

Office Team

Family
```

## Table

```
groups
```

| Column      | Type         | Constraint   |
| ----------- | ------------ | ------------ |
| id          | BIGINT       | PK           |
| group_name  | VARCHAR(150) | NOT NULL     |
| description | TEXT         | NULL         |
| created_by  | BIGINT       | FK(users.id) |
| created_at  | TIMESTAMP    | NOT NULL     |
| updated_at  | TIMESTAMP    | NOT NULL     |

---

### Relationships

```
created_by

↓

users.id
```

---

### Purpose

Stores expense groups.

---

# 6. GROUP_MEMBERS Table

A bridge table.

Many Users

↓

Many Groups

## Table

```
group_members
```

| Column    | Type      | Constraint    |
| --------- | --------- | ------------- |
| id        | BIGINT    | PK            |
| group_id  | BIGINT    | FK(groups.id) |
| user_id   | BIGINT    | FK(users.id)  |
| joined_at | TIMESTAMP | NOT NULL      |

---

### Unique Constraint

```
group_id

+

user_id
```

This prevents duplicate members.

---

### Relationships

```
Group

1

↓

N

Group Members

N

↓

1

Users
```

---

### Purpose

Maps users to groups.

---

# 7. EXPENSES Table

Stores every expense.

Example

```
Dinner

₹2500

Paid by Akshay
```

## Table

```
expenses
```

| Column       | Type          | Constraint    |
| ------------ | ------------- | ------------- |
| id           | BIGINT        | PK            |
| group_id     | BIGINT        | FK(groups.id) |
| paid_by      | BIGINT        | FK(users.id)  |
| title        | VARCHAR(150)  | NOT NULL      |
| description  | TEXT          | NULL          |
| category     | VARCHAR(50)   | NOT NULL      |
| total_amount | DECIMAL(12,2) | NOT NULL      |
| split_type   | ENUM          | NOT NULL      |
| expense_date | DATE          | NOT NULL      |
| created_at   | TIMESTAMP     | NOT NULL      |
| updated_at   | TIMESTAMP     | NOT NULL      |

---

### Split Types

```
EQUAL

PERCENTAGE

EXACT
```

---

### Categories

```
Food

Travel

Fuel

Hotel

Shopping

Medical

Entertainment

Other
```

---

### Relationships

```
Group

↓

Expenses

↓

Paid By User
```

---

### Purpose

Stores the master expense information.

---

# 8. EXPENSE_SPLITS Table

The most important table.

Stores how each expense is divided.

Example

Expense

```
₹1200
```

Members

```
Akshay

400

Rahul

400

Amit

400
```

Each row is stored separately.

---

## Table

```
expense_splits
```

| Column     | Type          | Constraint      |
| ---------- | ------------- | --------------- |
| id         | BIGINT        | PK              |
| expense_id | BIGINT        | FK(expenses.id) |
| user_id    | BIGINT        | FK(users.id)    |
| amount     | DECIMAL(12,2) | NOT NULL        |
| percentage | DECIMAL(5,2)  | NULL            |

---

### Example Data

| expense_id | user   | amount |
| ---------- | ------ | ------ |
| 1          | Akshay | 400    |
| 1          | Rahul  | 400    |
| 1          | Amit   | 400    |

---

### Purpose

Stores the calculated split for every participant.

---

# 9. SETTLEMENTS Table

Stores payment history.

Example

```
Rahul paid

Akshay

₹500
```

---

## Table

```
settlements
```

| Column          | Type          | Constraint    |
| --------------- | ------------- | ------------- |
| id              | BIGINT        | PK            |
| payer_id        | BIGINT        | FK(users.id)  |
| receiver_id     | BIGINT        | FK(users.id)  |
| group_id        | BIGINT        | FK(groups.id) |
| amount          | DECIMAL(12,2) | NOT NULL      |
| settlement_date | TIMESTAMP     | NOT NULL      |

---

### Purpose

Stores every completed settlement transaction.

---

# 10. Relationships

```
User

1

↓

N

Group

(created_by)
```

---

```
Group

1

↓

N

Expenses
```

---

```
Expense

1

↓

N

Expense Splits
```

---

```
Group

N

↓

N

Users

(using Group Members)
```

---

```
Users

↓

Settlements

↓

Users
```

---

# 11. Index Strategy

Indexes improve query performance.

### USERS

```
email
```

---

### GROUP_MEMBERS

Composite Index

```
group_id

user_id
```

---

### EXPENSES

Indexes

```
group_id

paid_by

expense_date
```

---

### EXPENSE_SPLITS

Indexes

```
expense_id

user_id
```

---

### SETTLEMENTS

Indexes

```
payer_id

receiver_id

group_id
```

---

# 12. Constraints

## Users

* Email must be unique.

---

## Groups

* Group name cannot be empty.

---

## Group Members

* Same user cannot join the same group twice.

---

## Expenses

* Amount > 0
* Paid By user must belong to the group.

---

## Expense Splits

Equal Split

```
Total Split Amount

=

Expense Amount
```

---

Percentage Split

```
Total Percentage

=

100
```

---

Exact Split

```
Total Exact Amount

=

Expense Amount
```

---

## Settlements

* Settlement amount > 0
* Cannot settle more than outstanding balance.

---

# 13. Normalization

The database follows **Third Normal Form (3NF)**:

### First Normal Form (1NF)

* No repeating groups.
* Atomic column values.

### Second Normal Form (2NF)

* Every non-key attribute fully depends on the primary key.

### Third Normal Form (3NF)

* No transitive dependencies.
* Separate bridge table (`group_members`) resolves the many-to-many relationship between users and groups.
* Expense split details are stored independently in `expense_splits`, avoiding duplicate expense data.

---

# 14. Future Database Enhancements

The following tables can be added in future versions:

```
notifications

refresh_tokens

audit_logs

attachments

currencies

budgets

recurring_expenses

expense_comments

payment_transactions
```

---

# 15. Why This Database Design?

This design provides:

* Clear separation of concerns.
* Minimal data duplication.
* Efficient joins.
* Easy balance calculations.
* Support for multiple split strategies.
* Scalable structure for new features.
* Compatibility with Spring Data JPA relationships.

The schema is intentionally modular so that additional capabilities such as notifications, recurring expenses, and payment integrations can be added without major structural changes.

