import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

import { environment } from 'src/environments/environment';

@Injectable()
export class WebsocketService {

    /**
     * Get WebSocketSubject for a given device-id and data.
     * @param device id.
     * @param data type of data.
     */
    public getSubject(device: string, data: string): WebSocketSubject<any> {
        var socket = webSocket(`${environment.ws}/ws/speed-assistant/` + device + `/` + data);
        socket.next('Bearer ' + localStorage.getItem("id_token"));
        return socket;
    }

}
