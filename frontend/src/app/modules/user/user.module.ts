import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserRoutingModule } from "./user-routing.module";
import { UserComponent } from "./pages/user/user.component";
import { AuthService } from "src/app/shared/services/auth.service";
import { AddVehicleComponent } from "./pages/add-vehicle/add-vehicle.component";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { SharedModule } from "src/app/shared/components/shared.module";
@NgModule({
  declarations: [UserComponent, AddVehicleComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    SharedModule
  ],
  providers: [AuthService]
})
export class UserModule {}
