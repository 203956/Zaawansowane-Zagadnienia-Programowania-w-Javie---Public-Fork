import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableCurrenciesService} from './available-currencies.service';
import {Currency} from './models/currency';
import {CurrencyRate} from "./models/currencyRate";
import {RateInTime} from "./models/RateInTime";
import {AverageDifferenceService} from "./average-difference.service";

@Component({
  selector: 'app-exchangerate',
  templateUrl: './exchangerate.component.html',
  styleUrls: ['./exchangerate.component.css']
})
export class ExchangerateComponent implements OnInit {

  currencies: Array<Currency>;
  rate: CurrencyRate;
  rateValue: number;
  errorMessage: string;
  endDate: string = "2018-03-15";
  startDate: string = "2018-03-11";
  result1: RateInTime[] = [];
  result2: RateInTime[] = [];
  resultDifference: RateInTime[] = [];

  chosenCurrency1: Currency;
  chosenCurrency2: Currency;
  chosenCurrency3: Currency;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private availableCurrencyService: AvailableCurrenciesService,
              private averageDifference: AverageDifferenceService) {
  }

  ngOnInit() {
    this.availableCurrencyService
      .getAvailableCurrency().subscribe(
        result => this.currencies = result
    );
  }

  chooseCurrency(event): void {
    this.getSelectedCurrencyRate(event.target.value);
    this.chosenCurrency1 = event.target.value;
  }

   chooseCurrency2(event): void {
     this.chosenCurrency2 = event.target.value;
   }

  chooseCurrency3(event): void {
    this.chosenCurrency3 = event.target.value;
  }

  //todo wysylac jeden request do liczenia róznicy
  getDifferenceForChosenPeriodOfTime() {
    this.averageDifference.getPriceOfCurrencyInGivenPeriodOfTime(this.startDate, this.endDate, this.chosenCurrency2, this.chosenCurrency1)
      .then(e=> {
        this.result1 = [];
        e.map(elem =>
        this.result1.push(elem));
        console.log(this.result1);
      })
      .catch(error=> console.log(error));

    this.averageDifference.getPriceOfCurrencyInGivenPeriodOfTime(this.startDate, this.endDate, this.chosenCurrency3, this.chosenCurrency1)
      .then(e=> {
        this.result2=[];
        e.map(elem =>
          this.result2.push(elem));
        console.log(this.result2);
      })
      .catch(error=> console.log(error));

    this.averageDifference.getDifferenceBetweenBuyingTwoCurrenciesInGivenPeriodOfTime(this.startDate, this.endDate, this.chosenCurrency1,
                                                                                      this.chosenCurrency2,
                                                                                      this.chosenCurrency3)
      .then(e=> {
        this.resultDifference=[];
        e.map(elem =>
          this.resultDifference.push(elem));
        console.log(this.resultDifference);
      })
      .catch(error=> console.log(error));
  }

  getSelectedCurrencyRate(symbol: string) {
    this.availableCurrencyService
      .getRate(symbol).subscribe(
      result => {
        this.rate = result;
      },
      error => this.errorMessage = error,
      () => {
        console.log(this.rate.rate);
        this.rateValue = this.rate.rate;
      }
    );
  }

  getDate(event) {
    let x = event.target.value;
    console.log(x);
  }
}
