import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsaRoutingModule } from './isa-routing.module';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

@NgModule({
    declarations: [
        DashboardComponent
    ],
    imports: [ 
        CommonModule,
        IsaRoutingModule
    ],
    providers: []
  })
  export class IsaModule { }