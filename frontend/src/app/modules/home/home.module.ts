import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './pages/home/home.component';
@NgModule({
    declarations: [
        HomeComponent
    ],
    imports: [ 
        CommonModule,
        HomeRoutingModule
    ],
    providers: []
  })
  export class HomeModule { }