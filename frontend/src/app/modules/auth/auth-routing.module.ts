import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SigninComponent } from './pages/signin/signin.component';
import { SignupComponent } from './pages/signup/signup.component';

const routes: Routes = [
    {
        path: 'signin',
        component: SigninComponent,
        data: {title: "SignIn"}
    },
    {
        path: 'signup',
        component: SignupComponent,
        data: {title: "Register",}
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class AuthRoutingModule { }