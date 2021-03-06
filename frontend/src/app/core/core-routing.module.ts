import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '../shared/guards/auth.guard';
import { AdminAuthGuard } from '../shared/guards/adminAuth.guard';
const routes: Routes = [
    {
      path: '',
      loadChildren: "../modules/home/home.module#HomeModule"
    },
    {
      path: 'auth',
      loadChildren: "../modules/auth/auth.module#AuthModule"
    },
    {
      path: 'isa',
      loadChildren: "../modules/isa/isa.module#IsaModule",
      canActivate: [AuthGuard]
    },
    {
      path: 'user',
      loadChildren: "../modules/user/user.module#UserModule",
      canActivate: [AuthGuard]
    },
    {
      path: 'admin',
      loadChildren: "../modules/admin/admin.module#AdminModule",
      canActivate: [AdminAuthGuard]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class CoreRoutingModule { }