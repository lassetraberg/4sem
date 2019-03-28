import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { tap, delay } from "rxjs/operators";
import { Observable, empty, of } from 'rxjs';

@Injectable()
export class AuthService {

    constructor(private http: HttpClient){

    }

    /**
     * JWT authentication signin method.
     * @param username credentials 
     * @param password credentials
     */
    login(username:string, password:string): Observable<any> {
        localStorage.setItem('id_token', 'token');
        localStorage.setItem('username', 'lassetraberg');
        return this.http.post<string>(`${environment.api}/accounts/login`, {
            username,
            password
        }).pipe(
            tap(res => this.setSession(res))
        );
    }

    /**
     * Remove JWT from local storage.
     */
    logout(): void {
        localStorage.removeItem('id_token');
    }

    /**
     * Assigns JWT and expiration to local storage.
     * @param result token retrieved from API.
     */
    private setSession(result): void {
        localStorage.setItem('id_token', result.token);
        localStorage.setItem("expires_at", JSON.stringify(result.valueOf()) );
    }

    /**
     * Checks if JWT exists in local storage.
     */
    public isAuthenticated(): boolean {
        const token = localStorage.getItem("id_token");
        return token !== null;
      }
    

}