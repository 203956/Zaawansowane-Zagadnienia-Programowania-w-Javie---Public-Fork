import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableCurrenciesService} from './available-currencies.service';
import {Currency} from './models/currency';
import {CurrencyRate} from "./models/currencyRate";

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

  getSelectedCurrencyRate(currency: string) {
    this.availableCurrencyService
      .getRate1(currency).subscribe(
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
