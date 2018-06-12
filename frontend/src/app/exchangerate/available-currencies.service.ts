import { Injectable } from '@angular/core';
import {Currency} from "./models/currency";
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {CurrencyRate} from './models/currencyRate';
import {RateInTime} from "./models/RateInTime";

@Injectable()
export class AvailableCurrenciesService {

  availableCurrenciesPath = '/api/currencies/available';
  selectedCurrencyPath = '/api/currencies/';
  selectedCurrenciesAmountOfOtherCurrencyPath = '/api/currencies/';

  constructor(private http: HttpClient) {
  }

  getAvailableCurrency(): Observable<Currency[]> {
    return this.http.get<Currency[]>(this.availableCurrenciesPath);
  }

  getRate(symbol: string): Observable<CurrencyRate> {
    return this.http.get<CurrencyRate>(this.selectedCurrencyPath + symbol + '/rate');
  }

  getSelectedCurrenciesAmountOfOtherCurrency(amountOfInCurrencies: number[], inCurrencies: string[], outCurrency: string): Observable<CurrencyRate> {
    let endpoint = this.selectedCurrenciesAmountOfOtherCurrencyPath + '?out=' + outCurrency;
    inCurrencies.forEach(c => endpoint += `&currencies=${c}`);
    amountOfInCurrencies.forEach(a => endpoint += `&amount=${a}`);
    return this.http.get<CurrencyRate>(endpoint);
  }
}
