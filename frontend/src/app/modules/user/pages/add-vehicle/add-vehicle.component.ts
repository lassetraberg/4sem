import { Component, OnInit } from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators
} from "@angular/forms";
import { DataService } from "src/app/shared/services/data.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-add-vehicle",
  templateUrl: "./add-vehicle.component.html",
  styleUrls: ["./add-vehicle.component.scss"]
})
export class AddVehicleComponent implements OnInit {
  form: FormGroup;
  vehicle = { licensePlate: '', deviceId: '' };

  constructor(private data: DataService, private router: Router) {}

  ngOnInit() {
    this.form = new FormGroup({
      'licensePlate': new FormControl(this.vehicle.licensePlate, [
        Validators.required
      ]),
      'deviceId': new FormControl(this.vehicle.deviceId, [
        Validators.required,
        Validators.maxLength(36),
        Validators.minLength(36) 
      ])
    });
  }

  get licensePlate() {
    return this.form.get("licensePlate");
  }
  get deviceId() {
    return this.form.get("deviceID");
  }

  public async submit() {
    await this.data
      .registerVehicle(
        this.form.controls.licensePlate.value,
        this.form.controls.deviceId.value
      )
      .toPromise();
    this.router.navigate(["user/"]);
  }
}
