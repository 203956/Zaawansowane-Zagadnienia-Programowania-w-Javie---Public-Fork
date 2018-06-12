import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableCurrenciesService} from './available-currencies.service';
import {CurrencyRate} from './models/currencyRate';

@Component({
  selector: 'app-exchangerate',
  templateUrl: './exchangerate.component.html',
  styleUrls: ['./exchangerate.component.css']
})
export class ExchangerateComponent implements OnInit {

  currencies = [];
  selectedCurrencies = [];
  currenciesAmount = [];
  rate: CurrencyRate;
  rateForSelectedCurrencies: CurrencyRate;
  currency = 'PLN';
  rateValue: number;
  rateValueForSelectedCurrencies: number;
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

  calculateCurrenciesRate(): void {
    this.getSelectedCurrenciesAmountOfOtherCurrency();
  }

  getSelectedCurrenciesAmountOfOtherCurrency(): void {
    this.availableCurrencyService
      .getSelectedCurrenciesAmountOfOtherCurrency(this.currenciesAmount, this.selectedCurrencies, this.currency)
      .subscribe(
        result => {
          this.rateForSelectedCurrencies = result;
        },
        error => this.errorMessage = error,
        () => {
          this.rateValueForSelectedCurrencies = this.rateForSelectedCurrencies.rate;
        });
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

  setArrayWithCheckboxValues(event, item) {
    if ( !event.target.checked ) {
      const index: number = this.selectedCurrencies.indexOf(item.symbol);
      if (index !== -1) {
        this.selectedCurrencies.splice(index, 1);
        this.currenciesAmount.splice(index, 1);
      }
    } else {
      this.selectedCurrencies.push(item.symbol);
    }
  }
}
