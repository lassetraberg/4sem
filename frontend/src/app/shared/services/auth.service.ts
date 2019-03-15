import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { tap } from "rxjs/operators";
import { Observable } from 'rxjs';

@Injectable()
export class AuthService {

    constructor(private http: HttpClient){

    }

    /**
     * 
     * @param username 
     * @param password 
     */
    login(username:string, password:string): Observable<any> {
        return this.http.post<string>(`${environment.api}/accounts/login`, {
            username,
            password
        }).pipe(
            tap(res => this.setSession(res))
        );
    }

    /**
     * 
     */
    logout(): void {
        localStorage.removeItem('token');
    }

    /**
     * 
     * @param result 
     */
    private setSession(result): void {
        localStorage.setItem('id_token', result.token);
        localStorage.setItem("expires_at", JSON.stringify(result.valueOf()) );
    }
    

}