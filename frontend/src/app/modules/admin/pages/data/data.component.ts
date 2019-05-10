import {
  Component,
  OnInit,
  ViewChild,
  ViewChildren,
  QueryList,
  ElementRef,
  ViewContainerRef
} from "@angular/core";
import { FormsModule } from "@angular/forms";
import { VehicleDataComponent } from "src/app/shared/components/vehicle-data/vehicle-data.component";
import { DataService } from "src/app/shared/services/data.service";
import { VehicleData } from "src/app/shared/models/vehicledata";
import { MapComponent } from "src/app/shared/components/map/map.component";
import { GPS } from "src/app/shared/models/gps";

@Component({
  selector: "app-data",
  templateUrl: "./data.component.html",
  styleUrls: ["./data.component.scss"]
})
export class DataComponent implements OnInit {
  vehicleData: any = {};
  tableColumns: string[] = [
    "timestamp",
    "gps",
    "velocity",
    "speedLimit",
    "acceleration"
  ];

  constructor(private data: DataService) {}

  ngOnInit() {
    this.getVehicleData();
  }

  private getVehicleData() {
    this.data
      .getAllData()
      .toPromise()
      .then(vehicleData => {
        this.vehicleData = vehicleData;
      });
  }
}
