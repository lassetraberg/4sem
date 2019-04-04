import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';

import { CONTENT_URL_PREFIX } from 'src/app/shared/services/document.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  vehiclesPath: string = CONTENT_URL_PREFIX + 'img/vehicles';
  vehicleIsSelected: boolean = false;

  myVehicle = {
    "licensePlate": "AZ92432",
    "type": "mini",
    "brand": "Toyota",
    "model": "Aygo"
  };

  vehicles = [
    {
    "licensePlate": "AZ92432",
    "type": "mini",
    "brand": "Toyota",
    "model": "Aygo"
    },
    {
      "licensePlate": "AZ92433",
      "type": "sedan",
      "brand": "Toyota",
      "model": "Aygo"
    },
    {
      "licensePlate": "AZ92434",
      "type": "sport",
      "brand": "Toyota",
      "model": "Aygo"
    },
    {
      "licensePlate": "AZ92435",
      "type": "suv",
      "brand": "Toyota",
      "model": "Aygo"
    },
    {
      "licensePlate": "AZ92436",
      "type": "van",
      "brand": "Toyota",
      "model": "Aygo"
    },
  ];

  constructor(private auth: AuthService) { }

  ngOnInit() {
    // Load users vehicles into vehicles array.
  }

  /**
   * Sets the checked value of a given vehicle to true.
   * @param licensePlate id of the vehicle.
   */
  public selectVehicle(licensePlate: string): void{
    var radioButton = document.getElementById("radio_" + licensePlate) as HTMLInputElement;
    if(radioButton.checked){
      radioButton.checked = false;
      this.vehicleIsSelected = false;
    } else {
      radioButton.checked = true;
      this.vehicleIsSelected = true;
    }
  }

  public deleteVehicle(licensePlate: string): void{
    event.stopPropagation();
    console.log("Deleted " + licensePlate);
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
    console.log("Vehicle set!");
    this.toggleListOfVehicles();
  }

  /**
   * Method 
   */
  private addVehicle(): void{
    console.log("Let's add a new vehicle");
  }

}
