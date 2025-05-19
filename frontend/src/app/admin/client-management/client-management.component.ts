import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client-management',
  standalone: true,
  imports: [CommonModule],
  template: `<h2>Client Management (Admin)</h2><p>View, create, edit, delete clients.</p>`
})
export class ClientManagementComponent {} 