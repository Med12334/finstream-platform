import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FinStreamApiService } from '../../core/services/finstream-api.service';
import { Order, SettlementAuditLog } from '../../core/models/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  private api = inject(FinStreamApiService);

  // Angular Signals for Reactive State
  orders = signal<Order[]>([]);
  settlements = signal<SettlementAuditLog[]>([]);
  isSubmitting = signal<boolean>(false);

  newOrder: Order = {
    accountId: 'ACC-89421',
    symbol: 'AAPL',
    quantity: 100,
    price: 185.50
  };

  ngOnInit(): void {
    this.refreshAll();
  }

  refreshAll(): void {
    this.api.getOrders().subscribe({
      next: (data) => this.orders.set(data),
      error: () => console.log('Order API simulated/offline')
    });

    this.api.getSettlements().subscribe({
      next: (data) => this.settlements.set(data),
      error: () => console.log('Settlement API simulated/offline')
    });
  }

  executeOrder(): void {
    this.isSubmitting.set(true);
    this.api.submitOrder(this.newOrder).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        setTimeout(() => this.refreshAll(), 500);
      },
      error: () => {
        this.isSubmitting.set(false);
        // Optimistic UI fallback
        const mockOrder = { ...this.newOrder, orderId: 'ORD-' + Math.floor(Math.random()*10000), status: 'PENDING_EXECUTION' };
        this.orders.update(prev => [mockOrder, ...prev]);
      }
    });
  }
}
