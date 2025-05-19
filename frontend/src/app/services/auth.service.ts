import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _isAuthenticated: boolean = false;
  private _userName: string | null = null;
  private _roles: string[] = [];
  private _accessToken: string | null = null;

  constructor(private http: HttpClient, private router: Router) {
    this.loadTokenFromLocalStorage();
  }

  public login(username: string, password: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post<{ 'access-token': string }>(environment.backendHost + '/auth/login', params).pipe(
      tap(response => {
        this.processLoginResponse(response);
      })
    );
  }

  private processLoginResponse(response: { 'access-token': string }): void {
    if (response && response['access-token']) {
      this._accessToken = response['access-token'];
      localStorage.setItem('access-token', this._accessToken);
      try {
        const decodedJwt: any = jwtDecode(this._accessToken);
        this._userName = decodedJwt.sub;
        const scope = decodedJwt.scope;
        if (typeof scope === 'string') {
          this._roles = scope.split(' ').filter(role => role.trim().length > 0);
        } else if (Array.isArray(scope)) {
          this._roles = scope;
        } else {
          this._roles = [];
        }
        this._isAuthenticated = true;
        console.log('User authenticated:', this._userName, 'Roles:', this._roles);
      } catch (error) {
        console.error('Error decoding JWT:', error);
        this.clearAuthData();
      }
    } else {
      this.clearAuthData();
    }
  }

  public loadProfile(tokenContainer: { 'access-token': string }): void {
    this.processLoginResponse(tokenContainer);
  }

  public logout(): void {
    this.clearAuthData();
    this.router.navigateByUrl('/login');
  }

  private clearAuthData(): void {
    this._isAuthenticated = false;
    this._userName = null;
    this._roles = [];
    this._accessToken = null;
    localStorage.removeItem('access-token');
  }

  public loadTokenFromLocalStorage(): void {
    const token = localStorage.getItem('access-token');
    if (token) {
      this.processLoginResponse({ 'access-token': token });
    } else {
      this.clearAuthData();
    }
  }

  public isAuthenticated(): boolean {
    return this._isAuthenticated && !!this._accessToken;
  }

  public getRoles(): string[] {
    return [...this._roles];
  }

  public getUserName(): string | null {
    return this._userName;
  }

  public getAccessToken(): string | null {
    return this._accessToken;
  }
}
