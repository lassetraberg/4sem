import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { WebsocketService } from 'src/app/shared/services/websocket.service';
import { WebSocketSubject } from 'rxjs/webSocket';
import { VehicleData } from 'src/app/shared/models/vehicledata';
import { interval, Subscription, Observable, of, BehaviorSubject } from 'rxjs';
import { MapComponent } from 'src/app/shared/components/map/map.component';
import { DataService } from 'src/app/shared/services/data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  @ViewChild(MapComponent) map;

  connected: boolean = false;

  connecting: boolean = false;

  vehicleData: VehicleData;

  deviceId: string = null;

  sockets = {};

  constructor(private socket: WebsocketService, private data: DataService, private router: Router) { 
    this.vehicleData = new VehicleData();
  }

  ngOnInit() {
    // Connect to WebSockets for the properties of VehicleData.   
    const myVehicle = this.data.getMyVehicle()
    if (myVehicle === null) {
      this.router.navigate(["user/"]);
    } else {
      this.deviceId = myVehicle.deviceId;
    }

    this.subscribeToWebSockets();
  }

  ngAfterViewInit(){

  }

  subscribeToWebSockets(){
    this.connecting = true;
    this.unsubscribeWebSockets();
    Object.keys(this.vehicleData).forEach(key => {  
      this.sockets[key] = this.socket.getSubject(this.deviceId, key);
      this.sockets[key].subscribe(
        msg => {
          this.connected = true; 
          this.vehicleData[key] = msg[key];
          this.push();
        },
        err => {
          this.connected = false;
        }
      )
    });
    this.connecting = false;
  }

  unsubscribeWebSockets(){
    // Disconnect from every WebSocket.
    Object.keys(this.sockets).forEach(property => {
      var subject = this.sockets[property] as WebSocketSubject<any>;
      subject.unsubscribe();
    })
  }

  private push(){
    if(this.vehicleData.gps) this.map.updateMap(this.vehicleData.gps);  
  }


  ngOnDestroy(){
    this.unsubscribeWebSockets();
  }

}
