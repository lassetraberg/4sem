import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';

import { CONTENT_URL_PREFIX } from 'src/app/shared/services/document.service';
import { DataService } from 'src/app/shared/services/data.service';
import { Vehicle } from 'src/app/shared/models/vehicle';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  vehiclesPath: string = CONTENT_URL_PREFIX + 'img/vehicles';
  vehicleIsSelected: boolean = false;
  selectedLicensePlate: string = null;

  myVehicle: Vehicle = null;

  vehicles: Array<Vehicle> = [];

  constructor(private auth: AuthService, private data: DataService) { }

  ngOnInit() {
    // Load users vehicles into vehicles array.
    this.getVehicles()

    if (this.vehicles.length === 0) {
      this.data.unsetMyVehicle()
    }

    this.setMyVehicle();
    if (this.myVehicle === null){
      this.toggleListOfVehicles()
    }
  }

  /**
   * Sets the checked value of a given vehicle to true.
   * @param licensePlate id of the vehicle.
   */
  public selectVehicle(licensePlate: string): void{
    this.selectedLicensePlate = licensePlate
    var radioButton = document.getElementById("radio_" + licensePlate) as HTMLInputElement;
    if(radioButton.checked){
      radioButton.checked = false;
      this.vehicleIsSelected = false;
    } else {
      radioButton.checked = true;
      this.vehicleIsSelected = true;
    }
  }

  public async deleteVehicle(deviceId: string) {
    event.stopPropagation();
    const myVehicle = this.data.getMyVehicle()
    if (myVehicle && myVehicle.deviceId === deviceId) {
      this.data.unsetMyVehicle()
      this.setMyVehicle();
    }
    await this.data.deleteVehicle(deviceId).toPromise();
    this.getVehicles();
  }


  public showMyVehicles(): void{
    this.toggleListOfVehicles();
  }

  public signOut(){
    this.auth.logout();
  }

  /**
   * Toggles the expansion the list of vehicles.
   */
  private toggleListOfVehicles(){
    document.getElementById("vehicles_list").classList.toggle("expanded");
  }

  private setVehicle(){
    const vehicle = this.vehicles.find(v => v.licensePlate === this.selectedLicensePlate)
    this.data.setMyVehicle(vehicle)
    this.toggleListOfVehicles();
    this.setMyVehicle()
  }

  private async setMyVehicle() {
    this.myVehicle = await this.data.getMyVehicle()
  }

  private async getVehicles() {
    this.data.getVehicles().toPromise()
    .then(vehicles => {
      vehicles.forEach(async vehicle => {
        const plateData = await this.data.getPlateData(vehicle.licensePlate).toPromise()
        vehicle.brand = plateData.make;
        vehicle.model = plateData.model;
        vehicle.type = this.data.vehicleTypeMapper[plateData.chassis_type] || this.data.types[1];
      })
      this.vehicles = vehicles;
    })
  }

}
