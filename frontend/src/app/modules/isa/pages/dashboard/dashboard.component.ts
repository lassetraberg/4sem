import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { WebsocketService } from 'src/app/shared/services/websocket.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  velocity: number;

  constructor(private socket: WebsocketService) { }

  ngOnInit() {
    this.socket.getSubject("2905d0e7-615c-455b-8807-ddd7665d3994", "velocity").subscribe(
      msg => this.velocity = msg.velocity,
      err => console.log(err),
      () => console.log('complete')
    );
  }

}
