import { NgModule } from '@angular/core';
import { CoreComponent } from './core.component';
import { CoreRoutingModule } from './core-routing.module';
import { HeaderComponent } from './layout/header/header.component';
import { ScreenService } from '../shared/services/screen.service';
import { TitleService } from '../shared/services/title.service';
@NgModule({
  declarations: [
      CoreComponent,
      HeaderComponent,
  ],
  imports: [
    CoreRoutingModule
  ],
  providers: [ScreenService, TitleService]
})
export class CoreModule { }