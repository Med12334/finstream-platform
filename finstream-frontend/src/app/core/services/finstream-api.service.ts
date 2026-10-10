import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, SettlementAuditLog } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class FinStreamApiService {
  private http = inject(HttpClient);

  private readonly orderApiUrl = 'http://localhost:8081/api/v1/orders';
  private readonly settlementApiUrl = 'http://localhost:8082/api/v1/settlements';

  // PostgreSQL operations
  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.orderApiUrl);
  }

  submitOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(this.orderApiUrl, order);
  }

  // IBM DB2 Archival Audit operations
  getSettlements(): Observable<SettlementAuditLog[]> {
    return this.http.get<SettlementAuditLog[]>(this.settlementApiUrl);
  }
}
