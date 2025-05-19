import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CreditService } from '../../services/credit.service';
import { AuthService } from '../../services/auth.service';
import { CreditDTO, StatutCredit, TypeBienFinance } from '../../dtos/credit.dto';

@Component({
  selector: 'app-apply-credit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './apply-credit.component.html',
  styleUrls: ['./apply-credit.component.css']
})
export class ApplyCreditComponent implements OnInit {
  creditForm!: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;
  // Enums for template
  typeBienFinanceValues = Object.values(TypeBienFinance);

  // To get clientId - this needs to be robustly fetched, e.g. from JWT or a profile call
  // For now, assuming it might be part of authService or needs a separate profile service call
  // This is a placeholder, actual clientId retrieval will be needed.
  private clientId: number | null = null; 

  constructor(
    private fb: FormBuilder,
    private creditService: CreditService,
    private authService: AuthService, // To get client ID eventually
    private router: Router
  ) {}

  ngOnInit(): void {
    this.creditForm = this.fb.group({
      montant: ['', [Validators.required, Validators.min(1)]],
      duree: ['', [Validators.required, Validators.min(1)]],
      tauxInteret: [{ value: 0.05, disabled: true }, [Validators.required]], // Example: fixed or admin-set
      type: ['', Validators.required], // Credit Type: PERSONNEL, IMMOBILIER, PROFESSIONNEL
      // Conditional fields
      motif: [''],
      typeBienFinance: [''],
      raisonSocialeEntreprise: ['']
    });

    // Example logic to get client ID (replace with actual implementation)
    // This is highly dependent on how your user profile/JWT is structured.
    // For now, let's simulate it or assume authService can provide it directly.
    // const userProfile = this.authService.getCurrentUserProfile(); // Fictional method
    // if (userProfile && userProfile.clientId) { 
    //  this.clientId = userProfile.clientId; 
    // }
    // For the purpose of this example, if you store username as 'client' (from InMemoryUserDetailsManager)
    // and your actual client entities have an ID that needs to be fetched based on username/email:
    // You would typically fetch the full ClientDTO via a service method in ngOnInit here if needed.
    // For this form, we only need `clientId` for the CreditDTO.
    // Let's assume for now the backend links the credit to the authenticated user.
    // If client ID MUST be part of the DTO from frontend, it needs to be fetched.
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.creditForm.invalid) {
      this.errorMessage = 'Please fill in all required fields correctly.';
      return;
    }

    this.isLoading = true;

    // const loggedInUser = this.authService.getUserName(); // or some ID
    // For now, we assume backend associates the credit with the logged-in user or clientId is optional/derived.
    // If your CreditDTO *requires* clientId to be set by frontend, you must fetch it first.
    // This is a common challenge: mapping JWT principal to database entity ID.

    const formValue = this.creditForm.getRawValue();

    const creditData: Partial<CreditDTO> = {
      montant: formValue.montant,
      duree: formValue.duree,
      tauxInteret: formValue.tauxInteret, // Assuming it's fixed or comes from a source
      type: formValue.type,
      dateDemande: new Date().toISOString().split('T')[0], // Current date as YYYY-MM-DD
      statut: StatutCredit.EN_COURS,
      // clientId: this.clientId, // Set this if you have it and it's required
      motif: formValue.motif || undefined,
      typeBienFinance: formValue.typeBienFinance || undefined,
      raisonSocialeEntreprise: formValue.raisonSocialeEntreprise || undefined
    };

    this.creditService.createCredit(creditData as CreditDTO).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = `Credit application submitted successfully! Credit ID: ${response.id}`;
        this.creditForm.reset();
        // Optionally, navigate to a 'my credits' page
        // this.router.navigate(['/client/my-credits']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to submit credit application. ' + (err.error?.message || err.message);
        console.error(err);
      }
    });
  }

  get type() { return this.creditForm.get('type'); }
} 