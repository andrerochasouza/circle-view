import { LOCALE_ID, NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FooterComponent } from './components/footer/footer.component';
import { FrameComponent } from './components/frame/frame.component';
import { HomeComponent } from './views/home/home.component';
import { V1Component } from './views/home/v1/v1.component';
import { PoModule } from '@po-ui/ng-components';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { PoFieldModule } from '@po-ui/ng-components';
import { V2Component } from './views/home/v2/v2.component';


@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    FrameComponent,
    HomeComponent,
    V1Component,
    V2Component
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    RouterModule.forRoot([]),
    HttpClientModule,
    PoModule,
    FormsModule,
    PoFieldModule
  ],
  providers: [
    {
      provide: LOCALE_ID,
      useValue: 'pt-BR'
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
