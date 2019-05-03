import { Component, OnInit } from "@angular/core";
import { DataService } from "src/app/shared/services/data.service";

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

  formatDate(date: string): string {
    const d = new Date(date);
    return d.toLocaleString();
  }

  hasVehicleData() {
    return Object.keys(this.vehicleData).length > 0;
  }

  private getVehicleData() {
    this.data.getAllData().subscribe(vehicleData => {
      this.vehicleData = vehicleData.reduce((results, curr) => {
        (results[curr.deviceId] = results[curr.deviceId] || []).push(curr);
        return results;
      }, {});
    });
  }
}
