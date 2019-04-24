import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { WebsocketService } from 'src/app/shared/services/websocket.service';
import { WebSocketSubject } from 'rxjs/webSocket';
import { VehicleData } from 'src/app/shared/models/vehicledata';
import { interval, Subscription, Observable, of, BehaviorSubject } from 'rxjs';
import { MapComponent } from 'src/app/shared/components/map/map.component';

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

  sockets = {};

  constructor(private socket: WebsocketService) { 
    this.vehicleData = new VehicleData();
  }

  ngOnInit() {
    // Connect to WebSockets for the properties of VehicleData.   
    this.subscribeToWebSockets();
  }

  ngAfterViewInit(){

  }

  subscribeToWebSockets(){
    this.connecting = true;
    this.unsubscribeWebSockets();
    Object.keys(this.vehicleData).forEach(key => {  
      this.sockets[key] = this.socket.getSubject("2905d0e7-615c-455b-8807-ddd7665d3994", key);
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
