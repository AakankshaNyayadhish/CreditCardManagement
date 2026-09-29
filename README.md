## Project Overview

The **Credit Card Management System** is a Java-based fintech application that enables banks to manage credit cards, customers, and card transactions. The system allows customers to apply for credit cards, make purchases, pay outstanding dues, and monitor their available credit limit.

The application maintains complete transaction history and stores all information in an Oracle database, ensuring data integrity and consistency.

The objective of this project is to help freshers gain hands-on experience in building enterprise applications using Oracle Database 26ai, Java SE 25, Spring Boot, REST APIs and Spring Data JPA.
# Functional Requirements

## 1. Customer Management (CUSTOMER db table)

The application shall provide the following operations:

* Register a new customer
* Update customer information
* Delete a customer
* Retrieve customer details by Customer ID
* Retrieve all customers

Each customer shall contain:

* Customer ID (Unique)
* Customer Name
* Email Address
* Mobile Number
* PAN Number

---

## 2. Credit Card Management (CREDIT_CARD DB table)

The application shall provide the following operations:

* Issue a new credit card
* Update credit card information
* Block or Unblock a credit card
* Retrieve credit card details
* Retrieve all credit cards

Each credit card shall contain:

* Card Number (Unique)
* Card Type (Silver / Gold / Platinum)
* Credit Limit
* Available Credit
* Outstanding Amount
* Expiry Date
* Card Status (ACTIVE / BLOCKED)

Each customer can own one or more credit cards.

---

## 3. Merchant Management (MERCHANT DB TABLE)

The application shall maintain merchant information.

Operations include:

* Add Merchant
* Update Merchant
* Delete Merchant
* Retrieve Merchant Details
* Retrieve All Merchants

Merchant details:

* Merchant ID
* Merchant Name
* Category
* Location

---

## 4. Purchase Transactions (TRANSACTIONS DB TABLE)

Customers can make purchases using their credit cards.

The system shall:

* Verify that the card is ACTIVE.
* Verify that sufficient available credit exists.
* Deduct the purchase amount from the available credit.
* Increase the outstanding amount.
* Record the transaction.

Each purchase transaction shall contain:

* Transaction ID
* Card Number
* Merchant ID
* Purchase Amount
* Transaction Date & Time
* Transaction Status

---

## 5. Bill Payment (BILLS DB TABLE)

Customers can pay their outstanding credit card balance.

The system shall support:

* Full Payment
* Partial Payment

The system shall:

* Reduce the outstanding amount.
* Increase the available credit.
* Record the payment transaction.

---

## 6. Transaction Management

Every financial activity shall generate a transaction record.

Supported transaction types:

* PURCHASE
* PAYMENT

Each transaction shall contain:

* Transaction ID
* Card Number
* Transaction Type
* Amount
* Merchant ID (for purchases)
* Transaction Date & Time
* Status (SUCCESS / FAILED)

---

## 7. Data Persistence

All application data shall be stored in Oracle Database.

Suggested tables:

* CUSTOMER
* CREDIT_CARD
* MERCHANT
* TRANSACTION

Proper primary key and foreign key relationships shall be maintained.

---

# Technical Requirements

### Programming Language

* Java SE 25

### Framework

* Spring Boot 3.x
* Spring Data JPA
* Spring Web

### Database

* Oracle Database 26ai

### Build Tool

* Maven

### Architecture

* Layered Architecture

  * Controller Layer
  * Service Layer
  * Repository Layer
  * Entity Layer

### Design Principles

* Object-Oriented Programming
* RESTful APIs
* Validation
* Exception Handling
* Transaction Management

---

# Business Rules

* Card number must be unique.
* A customer may own multiple credit cards.
* Purchases are allowed only on ACTIVE cards.
* Available credit must never become negative.
* Payment amount cannot exceed the outstanding balance.
* Every purchase and payment must be recorded.
* Blocked cards cannot perform transactions.
* Failed transactions must also be logged.

---

# Reports

1. Display all customer details.

2. Display all credit card details.

3. Display all merchant details.

4. Display complete transaction history.

5. Display customers with the highest outstanding balance.

6. Display customers with the lowest outstanding balance.

7. Display the merchant with the highest sales amount.

8. Display the merchant with the highest number of transactions.

9. Display the most frequently used credit card.

10. Display the least frequently used credit card.

11. Display total purchase amount for today.

12. Display total payment amount for today.

13. Display all blocked credit cards.

14. Display cards whose available credit is below 20% of the credit limit.

15. Display the customer who has spent the highest amount.

16. Display the customer who made the highest payment.

17. Display total outstanding amount across all customers.

18. Display average purchase transaction amount.

19. Display the largest purchase transaction.

20. Display the monthly spending summary of every customer.

---

# Bonus Features (Optional)

Participants may implement any of the following advanced features:

* Credit card statement generation
* Search transactions by date range
* Search transactions by merchant
* Search transactions by amount
* Reward points calculation
* Cashback offers
* Late payment penalty calculation
* Credit limit increase request
* Email notification simulation
* Swagger/OpenAPI documentation
* Unit testing using JUnit
* Dockerize the application
* Logging using SLF4J
