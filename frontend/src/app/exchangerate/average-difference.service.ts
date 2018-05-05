import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {Currency} from "./models/currency";

@Injectable()
export class AverageDifferenceService {

  averageDifferencePath: string = '/api/';

  private handleError: string;

  constructor(
    private http: HttpClient) {}

  getAeraggeDifference() {
    const parameters = {
      firstCurrency: 'PLN',
      secondCurrency: 'USD',
      firstDate: '',
      secondDate: ''};

    return this.http.get(this.averageDifferencePath, parameters)
      .toPromise();
  }

}
