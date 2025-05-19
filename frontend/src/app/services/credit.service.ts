import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CreditDTO, StatutCredit } from '../dtos/credit.dto';
import { RemboursementDTO } from '../dtos/remboursement.dto';

@Injectable({
  providedIn: 'root'
})
export class CreditService {
  private apiUrl = environment.backendHost + '/api/credits';
  private remboursementApiUrl = environment.backendHost + '/api/remboursements';

  constructor(private http: HttpClient) { }

  getAllCredits(): Observable<CreditDTO[]> {
    return this.http.get<CreditDTO[]>(this.apiUrl);
  }

  getCreditById(id: number): Observable<CreditDTO> {
    return this.http.get<CreditDTO>(`${this.apiUrl}/${id}`);
  }

  createCredit(credit: CreditDTO): Observable<CreditDTO> {
    return this.http.post<CreditDTO>(this.apiUrl, credit);
  }

  updateCredit(id: number, credit: CreditDTO): Observable<CreditDTO> {
    return this.http.put<CreditDTO>(`${this.apiUrl}/${id}`, credit);
  }

  deleteCredit(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  acceptCredit(id: number): Observable<CreditDTO> {
    return this.http.put<CreditDTO>(`${this.apiUrl}/${id}/accept`, {});
  }

  rejectCredit(id: number): Observable<CreditDTO> {
    return this.http.put<CreditDTO>(`${this.apiUrl}/${id}/reject`, {});
  }

  getCreditsByStatus(status: StatutCredit): Observable<CreditDTO[]> {
    return this.http.get<CreditDTO[]>(`${this.apiUrl}/status/${status}`);
  }

  getRemboursementsByCreditId(creditId: number): Observable<RemboursementDTO[]> {
    return this.http.get<RemboursementDTO[]>(`${this.apiUrl}/${creditId}/remboursements`);
  }

  getMyCredits(): Observable<CreditDTO[]> {
    return this.http.get<CreditDTO[]>(`${this.apiUrl}/my-credits`);
  }

  addRemboursement(remboursement: RemboursementDTO): Observable<RemboursementDTO> {
    return this.http.post<RemboursementDTO>(this.remboursementApiUrl, remboursement);
  }
} 