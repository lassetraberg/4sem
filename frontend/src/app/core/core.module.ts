import { NgModule } from '@angular/core';
import { CoreComponent } from './core.component';
import { CoreRoutingModule } from './core-routing.module';
@NgModule({
  declarations: [
      CoreComponent,
  ],
  imports: [
    CoreRoutingModule
  ],
  providers: []
})
export class CoreModule { }