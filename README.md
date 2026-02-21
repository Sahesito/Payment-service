## 💳 Payment Service – SmartCommerce
This module manages the full payment lifecycle inside SmartCommerce. It records transactions, controls payment states, enforces security rules, and guarantees transactional integrity — all in a stateless microservice environment.

## 🎯 Why This Service Exists
- Dedicated Payment Domain
  Payment logic is isolated from Order and User services to maintain clean domain boundaries.

- Controlled Payment Lifecycle
  Centralizes status management (PENDING, COMPLETED, FAILED, REFUNDED) to avoid inconsistent states.

- Secure Financial Operations
  Enforces strict role-based access with JWT authentication and method-level authorization.

- Traceable Transactions
  Generates unique transaction IDs (TXN-UUID) for auditing and traceability.

- Extensible Architecture
  Designed to integrate with a real payment gateway in production while currently simulating processing.

## 🔑 Core Capabilities
- Payment Management
Create payment (CLIENT, ADMIN)
Get payment by ID (CLIENT, ADMIN)
Get payments by order ID (CLIENT, ADMIN)
Get payments by user ID (CLIENT, ADMIN)
Get payments by status (ADMIN)
List all payments (ADMIN)
Update payment status (ADMIN)
Delete payment (ADMIN)

## 🔄 Payment Lifecycle
PENDING → COMPLETED
   ↓
 FAILED
   ↓
 REFUNDED

New payments start as PENDING.
Processing simulation automatically marks them as COMPLETED.
Errors mark them as FAILED.
Status updates are restricted to administrators.

## ⚙️ Payment Creation Flow
- Validate request payload.
- Create payment with status PENDING.
- Generate unique transaction ID.
- Persist to database.
- Simulate processing.
- Update status to COMPLETED (or FAILED on error).
- All wrapped inside transactional boundaries.

## 🔐 Security Model
- Stateless JWT authentication.
- Custom JwtAuthenticationFilter.
- Role extracted from JWT claim.
- Method-level access control via @PreAuthorize.
- CORS configured for Angular frontend (localhost:4200).
/actuator/** publicly accessible for monitoring.

| Endpoint          | Roles         |
| ----------------- | ------------- |
| Create Payment    | ADMIN, CLIENT |
| View Own Payments | ADMIN, CLIENT |
| Filter by Status  | ADMIN         |
| Update Status     | ADMIN         |
| Delete Payment    | ADMIN         |
| List All          | ADMIN         |
