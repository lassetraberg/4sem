import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DashboardComponent } from "./pages/dashboard/dashboard.component";
import { UsersComponent } from "./pages/users/users.component";
import { DataComponent } from "./pages/data/data.component";

const routes: Routes = [
  {
    path: "",
    component: DashboardComponent,
    data: { title: "Dashboard" }
  },
  {
    path: "users",
    component: UsersComponent,
    data: { title: "User Administration" }
  },
  {
    path: "data",
    component: DataComponent,
    data: { title: "Vehicle Data" }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
