import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsaRoutingModule } from './isa-routing.module';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { WebsocketService } from 'src/app/shared/services/websocket.service';
import { SharedModule } from 'src/app/shared/components/shared.module';

@NgModule({
    declarations: [
        DashboardComponent
    ],
    imports: [ 
        CommonModule,
        IsaRoutingModule,
        SharedModule
    ],
    providers: [WebsocketService]
  })
  export class IsaModule { }