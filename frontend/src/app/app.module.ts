import { NgModule } from '@angular/core';
import { CoreComponent } from './core/core.component';
import { CoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './shared/interceptors/AuthInterceptor';
import { AuthGuard } from './shared/guards/auth.guard';
import { AuthService } from './shared/services/auth.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AdminAuthGuard } from './shared/guards/adminAuth.guard';

@NgModule({
  declarations: [
    
    ],
  imports: [
    BrowserAnimationsModule,
    CoreModule,
    HttpClientModule
  ],
  providers: [
    AuthInterceptor,
    AuthGuard,
    AdminAuthGuard,
    AuthService
  ],
  bootstrap: [CoreComponent]
})
export class AppModule { }
