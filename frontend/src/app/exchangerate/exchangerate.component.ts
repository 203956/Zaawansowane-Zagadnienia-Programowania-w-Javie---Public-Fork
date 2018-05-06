import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableCurrenciesService} from './available-currencies.service';
import {Currency} from './models/currency';
import {CurrencyRate} from "./models/currencyRate";
import {RateInTime} from "./models/RateInTime";

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
  endDate: string = "2018-03-22";
  startDate: string = "2018-03-12";
  result: RateInTime[] = [];
  constructor(private router: Router,
              private route: ActivatedRoute,
              private availableCurrencyService: AvailableCurrenciesService) {
  }

  ngOnInit() {
    this.availableCurrencyService
      .getAvailableCurrency().subscribe(
        result => this.currencies = result
    );
  }

  chooseCurrency(event): void {
    this.getSelectedCurrencyRate(event.target.value);
  }

  test() {
    this.availableCurrencyService.test(this.startDate, this.endDate).then(e=> {
    e.map(elem =>
    this.result.push(elem));
      console.log(this.result);
    }).catch(error=> console.log(error));
      /*.map(e=>
      this.result.push(e)
    );*/

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
}
