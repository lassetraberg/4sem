import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserComponent } from './pages/user/user.component';
import { AddVehicleComponent } from './pages/add-vehicle/add-vehicle.component';

const routes: Routes = [
    {
        path: '',
        component: UserComponent,
        data: {title: "User"}
    },
    {
        path: 'add-vehicle',
        component: AddVehicleComponent,
        data: {title: "AddNewVehicle"}
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class UserRoutingModule { }