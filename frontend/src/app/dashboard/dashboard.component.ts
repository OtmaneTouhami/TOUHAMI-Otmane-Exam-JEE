import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <h2>Welcome to the Dashboard, {{ username }}!</h2>
      <p>Your roles: {{ roles.join(', ') }}</p>
      <p>This is a generic dashboard page. Content can be customized based on your role.</p>
      <!-- Add role-specific content here using *ngIf -->
    </div>
  `,
  styles: [``]
})
export class DashboardComponent implements OnInit {
  username: string | null = null;
  roles: string[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.username = this.authService.getUserName();
    this.roles = this.authService.getRoles();
  }
} 