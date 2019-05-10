import { Component, OnInit } from "@angular/core";
import { AuthService } from "src/app/shared/services/auth.service";
import { VehicleDataComponent } from "src/app/shared/components/vehicle-data/vehicle-data.component";

import { CONTENT_URL_PREFIX } from "src/app/shared/services/document.service";
import { DataService } from "src/app/shared/services/data.service";
import { Vehicle } from "src/app/shared/models/vehicle";
import { Router } from "@angular/router";
import { VehicleData } from "src/app/shared/models/vehicledata";

@Component({
  selector: "app-user",
  templateUrl: "./user.component.html",
  styleUrls: ["./user.component.scss"]
})
export class UserComponent implements OnInit {
  vehiclesPath: string = CONTENT_URL_PREFIX + "img/vehicles";
  vehicleIsSelected: boolean = false;
  selectedLicensePlate: string = null;

  myVehicle: Vehicle = null;
  vehicles: Array<Vehicle> = [];

  vehicleDataArray: Array<VehicleData> = [];

  constructor(
    private auth: AuthService,
    private data: DataService,
    private router: Router
  ) {}

  async ngOnInit() {
    await this.getVehicles();
    await this.getVehicleData();

    if (this.vehicles.length === 0) {
      this.data.unsetMyVehicle();
    }

    await this.setMyVehicle();
    if (this.myVehicle === null) {
      this.toggleListOfVehicles();
    }
  }

  /**
   * Sets the checked value of a given vehicle to true.
   * @param licensePlate id of the vehicle.
   */
  public selectVehicle(licensePlate: string): void {
    this.selectedLicensePlate = licensePlate;
    var radioButton = document.getElementById(
      "radio_" + licensePlate
    ) as HTMLInputElement;
    if (radioButton.checked) {
      radioButton.checked = false;
      this.vehicleIsSelected = false;
    } else {
      radioButton.checked = true;
      this.vehicleIsSelected = true;
    }
  }

  public async deleteVehicle(deviceId: string) {
    event.stopPropagation();
    const myVehicle = this.data.getMyVehicle();
    if (myVehicle && myVehicle.deviceId === deviceId) {
      this.data.unsetMyVehicle();
      this.setMyVehicle();
    }
    await this.data.deleteVehicle(deviceId).toPromise();
    this.getVehicles();
  }

  public showMyVehicles(): void {
    this.toggleListOfVehicles();
  }

  public signOut() {
    this.auth.logout();
    this.router.navigate(["auth/signin"]);
  }

  /**
   * Toggles the expansion the list of vehicles.
   */
  private toggleListOfVehicles() {
    document.getElementById("vehicles_list").classList.toggle("expanded");
  }

  private setVehicle() {
    const vehicle = this.vehicles.find(
      v => v.licensePlate === this.selectedLicensePlate
    );
    this.data.setMyVehicle(vehicle);
    this.toggleListOfVehicles();
    this.setMyVehicle();
  }

  private async setMyVehicle() {
    this.myVehicle = await this.data.getMyVehicle();
  }

  private async getVehicles() {
    const vehicles: Array<Vehicle> = await this.data.getVehicles().toPromise();
    vehicles.forEach(async vehicle => {
      const plateData = await this.data
        .getPlateData(vehicle.licensePlate)
        .toPromise();
      vehicle.brand = plateData.make;
      vehicle.model = plateData.model;
      vehicle.type =
        this.data.vehicleTypeMapper[plateData.chassis_type] ||
        this.data.types[1];
    });

    this.vehicles = vehicles;
  }

  private async getVehicleData() {
    const theData = [];
    this.vehicles.forEach(async vehicle => {
      const data = await this.data.getData(vehicle.deviceId).toPromise();
      theData.push(...data);
      this.vehicleDataArray = theData;
    });
  }
}
