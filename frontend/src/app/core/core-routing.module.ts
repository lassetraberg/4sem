import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '../shared/guards/auth.guard';
const routes: Routes = [
    {
      path: '',
      loadChildren: "../modules/home/home.module#HomeModule",
      canActivate: [AuthGuard],
      data: {
        animation: 'routerAnimation'
      }
    },
    {
      path: 'auth',
      loadChildren: "../modules/auth/auth.module#AuthModule",
      data: {
        animation: 'HomePage'
      }
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class CoreRoutingModule { }