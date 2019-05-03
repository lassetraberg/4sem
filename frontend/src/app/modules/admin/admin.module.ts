import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatTableModule } from "@angular/material/table";
import { MatPaginatorModule } from "@angular/material/paginator";
import { AdminRoutingModule } from "./admin-routing.module";
import { DashboardComponent } from "./pages/dashboard/dashboard.component";
import { UsersComponent } from "./pages/users/users.component";
import { DataComponent } from "./pages/data/data.component";
@NgModule({
  declarations: [DashboardComponent, UsersComponent, DataComponent],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MatExpansionModule,
    MatTableModule,
    MatPaginatorModule
  ],
  providers: []
})
export class AdminModule {}
