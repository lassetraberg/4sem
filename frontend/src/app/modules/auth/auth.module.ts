import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AuthRoutingModule } from "./auth-routing.module";
import { SigninComponent } from "./pages/signin/signin.component";
import { AuthService } from "src/app/shared/services/auth.service";
import { ReactiveFormsModule } from "@angular/forms";
import { SignupComponent } from "./pages/signup/signup.component";
import { MatSnackBarModule } from "@angular/material";

@NgModule({
  declarations: [SigninComponent, SignupComponent],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    MatSnackBarModule
  ],
  exports: [],
  providers: [AuthService]
})
export class AuthModule {}
