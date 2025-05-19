import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-employee-credit-tasks',
  standalone: true,
  imports: [CommonModule],
  template: `<h2>Credit Tasks (Employee)</h2><p>Process credit applications, manage existing credits.</p>`
})
export class EmployeeCreditTasksComponent {} 