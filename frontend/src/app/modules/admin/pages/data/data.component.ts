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
    this.maps.forEach(map => console.log(map));
    if (data.length > 0) {
      this.showMap[data[0].deviceId] = true;
    }
    const loop = i => {
      if (!this.showMap[data[0].deviceId]) return;
      if (i === data.length - 1) return;
      const currentTimestamp = new Date(data[i].timestamp);
      const nextTimestamp = new Date(data[i + 1].timestamp);
      const diffInMS =
        (nextTimestamp.getTime() - currentTimestamp.getTime()) /
        this.speedModifierEnum[this.speedModifier[data[i].deviceId]];
      const map = this.maps.first;

      if (map) {
        this.updateMap(map, data[i], i);
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

  private updateMap(map: MapComponent, dataRow: VehicleData, index: number) {
    map.updateMap(dataRow.gps);
    const velocityElem = document.getElementById(
      `${dataRow.deviceId}-velocity`
    );
    const speedLimitElem = document.getElementById(
      `${dataRow.deviceId}-speedLimit`
    );
    const recordElem = document.getElementById(
      `${dataRow.deviceId}-recordIndex`
    );
    velocityElem.innerText = " " + dataRow.velocity + " ";
    speedLimitElem.innerText = " " + dataRow.speedLimit + " ";
    recordElem.innerText = " " + index + " ";

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
          return results;
        }, {});
      });
  }
}
