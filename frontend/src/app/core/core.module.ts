import { NgModule } from '@angular/core';
import { CoreComponent } from './core.component';
import { CoreRoutingModule } from './core-routing.module';
import { HeaderComponent } from './layout/header/header.component';
import { ScreenService } from '../shared/services/screen.service';
import { TitleService } from '../shared/services/title.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ThemeService } from '../shared/services/theme.service';
import { NavComponent } from './layout/nav/nav.component';
import { NavigationService } from '../shared/services/navigation.service';

@NgModule({
  declarations: [
      CoreComponent,
      HeaderComponent,
      NavComponent,
  ],
  imports: [
    CoreRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [ScreenService, TitleService, ThemeService, NavigationService]
})
export class CoreModule { }