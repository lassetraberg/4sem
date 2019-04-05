import { GPS } from './gps';

export class VehicleData {
    velocity: number = null;
    acceleration: number = null;
    gps: GPS = {lat: '0', lon: '0'};
}
