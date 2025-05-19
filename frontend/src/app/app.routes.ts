import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';

// Admin components
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { ClientManagementComponent as AdminClientManagementComponent } from './admin/client-management/client-management.component';
import { CreditManagementComponent as AdminCreditManagementComponent } from './admin/credit-management/credit-management.component';

// Employee components
import { EmployeeDashboardComponent } from './employee/employee-dashboard/employee-dashboard.component';
import { EmployeeClientListComponent } from './employee/client-list/client-list.component';
import { EmployeeCreditTasksComponent } from './employee/credit-tasks/credit-tasks.component';

// Client components
import { ClientDashboardComponent } from './client/client-dashboard/client-dashboard.component';
import { ApplyCreditComponent } from './client/apply-credit/apply-credit.component';
import { MyCreditsComponent } from './client/my-credits/my-credits.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'admin',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_ADMIN'] },
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'clients', component: AdminClientManagementComponent },
      { path: 'credits', component: AdminCreditManagementComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  {
    path: 'employee',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_EMPLOYE'] },
    children: [
      { path: 'dashboard', component: EmployeeDashboardComponent }, // Or use generic DashboardComponent
      { path: 'clients', component: EmployeeClientListComponent },
      { path: 'credits', component: EmployeeCreditTasksComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  {
    path: 'client',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_CLIENT'] },
    children: [
      { path: 'dashboard', component: ClientDashboardComponent }, // Or use generic DashboardComponent
      { path: 'apply-credit', component: ApplyCreditComponent },
      { path: 'my-credits', component: MyCreditsComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  // Generic dashboard for any authenticated user, if not covered by role-specific dashboards
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
  },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }, // Default route for authenticated users
  { path: '**', redirectTo: '/login' } // Wildcard route for a 404 or redirect to login
];
