
# API_DESIGN.md

# Expense Sharing & Settlement System

## REST API Design Document

**Base URL**

```http
http://localhost:8080/api/v1
```

---

# API Standards

## Content Type

```http
Content-Type: application/json
```

---

## Authentication

Except Register and Login, every API requires JWT Authentication.

Header

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## Success Response Format

```json
{
    "success": true,
    "message": "Operation completed successfully",
    "data": {}
}
```

---

## Error Response Format

```json
{
    "success": false,
    "timestamp": "2026-06-18T10:20:30",
    "status": 400,
    "message": "Validation Failed",
    "errors": [
        "Group name is required"
    ]
}
```

---

# Authentication APIs

---

## Register User

### Endpoint

```http
POST /auth/register
```

### Description

Registers a new user.

### Authentication

❌ Not Required

### Request

```json
{
    "fullName": "Akshay Patel",
    "email": "akshay@gmail.com",
    "password": "Password@123"
}
```

### Success Response

```json
{
    "success": true,
    "message": "User registered successfully"
}
```

### Status Codes

| Code | Description          |
| ---- | -------------------- |
| 201  | Created              |
| 400  | Validation Error     |
| 409  | Email Already Exists |

---

## Login

```http
POST /auth/login
```

Authentication

❌ Not Required

Request

```json
{
    "email":"akshay@gmail.com",
    "password":"Password@123"
}
```

Response

```json
{
    "token":"JWT_TOKEN",
    "expiresIn":3600
}
```

Status

```
200 OK

401 Unauthorized
```

---

# User APIs

---

## Get Logged In User

```http
GET /users/me
```

Authentication

✅ Required

Response

```json
{
    "id":1,
    "fullName":"Akshay Patel",
    "email":"akshay@gmail.com"
}
```

---

## Update Profile

```http
PUT /users/me
```

Request

```json
{
    "fullName":"Akshay Kumar Patel"
}
```

---

## Search Users

```http
GET /users/search?keyword=akshay
```

Returns users matching the keyword.

---

# Group APIs

---

## Create Group

```http
POST /groups
```

Request

```json
{
    "groupName":"Goa Trip",
    "description":"Goa vacation expenses"
}
```

Response

```json
{
    "groupId":1,
    "groupName":"Goa Trip"
}
```

Status

```
201 Created
```

---

## Get My Groups

```http
GET /groups
```

Returns every group where the logged-in user is a member.

---

## Get Group Details

```http
GET /groups/{groupId}
```

---

## Update Group

```http
PUT /groups/{groupId}
```

Request

```json
{
    "groupName":"Goa Trip 2026"
}
```

---

## Delete Group

```http
DELETE /groups/{groupId}
```

Status

```
204 No Content
```

---

# Group Member APIs

---

## Add Member

```http
POST /groups/{groupId}/members
```

Request

```json
{
    "userId":5
}
```

Business Rules

* User must exist.
* User must not already belong to the group.

---

## Remove Member

```http
DELETE /groups/{groupId}/members/{userId}
```

---

## Get Members

```http
GET /groups/{groupId}/members
```

Returns all group members.

---

# Expense APIs

---

## Create Expense

```http
POST /expenses
```

Request

```json
{
    "groupId":1,
    "title":"Dinner",
    "description":"Dinner at Beach",
    "category":"FOOD",
    "amount":1200,
    "paidBy":1,
    "splitType":"EQUAL",
    "splits":[
        {
            "userId":1
        },
        {
            "userId":2
        },
        {
            "userId":3
        }
    ]
}
```

Response

```json
{
    "expenseId":1,
    "message":"Expense added successfully"
}
```

Business Rules

* Payer must be a member of the group.
* Amount must be greater than zero.
* Group must exist.

---

## Update Expense

```http
PUT /expenses/{expenseId}
```

---

## Delete Expense

```http
DELETE /expenses/{expenseId}
```

---

## Get Expense

```http
GET /expenses/{expenseId}
```

---

## Get Group Expenses

```http
GET /groups/{groupId}/expenses
```

Supports

```
Pagination

Sorting

Filtering
```

Example

```http
GET /groups/1/expenses?page=0&size=10&sort=expenseDate,desc
```

