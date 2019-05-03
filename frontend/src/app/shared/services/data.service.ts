import { Injectable } from "@angular/core";
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse
} from "@angular/common/http";
import { environment } from "src/environments/environment";
import { User } from "../models/user";
import { Observable } from "rxjs";
import { VehicleData } from "../models/vehicledata";

const httpOptions = {
  headers: new HttpHeaders({
    "Content-Type": "application/json"
  })
};

@Injectable()
export class DataService {
  constructor(private http: HttpClient) {}

  public getVehicles() {
    return this.http.get(`${environment.restapi}/vehicle/`, httpOptions);
  }

  public getUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(
      `${environment.restapi}/accounts`,
      httpOptions
    );
  }

  public unlockAccount(userId: number): Observable<any> {
    return this.http.post(
      `${environment.restapi}/accounts/unlock/${userId}`,
      null,
      httpOptions
    );
  }

  public getAllData(): Observable<Array<VehicleData>> {
    return this.http.get<Array<VehicleData>>(
      `${environment.restapi}/vehicle/all-data`,
      httpOptions
    );
  }
}
