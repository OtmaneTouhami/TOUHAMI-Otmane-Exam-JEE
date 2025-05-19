import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-credit-management',
  standalone: true,
  imports: [CommonModule],
  template: `<h2>Credit Management (Admin)</h2><p>View all credits, manage statuses, etc.</p>`
})
export class CreditManagementComponent {} 