import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavComponent } from './nav/nav.component';
import { CoreRoutingModule } from 'src/app/core/core-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
    declarations: [
        NavComponent
    ],
    imports: [ 
        CoreRoutingModule,
        BrowserAnimationsModule
    ],
    exports: [
        NavComponent,
        CoreRoutingModule,
        BrowserAnimationsModule
    ],
    providers: []
  })
  export class SharedModule { }