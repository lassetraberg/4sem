import { NgModule } from '@angular/core';
import { NavComponent } from './nav/nav.component';
import { MapComponent } from './map/map.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@NgModule({
    declarations: [
        NavComponent,
        MapComponent
    ],
    imports: [
        RouterModule,
        CommonModule
    ],
    exports: [
        NavComponent,
        MapComponent,
        RouterModule,
        CommonModule,
    ],
    providers: []
  })
  export class SharedModule { }