import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableCurrenciesService} from './available-currencies.service';
import {Currency} from './models/currency';
import {Rate} from './models/rate';
import {JsonResponse} from './models/jsonResponse';

@Component({
  selector: 'app-exchangerate',
  templateUrl: './exchangerate.component.html',
  styleUrls: ['./exchangerate.component.css']
})
export class ExchangerateComponent implements OnInit {

  currencies: Array<Currency>;
  rate: Rate;
  jsonR: JsonResponse;
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
    this.jsonR = new JsonResponse();
  }
  chooseCurrency(event): void {
    this.getSelectedCurrencyRate(event.target.value);
  }

  getSelectedCurrencyRate(currency: string) {
    this.availableCurrencyService
      .getRate(currency).subscribe(
      result => {
        this.jsonR = result;
        this.rate = new Rate();
        this.rate.importToMap(this.jsonR);
      },
      error => this.errorMessage = error,
      () => {
        console.log(this.rate);
        this.getRatequotesValue();
      }
    );
  }

  getRatequotesValue() {
    for (const [key, value] of Array.from(this.rate.quotes)) {
      this.rateValue = value;
    }
  }
}
