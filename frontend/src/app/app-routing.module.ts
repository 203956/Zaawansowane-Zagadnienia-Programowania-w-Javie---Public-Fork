import {NgModule} from '@angular/core';
import {Route, RouterModule} from '@angular/router';
import {ExchangerateComponent} from "./exchangerate/exchangerate.component";

const APP_ROUTES: Route[] = [
  {path: '', pathMatch: 'full', redirectTo: 'exchangerate' },
  {path: 'exchangerate', component: <any>ExchangerateComponent}
];

@NgModule ({
  imports: [
    RouterModule.forRoot(APP_ROUTES)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule {}
