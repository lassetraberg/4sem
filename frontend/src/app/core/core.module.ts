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
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../shared/interceptors/AuthInterceptor';
import { CoreRoutingModule } from './core-routing.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
      CoreComponent,
      HeaderComponent
  ],
  imports: [
      CoreRoutingModule,
      SharedModule
  ],
  providers: [
    ScreenService, 
    TitleService, 
    ThemeService, 
    NavigationService, 
    AuthService, 
    DataService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ]
})
export class CoreModule { }