import { Injectable } from '@angular/core';
import {Currency} from "./models/currency";
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {CurrencyRate} from './models/currencyRate';

@Injectable()
export class AvailableCurrenciesService {

  availableCurrenciesPath = '/api/currencies/available';
  selectedCurrencyPath = '/api/currencies/';

  constructor(
    private http: HttpClient) {}

  getAvailableCurrency(): Observable<Currency[]> {
    return this.http.get<Currency[]>(this.availableCurrenciesPath);
  }
  test() {
    return this.http.get<number>(this.availableCurrenciesPath+"/a").toPromise().then(resolve=> {
      console.log(resolve);
    });
  }

  getRate(symbol: string): Observable<CurrencyRate> {
    return this.http.get<CurrencyRate>(this.selectedCurrencyPath + symbol + '/rate');
  }
}
