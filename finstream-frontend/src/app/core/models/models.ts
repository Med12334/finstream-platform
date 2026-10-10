export interface Order {
  orderId?: string;
  accountId: string;
  symbol: string;
  quantity: number;
  price: number;
  status?: string;
  createdAt?: string;
}

export interface SettlementAuditLog {
  auditId: string;
  orderId: string;
  accountId: string;
  settledAmount: number;
  executionFee: number;
  settlementStatus: string;
  kafkaOffset: number;
  kafkaPartition: number;
  settledAt: string;
}
