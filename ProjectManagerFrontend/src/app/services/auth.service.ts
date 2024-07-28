import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    const authData = btoa(`${username}:${password}`);
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(() => {
        localStorage.setItem('authToken', authData);
      })
    );
  }

  register(username: string, password: string, email: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, { username, password, email }).pipe(
      tap(() => {
        const authData = btoa(`${username}:${password}`);
        localStorage.setItem('authToken', authData);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('authToken');
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }
}
