import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { DashboardComponent } from './app/features/dashboard/dashboard.component';

bootstrapApplication(DashboardComponent, {
  providers: [
    provideHttpClient()
  ]
}).catch(err => console.error(err));
