# CustomerCare Platform

## About The Project

**CustomerCare** is a backend system for managing customer relationships, support ticketing, and order processing.
It separates public customer operations from internal system administration, with strict security isolation between
the two, and focuses on data integrity, clean architecture, and predictable API behavior.

###### ** The project is still under development and is not yet finished ! **

---

## 1. Snowflake ID Generator

Instead of relying on database-native identity sequences or traditional random UUIDs, this platform uses a custom
Java implementation of the **Snowflake ID Generation Algorithm**.

* Activated via a custom `@SnowflakeId` annotation placed directly on entity primary keys.
* Generates **64-bit, time-ordered, unique identifiers**, avoiding the lock bottlenecks of database-native
  auto-increment sequences.

###### (It is not currently fully polished and is still under development.)

---

## 2. Entities & Relationships

The system is built around 5 core entities:

* **`CUSTOMER`** — stores public customer profiles and authentication credentials.
* **`SYSTEM_USER`** — stores internal administrative/staff accounts. Strictly isolated from `CUSTOMER` to guarantee
  zero-risk privilege escalation (separate table, separate repository, separate authentication flow).
* **`ORDER`** — tracks a customer's purchase.
* **`ORDER_ITEM`** — individual line items that belong to an order.
* **`TICKET`** — a support request submitted by a customer.

**Relationships:**

* `CUSTOMER` → `ORDER` : **One-to-Many** (one customer can have many orders)
* `CUSTOMER` → `TICKET` : **One-to-Many** (one customer can have many tickets)
* `ORDER` → `ORDER_ITEM` : **One-to-Many** (one order can have many items)
* `SYSTEM_USER` : standalone — no relation to any other entity, by design, to keep admin accounts fully isolated.

---

## 3. Repository, Service & Business Logic

* **Tickets** carry a `status` (e.g. `OPEN`, `IN_PROGRESS`, ...) and a `priority` level, used to track and triage
  support requests.
* **Orders** carry a `status` (e.g. `PENDING`, `PROCESSING`, ...) representing where the order is in its lifecycle.
* Listing endpoints use a simple, deliberately minimal combination of **Pageable** (pagination) and
  **Specification** (dynamic filtering) — kept intentionally lightweight rather than over-engineered, to stay within
  the project's time constraints while still demonstrating the concept correctly.
* A small reusable helper class, **`SpecificationUtils`**, wraps common `Specification` building blocks (`equal`,
  `notEqual`, ...) to avoid repeating Criteria API boilerplate across services.
* All API responses are wrapped in a single unified **`ApiResponse`** structure, so every endpoint returns data in
  the same predictable shape (metadata + body) instead of each controller inventing its own response format.
* Each operation's possible outcomes (success and failure) are documented alongside their path, so the expected
  status code for a given endpoint is clear and consistent.

---

## 4. Exception Handling

A centralized exception handler tracks and catches errors across the application, rather than letting them leak out
as raw stack traces or inconsistent error shapes. Each caught exception is translated into a clean, generic message
for the client.

###### Hopefully nothing breaks — but if it does, it fails predictably instead of silently.

---

## 5. Controllers

Controllers are organized by resource, following REST URL conventions as closely as time allowed:

* Resource names are **plural nouns** (`/customers`, `/orders`, `/tickets`) across the board.
* Nesting is kept to a **maximum of 2 path parameters deep** wherever the resource hierarchy required it, to keep
  URLs readable and avoid over-nesting.
* Naming is kept consistent between the HTTP method (which expresses the action) and the resource name (which never
  contains a verb).

---

## 6. Security

The biggest challenge of this project, by far. Here's how authentication and authorization are structured:

```text
                         ┌──────────────────────┐
                         │     HTTP Request     │
                         │ Authorization: JWT   │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    JwtAuthFilter     │
                         └──────────┬───────────┘
                                    │
                            Extract from JWT
                         ┌──────────┴───────────┐
                         │                      │
                     username                  role
                         │                      │
                         └──────────┬───────────┘
                                    │
                                    ▼
                           ┌─────────────────┐
                           │   Which Role?   │
                           └────────┬────────┘
                                    │
                 ┌──────────────────┴──────────────────┐
                 │                                     │
                 ▼                                     ▼
          ┌──────────────┐                      ┌──────────────┐
          │    ADMIN     │                      │   CUSTOMER   │
          └──────┬───────┘                      └──────┬───────┘
                 │                                     │
                 ▼                                     ▼
       ┌──────────────────┐                   ┌──────────────────┐
       │SystemUserDetails │                   │ CustomerDetails  │
       │     Service      │                   │     Service      │
       └────────┬─────────┘                   └────────┬─────────┘
                │                                      │
                ▼                                      ▼
       ┌──────────────────┐                   ┌──────────────────┐
       │ System User      │                   │ Customer         │
       │ Repository / DB  │                   │ Repository / DB  │
       └────────┬─────────┘                   └────────┬─────────┘
                │                                      │
                └──────────────────┬───────────────────┘
                                   │
                                   ▼
                     ┌──────────────────────────┐
                     │   UserDetails loaded     │
                     │   + current authorities  │
                     └─────────────┬────────────┘
                                   │
                                   ▼
                     ┌──────────────────────────┐
                     │ Authentication created   │
                     │ UsernamePasswordToken    │
                     └─────────────┬────────────┘
                                   │
                                   ▼
                     ┌──────────────────────────┐
                     │     SecurityContext      │
                     └─────────────┬────────────┘
                                   │
                                   ▼
                     ┌──────────────────────────┐
                     │ Spring Authorization     │
                     │ @PreAuthorize / hasRole  │
                     └─────────────┬────────────┘
                                   │
                                   ▼
                              Controller
```

### Separation of concerns

Responsibilities are split cleanly from the ground up, starting at the `UserDetailsService` level: instead of one
shared implementation, there are **two** — `CustomerDetails` and `SystemUserDetails` — each implementing
`UserDetailsService` independently, each pointed at its own repository and its own table. Neither knows the other
exists.

Because both beans share the same type (`UserDetailsService`), **`@Qualifier`** is used wherever Spring needs to be
told exactly which one to inject — at the `AuthenticationManager` definitions and inside `JwtAuthFilter`.
**`@Primary`** is set on the customer-facing bean, since customers are the main, higher-traffic path through the
system, and a default is needed whenever two beans of the same type exist and no qualifier is given.

### Roles & account state

The system defines **3 roles**:

* **`USER`** — a newly registered account that is not yet activated; cannot use the system until an admin promotes
  it.
* **`CUSTOMER`** — an activated customer account with normal access.
* **`ADMIN`** — full system access, including managing other accounts.

Customer accounts also carry a timestamp field representing a **soft delete** — rather than being removed outright,
a "deleted" account is just marked with a deletion timestamp, blocking login and any further activity, while
preserving the underlying data.

---

## Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.3.4
* **Build Tool:** Maven

---

## A Note on AI Usage

AI assistance was used explicitly throughout this project — for studying concepts, verifying information, drafting
requests, and running experiments to understand behavior before implementing it. All resulting decisions and code
were reviewed and understood before being applied.

Hope you like it!