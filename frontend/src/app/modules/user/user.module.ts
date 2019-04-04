import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './pages/user/user.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { AddVehicleComponent } from './pages/add-vehicle/add-vehicle.component';
@NgModule({
    declarations: [
        UserComponent,
        AddVehicleComponent
    ],
    imports: [ 
        CommonModule,
        UserRoutingModule
    ],
    providers: [AuthService]
  })
  export class UserModule { }