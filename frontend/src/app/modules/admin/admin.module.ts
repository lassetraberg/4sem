import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatTableModule } from "@angular/material/table";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatCardModule } from "@angular/material/card";
import { MatSliderModule, MatSlider } from "@angular/material/slider";
import { AdminRoutingModule } from "./admin-routing.module";
import { DashboardComponent } from "./pages/dashboard/dashboard.component";
import { UsersComponent } from "./pages/users/users.component";
import { DataComponent } from "./pages/data/data.component";
import { SharedModule } from "src/app/shared/components/shared.module";
import { FormsModule } from "@angular/forms";
import { VehicleDataComponent } from "src/app/shared/components/vehicle-data/vehicle-data.component";
@NgModule({
  declarations: [DashboardComponent, UsersComponent, DataComponent],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MatExpansionModule,
    MatTableModule,
    MatPaginatorModule,
    MatCardModule,
    MatSliderModule,
    SharedModule,
    FormsModule
  ],
  providers: []
})
export class AdminModule {}
