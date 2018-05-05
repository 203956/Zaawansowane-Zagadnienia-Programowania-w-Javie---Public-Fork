import { Injectable } from '@angular/core';
import {Currency} from './models/currency';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {CurrencyRate} from "./models/currencyRate";

@Injectable()
export class AvailableCurrenciesService {

  availableCurrenciesPath = '/api/currencies/available';
  selectedCurrencyPath = '/api/';

  constructor(
    private http: HttpClient) {}

  getAvailableCurrency(): Observable<Currency[]> {
    return this.http.get<Currency[]>(this.availableCurrenciesPath);
  }

  getRate1(currency: string): Observable<CurrencyRate> {
    return this.http.get<CurrencyRate>(this.selectedCurrencyPath + currency + '/rate');
  }
}
