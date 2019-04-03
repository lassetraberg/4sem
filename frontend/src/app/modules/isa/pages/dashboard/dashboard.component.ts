import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import { WebsocketService } from 'src/app/shared/services/websocket.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  private velocity: number;

  constructor(private socket: WebsocketService) { }

  ngOnInit() {
    this.velocity = 50;
    this.socket.ngOnInit();
    this.socket.getSubject().subscribe(
      msg => this.velocity = msg.velocity,
      err => console.log(err),
      () => console.log('complete')
    );
  }

}
