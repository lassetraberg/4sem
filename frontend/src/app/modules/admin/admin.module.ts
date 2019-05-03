import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UsersComponent } from './pages/users/users.component';
import { DataComponent } from './pages/data/data.component';
@NgModule({
    declarations: [
        DashboardComponent,
        UsersComponent,
        DataComponent
    ],
    imports: [ 
        CommonModule,
        AdminRoutingModule
    ],
    providers: []
  })
  export class AdminModule { }