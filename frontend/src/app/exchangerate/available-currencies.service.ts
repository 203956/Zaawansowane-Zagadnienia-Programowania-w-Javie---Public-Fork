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

  constructor(
    private http: HttpClient) {}

  getAvailableCurrency(): Observable<Currency[]> {
    return this.http.get<Currency[]>(this.availableCurrenciesPath);
  }

  test(startDate, endDate, chosenCurrency1, chosenCurrency2): Promise<RateInTime[]> {
    let url = this.selectedCurrencyPath + chosenCurrency1 +"/"+ startDate +"/" + endDate+ "/rate"
    console.log(url);
    return this.http.get<RateInTime[]>(url).toPromise(
    ).then(resolve=> {
      console.log(resolve);
      return resolve as RateInTime[];
    }, error => {
      console.log(error);
    });
  }

  getRate(symbol: string): Observable<CurrencyRate> {
    return this.http.get<CurrencyRate>(this.selectedCurrencyPath + symbol + '/rate');
  }
}
