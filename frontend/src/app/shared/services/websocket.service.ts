import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

import { environment } from 'src/environments/environment';

@Injectable()
export class WebsocketService {

    webSocket: WebSocketSubject<any>;

    ngOnInit() {
        this.webSocket = webSocket(`${environment.ws}/ws/speed-assistant/2905d0e7-615c-455b-8807-ddd7665d3994/velocity`);
        this.webSocket.next('Bearer ' + localStorage.getItem("id_token"));
    }

    public getSubject(): WebSocketSubject<any> {
        return this.webSocket;
    }
}
