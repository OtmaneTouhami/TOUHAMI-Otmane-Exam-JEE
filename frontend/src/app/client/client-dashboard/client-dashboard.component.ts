import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container">
      <div class="card shadow-sm">
        <div class="card-body">
          <h2 class="card-title">Welcome, {{ username }}!</h2>
          <p class="card-text">This is your personal dashboard. Here you can manage your credits and personal information.</p>
          <hr>
          <div class="d-grid gap-2 d-md-flex justify-content-md-start">
            <a routerLink="/client/my-credits" class="btn btn-primary me-md-2 mb-2 mb-md-0">View My Credits</a>
            <a routerLink="/client/apply-credit" class="btn btn-success mb-2 mb-md-0">Apply for a New Credit</a>
            <!-- <a routerLink="/client/profile" class="btn btn-info">My Profile</a> -->
          </div>
        </div>
      </div>
    </div>
  `
})
export class ClientDashboardComponent implements OnInit {
  username: string | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.username = this.authService.getUserName();
  }
} 