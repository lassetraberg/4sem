import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CoreComponent } from './core/core.component';
import { CoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './shared/interceptors/AuthInterceptor';
import { AuthGuard } from './shared/guards/auth.guard';
import { AuthService } from './shared/services/auth.service';

@NgModule({
  declarations: [
    
    ],
  imports: [
    BrowserModule,
    CoreModule,
    HttpClientModule
  ],
  providers: [
    AuthInterceptor,
    AuthGuard,
    AuthService
  ],
  bootstrap: [CoreComponent]
})
export class AppModule { }
