import {
  Component,
  OnInit,
  ViewChildren,
  QueryList,
  Input,
  OnChanges,
  SimpleChanges
} from "@angular/core";
import { MapComponent } from "../map/map.component";
import { DataService } from "../../services/data.service";
import { VehicleData } from "../../models/vehicledata";
import { GPS } from "../../models/gps";

@Component({
  selector: "app-vehicle-data",
  templateUrl: "./vehicle-data.component.html",
  styleUrls: ["./vehicle-data.component.scss"]
})
export class VehicleDataComponent implements OnChanges {
  @Input("vehicleData") vehicleDataInput: any = {};
  vehicleData: any = {};
  tableColumns: string[] = [
    "timestamp",
    "gps",
    "velocity",
    "speedLimit",
    "acceleration"
  ];
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

  constructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.vehicleDataInput.currentValue) {
      this.vehicleData = changes.vehicleDataInput.currentValue;
      this.setupData();
    }
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

  private setupData() {
    this.vehicleData = this.vehicleDataInput.reduce((results, curr) => {
      (results[curr.deviceId] = results[curr.deviceId] || []).push(curr);
      this.showMap[curr.deviceId] = false;
      this.speedModifier[curr.deviceId] = "realtime";
      this.index[curr.deviceId] = 0;
      return results;
    }, {});
  }
}
