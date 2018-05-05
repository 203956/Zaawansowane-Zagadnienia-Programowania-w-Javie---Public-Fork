import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { ExchangerateComponent } from './exchangerate/exchangerate.component';
import {AppRoutingModule} from "./app-routing.module";
import {AvailableCurrenciesService} from "./exchangerate/available-currencies.service";
import {HttpClient, HttpClientModule, HttpHandler} from "@angular/common/http";
import {AverageDifferenceService} from "./exchangerate/average-difference.service";


@NgModule({
  declarations: [
    AppComponent,
    ExchangerateComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    AvailableCurrenciesService,
    HttpClient,
    AverageDifferenceService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
