# CustomerCare Platform

## About The Project
**CustomerCare** is a robust backend system designed to handle customer relationship management, support ticketing, and order processing workflows seamlessly. Built with high performance, data integrity, and strict security isolation in mind, it separates public customer operations from internal system administration.

###### ** The project is still under development and is not yet finished ! **

---

## Key Features & Highlights

### Custom `@SnowflakeId` Generator
Instead of relying on database-native identity sequences or traditional random UUIDs, this platform utilizes a custom Java implementation of the **Snowflake ID Generation Algorithm**.
* Activated seamlessly via a custom `@SnowflakeId` annotation on entity primary keys.
* Generates **64-bit, time-ordered, unique identifiers** distributed across systems without database lock bottlenecks.
    ###### (It is not currently ready and is under development.)

### Strictly Decoupled Authentication & Security
To eliminate security escalation risks, system users (Admins/Staff) and public Customers are completely separated into distinct database entities and tables.

### Optimized Batch Order & Items Processing
Orders are created cleanly with a `PENDING` state and filled in batches. Total amounts are calculated dynamically, and state changes to `PROCESSING` atomically.

---

## Database Entities & Architecture

Below is the high-level schema structure designed for maximum query efficiency and normalization:

* **`CUSTOMER`** `(id, name, username, password, email, phone, created_at, last_login, role)`
    * *Stores public customer profiles and authentication credentials.*

* **`SYSTEM_USER`** `(id, name, username, password, email, phone, created_at, last_login, role)`
    * *Stores internal administrative/staff accounts. Strictly isolated from `CUSTOMER` to guarantee zero-risk privilege escalation.*

* **`ORDERS`** `(id, customer_id, status, total, created_at)`
    * *Tracks purchasing transactions. Has a `Many-to-One` relation with `CUSTOMER` and manages child items via `CascadeType.ALL` and `orphanRemoval`.*

* **`ORDER_ITEMS`** `(id, order_id, product_name, quantity, unit_price)`
    * *Line items assigned to a specific order. Utilizes high-precision `BigDecimal` types for safe financial calculations.*

* **`TICKETS`** `(id, customer_id, subject, status, priority, created_at)`
    * *Manages support requests submitted by customers with tracking status (`OPEN`, `IN_PROGRESS`, etc.) and priority levels.*

---

## Tech Stack & Core Libraries

* **Language:** Java 17
* **Framework:** Spring Boot / Spring Data JPA (Hibernate)
* **ID Generation:** Custom `@SnowflakeId` (64-bit distributed ID algorithm)
* **Validation:** Jakarta Bean Validation (`@NotNull`, `@NotBlank`,`@Email`)
* **Database:** Main(H2) / PostgreSQL / MySQL  

---

## Workflow Summary
1. **Creation:** Customer submits an empty order (Initial status: `PENDING`).
2. **Item Batching:** Support staff or system populates items into `ORDER_ITEMS`.
3. **Processing:** System automatically recalculates order `TOTAL` using `BigDecimal`, updates order state to `PROCESSING`, and saves state atomically.