import { GPS } from "./gps";
import { Subject } from "rxjs";

export class VehicleData {
  deviceId: string = null;
  timestamp: string = null;
  velocity: number = null;
  acceleration: number = null;
  speedLimit: number = null;
  gps: GPS = { lat: "0", lon: "0" };

  constructor() {}
}
