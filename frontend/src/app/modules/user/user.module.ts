import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './pages/user/user.component';
import { AuthService } from 'src/app/shared/services/auth.service';
@NgModule({
    declarations: [
        UserComponent
    ],
    imports: [ 
        CommonModule,
        UserRoutingModule
    ],
    providers: [AuthService]
  })
  export class UserModule { }