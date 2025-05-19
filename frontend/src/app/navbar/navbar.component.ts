import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isAuthenticated: boolean = false;
  username: string | null = null;
  roles: string[] = [];

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.loadTokenFromLocalStorage(); // Ensure auth state is loaded
    this.isAuthenticated = this.authService.isAuthenticated();
    this.username = this.authService.getUserName();
    this.roles = this.authService.getRoles();

    // Subscribe to auth changes if you have an observable for that in AuthService
    // For simplicity, we're checking on init. For dynamic updates, an observable pattern is better.
  }

  hasRole(role: string): boolean {
    return this.authService.getRoles().includes(role);
  }

  handleLogout(): void {
    this.authService.logout();
    // State will be updated by authService, and router will navigate to /login
    // Re-check auth status for navbar display (or use an observable stream from authService)
    this.isAuthenticated = false;
    this.username = null;
    this.roles = [];
  }
} 