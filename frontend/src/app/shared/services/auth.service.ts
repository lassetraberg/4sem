import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { tap, delay } from "rxjs/operators";
import { Observable, empty, of } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json'
    })
};

@Injectable()
export class AuthService {

    readonly roles = ["AUTHENTICATED", "ADMIN"]

    constructor(private http: HttpClient){

    }

    /**
     * Register / sign up method.
     * @param username entered username
     * @param password entered password
     */
    register(username: string, password: string): Observable<any> {
        const credentials = {username, password};
        return this.http.post<string>(`${environment.restapi}/accounts`, credentials, httpOptions);
    }

    /**
     * JWT authentication signin method.
     * @param username credentials 
     * @param password credentials
     */
    login(username:string, password:string): Observable<any> {
        this.logout();
        return this.http.post<string>(`${environment.restapi}/accounts/login`, {
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

    getRole(): string {
        const userJson = localStorage.getItem("user")
        if (userJson) {
            return JSON.parse(userJson).role
        } else {
            return null
        }
    }

    /**
     * Assigns JWT and expiration to local storage.
     * @param result token retrieved from API.
     */
    private setSession(result): void {
        localStorage.setItem('id_token', result.token);
        localStorage.setItem("expires_at", result.tokenExpiresAt);
        localStorage.setItem("user", JSON.stringify(result.valueOf()))
    }

    /**
     * Checks if JWT exists in local storage.
     */
    public isAuthenticated(): boolean {
        const token = localStorage.getItem("id_token");
        return token !== null;
      }
    

}