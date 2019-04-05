import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json'
    })
};

@Injectable()
export class DataService {

    constructor(private http: HttpClient) { }

    public getVehicles(){
        return this.http.get(`${environment.restapi}/vehicle/`, httpOptions);
    } 


}