import { Component, OnInit, ViewChild } from "@angular/core";
import { DataService } from "src/app/shared/services/data.service";
import { VehicleData } from "src/app/shared/models/vehicledata";
import { MapComponent } from "src/app/shared/components/map/map.component";

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
  @ViewChild(MapComponent) map;
  showMap: any = {};

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

  replayData(data: Array<VehicleData>) {
    if (data.length > 0) {
      this.showMap[data[0].deviceId] = true;
    }
    const loop = i => {
      if (!this.showMap[data[0].deviceId]) return;
      if (i === data.length - 1) return;
      const currentTimestamp = new Date(data[i].timestamp);
      const nextTimestamp = new Date(data[i + 1].timestamp);
      const diffInMS = nextTimestamp.getTime() - currentTimestamp.getTime();

      if (this.map) {
        this.map.updateMap(data[i].gps);
        const velocityElem = document.getElementById(
          `${data[i].deviceId}-velocity`
        );
        const speedLimitElem = document.getElementById(
          `${data[i].deviceId}-speedLimit`
        );
        velocityElem.innerText = " " + data[i].velocity + " ";
        speedLimitElem.innerText = " " + data[i].speedLimit + " ";

        if (data[i].velocity > data[i].speedLimit) {
          velocityElem.classList.add("speeding");
        } else {
          velocityElem.classList.remove("speeding");
        }
      }

      setTimeout(() => {
        loop(i + 1);
      }, diffInMS);
    };
    loop(0);
  }

  stopReplay(data: Array<VehicleData>) {
    if (data.length > 0) {
      this.showMap[data[0].deviceId] = false;
    }
  }

  private getVehicleData() {
    this.data
      .getAllData()
      .toPromise()
      .then(vehicleData => {
        this.vehicleData = vehicleData.reduce((results, curr) => {
          (results[curr.deviceId] = results[curr.deviceId] || []).push(curr);
          this.showMap[curr.deviceId] = false;
          return results;
        }, {});
      });
  }
}
