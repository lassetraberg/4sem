import { NgModule } from "@angular/core";
import { NavComponent } from "./nav/nav.component";
import { MapComponent } from "./map/map.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { VehicleDataComponent } from "./vehicle-data/vehicle-data.component";
import {
  MatExpansionModule,
  MatTableModule,
  MatPaginatorModule,
  MatCardModule,
  MatSliderModule
} from "@angular/material";
import { FormsModule } from "@angular/forms";

@NgModule({
  declarations: [NavComponent, MapComponent, VehicleDataComponent],
  imports: [
    RouterModule,
    CommonModule,
    MatExpansionModule,
    MatTableModule,
    MatPaginatorModule,
    MatCardModule,
    MatSliderModule,
    FormsModule
  ],
  exports: [
    NavComponent,
    MapComponent,
    RouterModule,
    CommonModule,
    VehicleDataComponent
  ],
  providers: []
})
export class SharedModule {}
