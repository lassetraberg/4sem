import { NgModule } from '@angular/core';
import { CoreComponent } from './core.component';
import { HeaderComponent } from './layout/header/header.component';
import { ScreenService } from '../shared/services/screen.service';
import { TitleService } from '../shared/services/title.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ThemeService } from '../shared/services/theme.service';
import { NavigationService } from '../shared/services/navigation.service';
import { AuthService } from '../shared/services/auth.service';
import { SharedModule } from '../shared/components/shared.module';
import { DataService } from '../shared/services/data.service';

@NgModule({
  declarations: [
      CoreComponent,
      HeaderComponent
  ],
  imports: [
    SharedModule
  ],
  providers: [ScreenService, TitleService, ThemeService, NavigationService, AuthService, DataService]
})
export class CoreModule { }