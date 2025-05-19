import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `<h2>Employee Dashboard</h2><p>Tasks and client interactions.</p>`
})
export class EmployeeDashboardComponent {} 