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
  tableColumns: string[] = [
    "timestamp",
    "gps",
    "velocity",
    "speedLimit",
    "acceleration"
  ];
  vehicleData: any = {};
  showMap: any = {};
  speedModifier: any = {};
  index: any = {};
  speedModifierEnum = {
    slow: 0.5,
    realtime: 1,
    fast: 2,
    faster: 4
  };
  speedModifierEnumArr = Object.keys(this.speedModifierEnum);
  @ViewChildren(MapComponent) maps: QueryList<MapComponent>;

  constructor(private data: DataService) {}

  ngOnInit() {
    this.getVehicleData();
  }

  setSpeedModifier(deviceId: string, speed: string) {
    this.speedModifier[deviceId] = speed;
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
      const row = data[i];
      const nextRow = data[i + 1];
      const currentTimestamp = new Date(row.timestamp);
      const nextTimestamp = new Date(nextRow.timestamp);
      const diffInMS =
        (nextTimestamp.getTime() - currentTimestamp.getTime()) /
        this.speedModifierEnum[this.speedModifier[row.deviceId]];
      const map = this.maps.first;

      if (map) {
        this.updateMap(map, row);
      }

      this.index[row.deviceId] = this.index[row.deviceId] + 1;
      setTimeout(() => {
        loop(this.index[row.deviceId]);
      }, diffInMS);
    };
    loop(0);
  }

  stopReplay(data: Array<VehicleData>) {
    if (data.length > 0) {
      this.showMap[data[0].deviceId] = false;
      this.index[data[0].deviceId] = 0;
    }
  }

  createMapUrl(gps: GPS): string {
    return `https://www.openstreetmap.org/?mlat=${gps.lat}&mlon=${
      gps.lon
    }#map=18/${gps.lat}/${gps.lon}&layers=N`;
  }

  private updateMap(map: MapComponent, dataRow: VehicleData) {
    map.updateMap(dataRow.gps);
    const velocityElem = document.getElementById(
      `${dataRow.deviceId}-velocity`
    );
    const speedLimitElem = document.getElementById(
      `${dataRow.deviceId}-speedLimit`
    );
    velocityElem.innerText = " " + dataRow.velocity + " ";
    speedLimitElem.innerText = " " + dataRow.speedLimit + " ";

    if (dataRow.velocity > dataRow.speedLimit) {
      velocityElem.classList.add("speeding");
    } else {
      velocityElem.classList.remove("speeding");
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
          this.speedModifier[curr.deviceId] = "realtime";
          this.index[curr.deviceId] = 0;
          return results;
        }, {});
      });
  }
}
