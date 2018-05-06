import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AvailableCurrenciesService} from "./available-currencies.service";
import {Currency} from "./models/currency";

@Component({
  selector: 'app-exchangerate',
  templateUrl: './exchangerate.component.html',
  styleUrls: ['./exchangerate.component.css']
})
export class ExchangerateComponent implements OnInit {

  currencies: Array<Currency>;

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
    console.log(event.target.value);
  }

  test() {
    this.availableCurrencyService.test();
  }

}
