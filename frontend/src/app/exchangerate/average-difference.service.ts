import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {Currency} from "./models/currency";
import {RateInTime} from "./models/RateInTime";

@Injectable()
export class AverageDifferenceService {

  constructor(
    private http: HttpClient) {}

  mainCurrencyPath = '/api/currencies/';
  averageOeriodMainOath ='/api/currencies/average/';


  getDifferenceBetweenBuyingTwoCurrenciesInGivenPeriodOfTime(startDate, endDate, chosenCurrency1, chosenCurrency2, chosenCurrency3): Promise<RateInTime[]> {

    let url = this.mainCurrencyPath + chosenCurrency1 +"/" +chosenCurrency2 +"/"+chosenCurrency3+ "/"+ startDate +"/" + endDate+ "/rate"
    console.log(url);

    return this.http.get<RateInTime[]>(url).toPromise()
      .then(resolve=> {
        return resolve as RateInTime[];
      }, error => {
        console.log(error);
      });
  }

  getPriceOfCurrencyInGivenPeriodOfTime(startDate, endDate, chosenCurrency1, chosenCurrency2): Promise<RateInTime[]> {

    let url = this.mainCurrencyPath + chosenCurrency1 +"/" +chosenCurrency2+ "/"+ startDate +"/" + endDate+ "/rate"
    console.log(url);

    return this.http.get<RateInTime[]>(url).toPromise()
      .then(resolve=> {
        return resolve as RateInTime[];
      }, error => {
        console.log(error);
      });
  }

  test() {

    return this.http.get<RateInTime[]>(this.averageOeriodMainOath + "PLN/EUR/2018-01-30/2018-03-30" ).toPromise()
      .then(resolve=> {
        return resolve as RateInTime[];
      }, error => {
        console.log(error);
      });

  }

}
