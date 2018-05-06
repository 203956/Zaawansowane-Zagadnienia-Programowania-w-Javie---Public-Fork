import { Injectable } from '@angular/core';
import {Currency} from "./models/currency";
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class AvailableCurrenciesService {

  availableCurrenciesPath: string = '/api/currencies/available';

  private handleError: string;

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
}