---

# Equal Split API

Example Request

```json
{
    "splitType":"EQUAL",
    "amount":900,
    "members":[
        1,
        2,
        3
    ]
}
```

Automatically calculated

```
300

300

300
```

---

# Percentage Split API

Example

```json
{
    "splitType":"PERCENTAGE",
    "amount":1000,
    "splits":[
        {
            "userId":1,
            "percentage":50
        },
        {
            "userId":2,
            "percentage":30
        },
        {
            "userId":3,
            "percentage":20
        }
    ]
}
```

Validation

```
Total Percentage

=

100
```

---

# Exact Split API

Example

```json
{
    "splitType":"EXACT",
    "amount":1000,
    "splits":[
        {
            "userId":1,
            "amount":500
        },
        {
            "userId":2,
            "amount":300
        },
        {
            "userId":3,
            "amount":200
        }
    ]
}
```

Validation

```
Total Amount

=

Expense Amount
```

---

# Balance APIs

---

## Get My Balance

```http
GET /balances/me
```

Response

```json
{
    "totalOwed":600,
    "totalReceivable":1000,
    "netBalance":400
}
```

---

## Group Balance

```http
GET /balances/group/{groupId}
```

Response

```json
[
    {
        "from":"Rahul",
        "to":"Akshay",
        "amount":250
    },
    {
        "from":"Amit",
        "to":"Akshay",
        "amount":250
    }
]
```

---

## User Balance

```http
GET /balances/users/{userId}
```

Returns balances for a specific user.

---

# Settlement APIs

---

## Create Settlement

```http
POST /settlements
```

Request

```json
{
    "groupId":1,
    "payerId":2,
    "receiverId":1,
    "amount":250
}
```

Business Rules

* Amount must be positive.
* Amount cannot exceed the outstanding balance.
* Both users must belong to the group.

Response

```json
{
    "message":"Settlement successful"
}
```

---

## Settlement History

```http
GET /settlements
```

Supports

```
Pagination

Date Filters

Group Filters
```

---

# Dashboard APIs

---

## Dashboard Summary

```http
GET /dashboard
```

Response

```json
{
    "totalGroups":5,
    "totalExpenses":42,
    "totalAmountSpent":25400,
    "pendingSettlements":4
}
```

---

## Monthly Summary

```http
GET /dashboard/monthly
```

Returns monthly expense statistics.

---

# Common Query Parameters

Pagination

```http
?page=0

&size=10
```

Sorting

```http
?sort=createdAt,desc
```

Searching

```http
?keyword=goa
```

Filtering

```http
?category=FOOD
```

Date Range

```http
?from=2026-01-01

&to=2026-01-31
```

---

# HTTP Status Codes

| Code | Meaning               |
| ---- | --------------------- |
| 200  | Success               |
| 201  | Created               |
| 204  | Deleted Successfully  |
| 400  | Validation Failed     |
| 401  | Unauthorized          |
| 403  | Forbidden             |
| 404  | Resource Not Found    |
| 409  | Conflict              |
| 500  | Internal Server Error |

---

# API Versioning

All endpoints use URL versioning.

```http
/api/v1
```

Future versions:

```http
/api/v2

/api/v3
```

---

# API Naming Conventions

* Use plural nouns for collections (`/groups`, `/expenses`).
* Use nested resources where ownership is clear (`/groups/{groupId}/members`).
* Use HTTP verbs through the request method (GET, POST, PUT, DELETE), not in the URL.
* Return appropriate HTTP status codes.
* Keep request and response payloads consistent across all endpoints.

---

# Future APIs

The following endpoints can be introduced in future releases:

```
POST /notifications

GET /notifications

POST /attachments

POST /expenses/{id}/receipt

POST /budgets

GET /analytics

POST /recurring-expenses

GET /reports/monthly

POST /payments
```

---

# API Design Summary

The API follows RESTful principles with:

* Consistent endpoint naming
* JWT-based authentication
* Standard request/response structure
* Proper HTTP status codes
* Pagination, sorting, filtering, and searching support
* Extensible versioning strategy
* Clear separation of modules

These API contracts are intended to serve as the implementation blueprint for the Spring Boot backend.

