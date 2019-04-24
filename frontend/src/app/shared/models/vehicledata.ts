import { GPS } from './gps';
import { Subject } from 'rxjs';

export class VehicleData {
    velocity: number = null;
    acceleration: number = null;
    gps: GPS = { lat: '0', lon: '0' };

    constructor() {}

}
