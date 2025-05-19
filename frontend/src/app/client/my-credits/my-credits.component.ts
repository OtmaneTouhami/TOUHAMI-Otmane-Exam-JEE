import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CreditService } from '../../services/credit.service';
import { CreditDTO } from '../../dtos/credit.dto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-credits',
  standalone: true,
  imports: [CommonModule, RouterModule, DatePipe],
  templateUrl: './my-credits.component.html',
  styleUrls: ['./my-credits.component.css']
})
export class MyCreditsComponent implements OnInit {
  credits: CreditDTO[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';
  username: string | null = null;

  constructor(
    private creditService: CreditService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUserName();
    this.loadCredits();
  }

  loadCredits(): void {
    this.isLoading = true;
    this.errorMessage = '';
    // Option 1: Use a dedicated endpoint (preferred if backend supports it)
    this.creditService.getMyCredits().subscribe({
      next: (data) => {
        this.credits = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load credits. ' + (err.error?.message || err.message);
        console.error(err);
        this.isLoading = false;
      }
    });

    // Option 2: If you need to get clientId first (e.g. from a profile call) and then use another service method.
    // This requires ClientService and potentially an updated AuthService to hold/fetch the client's DB ID.
    // const clientId = this.authService.getClientDbId(); // This method needs to exist in AuthService
    // if (clientId) {
    //   this.clientService.getCreditsByClient(clientId).subscribe({ ... });
    // } else {
    //   this.errorMessage = 'Could not determine client ID to fetch credits.';
    //   this.isLoading = false;
    // }
  }

  // Helper to get a badge class based on credit status
  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'ACCEPTE': return 'badge bg-success';
      case 'REJETE': return 'badge bg-danger';
      case 'EN_COURS': return 'badge bg-warning text-dark';
      default: return 'badge bg-secondary';
    }
  }
} 