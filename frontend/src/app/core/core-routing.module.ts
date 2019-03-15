import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '../shared/guards/auth.guard';
const routes: Routes = [
    {
      path: '',
      loadChildren: "../modules/home/home.module#HomeModule",
      canActivate: [AuthGuard]
    },
    {
      path: 'login',
      loadChildren: "../modules/auth/auth.module#AuthModule"
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class CoreRoutingModule { }