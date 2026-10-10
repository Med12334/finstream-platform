# FinStream Architecture Overview

## Data Segregation Principles
1. **PostgreSQL (Operational Store):**
   - High-throughput OLTP writes for market orders (`orders` table).
   - Serves low-latency queries for user accounts and portfolio state.
   - Triggers outbound transactional events.

2. **Apache Kafka (Event Ingestion Layer):**
   - Topic `orders.inbound.v1`: Partitioned by `accountId` to guarantee ordering per account.
   - Resilient consumer groups with idempotent clearing handlers.
   - Dedicated Dead-Letter Queue (`orders.DLQ.v1`) for unprocessable messages.

3. **IBM DB2 (Compliance & Regulatory Ledger):**
   - High-durability ACID datastore holding immutable audit trails (`SETTLEMENT_AUDIT_LOG`).
   - Tracks exact Kafka topic partition offsets to maintain clear audit lineage.
   - Isolated from customer-facing query bursts to maintain reporting availability.
