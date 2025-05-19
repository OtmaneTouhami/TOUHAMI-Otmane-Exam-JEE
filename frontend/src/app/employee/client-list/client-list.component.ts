import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-employee-client-list',
  standalone: true,
  imports: [CommonModule],
  template: `<h2>Client List (Employee)</h2><p>View and manage assigned or all clients based on permissions.</p>`
})
export class EmployeeClientListComponent {} 