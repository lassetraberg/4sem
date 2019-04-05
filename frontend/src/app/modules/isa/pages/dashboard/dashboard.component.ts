import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { WebsocketService } from 'src/app/shared/services/websocket.service';
import { WebSocketSubject } from 'rxjs/webSocket';
import { VehicleData } from 'src/app/shared/models/vehicledata';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  connected: boolean = false;

  vehicleData: VehicleData;

  sockets = {};

  constructor(private socket: WebsocketService) { 
    this.vehicleData = new VehicleData();
  }

  ngOnInit() {
    // Connect to WebSockets for the properties of VehicleData.   
    Object.keys(this.vehicleData).forEach(key => {
      this.sockets[key] = this.socket.getSubject("2905d0e7-615c-455b-8807-ddd7665d3994", key);
      this.sockets[key].subscribe(
        msg => {
          this.vehicleData[key] = msg[key];
        }
      )
    });
  }

  ngOnDestroy(){
    console.log(this.vehicleData.velocity);
    console.log(this.vehicleData.acceleration);
    console.log(this.vehicleData.gps);
    // Disconnect from every WebSocket.
    Object.keys(this.sockets).forEach(property => {
      var subject = this.sockets[property] as WebSocketSubject<any>;
      subject.unsubscribe();
    })
  }

}
