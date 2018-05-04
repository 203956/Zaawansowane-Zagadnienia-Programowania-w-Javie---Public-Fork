import { Injectable } from '@angular/core';
import {Currency} from './models/currency';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {JsonResponse} from './models/jsonResponse';

@Injectable()
export class AvailableCurrenciesService {

  availableCurrenciesPath = '/api/currencies/available';
  selectedCurrencyPath = '/api/';

  constructor(
    private http: HttpClient) {}

  getAvailableCurrency(): Observable<Currency[]> {
    return this.http.get<Currency[]>(this.availableCurrenciesPath);
  }
  getRate(currency: string): Observable<JsonResponse> {
    return this.http.get<JsonResponse>(this.selectedCurrencyPath + currency + '/rate');
  }
}
