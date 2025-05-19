import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'; // Adjusted path
import { ClientDTO } from '../dtos/client.dto'; // Assuming DTOs will be in an 'app/dtos' folder
import { CreditDTO } from '../dtos/credit.dto'; // Added import for CreditDTO

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = environment.backendHost + '/api/clients';

  constructor(private http: HttpClient) { }

  // Method to get HttpHeaders with Authorization if needed (handled by interceptor)
  // private getAuthHeaders(): HttpHeaders {
  //   const token = localStorage.getItem('access-token'); // Or from AuthService
  //   return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  // }

  getAllClients(): Observable<ClientDTO[]> {
    return this.http.get<ClientDTO[]>(this.apiUrl);
  }

  getClientById(id: number): Observable<ClientDTO> {
    return this.http.get<ClientDTO>(`${this.apiUrl}/${id}`);
  }

  createClient(client: ClientDTO): Observable<ClientDTO> {
    return this.http.post<ClientDTO>(this.apiUrl, client);
  }

  updateClient(id: number, client: ClientDTO): Observable<ClientDTO> {
    return this.http.put<ClientDTO>(`${this.apiUrl}/${id}`, client);
  }

  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchClients(keyword: string): Observable<ClientDTO[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<ClientDTO[]>(`${this.apiUrl}/search`, { params });
  }

  getCreditsByClient(clientId: number): Observable<CreditDTO[]> { // Changed 'any[]' to 'CreditDTO[]'
    return this.http.get<CreditDTO[]>(`${this.apiUrl}/${clientId}/credits`);
  }
} 