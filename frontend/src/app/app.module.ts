import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CoreComponent } from './core/core.component';
import { CoreModule } from './core/core.module';

@NgModule({
  declarations: [
    
    ],
  imports: [
    BrowserModule,
    CoreModule
  ],
  providers: [],
  bootstrap: [CoreComponent]
})
export class AppModule { }
