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
import { LicensePlate } from '../models/licensePlate';
import { Vehicle } from '../models/vehicle';

const httpOptions = {
  headers: new HttpHeaders({
    "Content-Type": "application/json"
  })
};

@Injectable()
export class DataService {
  constructor(private http: HttpClient) {}

  public readonly types = ["mini", "sedan", "sport", "suv", "van"];
  public readonly vehicleTypeMapper = {
    'Hatchback': this.types[1]
  }

  public registerVehicle(licensePlate: string, deviceId: string) {
    const licensePlateFormatted = licensePlate.toUpperCase().replace(/\s/g, '')
    return this.http.post(
      `${environment.restapi}/vehicle`,
      { licensePlate: licensePlateFormatted, deviceId },
      httpOptions
    );
  }

  public getVehicles(): Observable<Array<any>> {
    return this.http.get<Array<any>>(`${environment.restapi}/vehicle/`, httpOptions);
  }

  public deleteVehicle(deviceId: string) {
    return this.http.delete(`${environment.restapi}/vehicle/${deviceId}`)
  }

  public getPlateData(licensePlate: string): Observable<LicensePlate> {
    return this.http.get<LicensePlate>(`${environment.restapi}/vehicle/licenseplate/${licensePlate}`)
  }

  public setMyVehicle(vehicle: Vehicle) {
    localStorage.setItem('myVehicle', JSON.stringify(vehicle))
  }

  public unsetMyVehicle() {
    localStorage.removeItem('myVehicle')
  }

  public getMyVehicle(): Vehicle | null {
    const vehicleJson = localStorage.getItem('myVehicle')
    if (vehicleJson) {
      return JSON.parse(vehicleJson)
    } else {
      return null
    }
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
