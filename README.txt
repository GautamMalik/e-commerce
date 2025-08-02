# E-Commerce Backend Microservices

This project is a microservices-based e-commerce backend system implemented using Spring Boot. It includes essential services such as User, Product, Order, Payment, and Notification. It uses Eureka for service discovery and an API Gateway for unified routing.

---

## 🔧 Technologies & Tools

- **Spring Boot** (Microservices Framework)
- **Spring Cloud Gateway** (API Gateway)
- **Eureka Server** (Service Discovery)
- **Resilience4j** (Rate Limiting & Circuit Breaking)
- **Load Balancing**
- **Factory & Strategy Design Patterns**
- **Java 17+**

---

## 🧩 Microservices Overview

### 1. **API Gateway**
- **Port**: `9001`
- Routes traffic to respective microservices

### 2. **Eureka Server**
- Central registry for service discovery
- All microservices register themselves here

---

## 🔐 User Microservice

Handles user-related operations and token validation.

### APIs:
- `POST /user/register` – Register new user
- `POST /user/login` – Authenticate user, returns **mocked session token** [mandatory in every api as 'token' parameter]
- `POST /user/logout` – Logs out user
- `GET /user/is-valid-token` – Validates token (for internal use)

### Rate Limiter:
- Allowed Requests: `limit-for-period: 5`
  - Only 5 requests are permitted within each 30-second window.
- Wait Time: `timeout-duration: 3s`
  - If the request limit is reached, additional requests will wait up to 3 seconds for a slot.
  - If no slot becomes available within that time, the request is rejected.

---

## 🔐 Product Microservice

Manages inventory and cart functionality.

### Inventory APIs:
- `GET /product/inventory` – View available products
- `DELETE /product/inventory/{productId}` – Delete product from inventory

### Cart APIs:
- `POST /product/cart/add` – Add product to cart
- `DELETE /product/cart/clear` – Clear entire cart

- Quantity in cart cannot exceed available inventory.

### Circuit Breaker:
- `registerHealthIndicator`: `true`
  - Exposes the circuit breaker's health status via Spring Boot Actuator (`/actuator/health`).
- `failureRateThreshold`: `50`
  - The circuit breaker opens if 50% or more of the calls fail.
- `minimumNumberOfCalls`: `3`
  - Circuit breaker starts evaluating failure rate only after 3 calls have been made.
- `waitDurationInOpenState`: `4s`
  - Once opened, the circuit breaker stays open for 4 seconds before transitioning to a half-open state to test if the service has recovered.

---

## 📦 Order Microservice

Processes and manages orders.

### APIs:
- `POST /order/process` – Move cart to pending orders
- `GET /order` – View all orders
- `POST /order/place` – Finalize order (triggers payment and notification)
- `POST /order/cancel` – Cancel order (triggers cancellation notification)

---

## 💳 Payment Microservice

Processes payments with support for multiple methods and currencies.

### API:
- `POST /payment/process` – Triggered internally during order placement

### Supported Payment Types:
- UPI
- Credit Card
- Debit Card
- Wallet
- Net Banking

### Supported Currencies:
- INR
- USD
- EUR
- GBP
- AUD

### Circuit Breaker:
- `registerHealthIndicator`: `true`
  - Exposes the circuit breaker's health status via Spring Boot Actuator (`/actuator/health`).
- `failureRateThreshold`: `50`
  - The circuit breaker opens if 50% or more of the calls fail.
- `minimumNumberOfCalls`: `3`
  - Circuit breaker starts evaluating failure rate only after 3 calls have been made.
- `waitDurationInOpenState`: `4s`
  - Once opened, the circuit breaker stays open for 4 seconds before transitioning to a half-open state to test if the service has recovered.

---

## 📢 Notification Microservice

### API:
- `POST /notification/send` – Logs message to console (called internally by Order Service)

---

## 🧱 Design Patterns Used

- **Factory Pattern** – Used in Inventory for predefined product creation
- **Strategy Pattern** – Used in Payment Service to process different payment methods

---

## ⚖️ Load Balancing

- All REST calls between microservices use `@LoadBalanced RestTemplate`
- Eureka + Ribbon (or Spring LoadBalancer) used for client-side load balancing

---

## 🚀 How to Run

1. Start Eureka Server
2. Start API Gateway
3. Start microservices in any order:
   - User Service
   - Product Service
   - Order Service
   - Payment Service
   - Notification Service
4. Access eureka via: [http://localhost:9001](http://localhost:9001)

---

## 📌 Notes

- All APIs (except login/register) require a valid token query parameter
- Internal APIs like `is-valid-token` are used by gateway or other services
- Notification system currently logs to console only

---

## 📫 Contact

For questions or improvements, feel free to open issues or contribute.
