import {JsonResponse} from './jsonResponse';

export class Rate {
   query: Map<string, number>;
   results: Map<string,{ [name: string]: any}>;

  constructor() {
    this.query = new Map<string, number>();
    this.results = new Map<string, { [name: string]: any}>();

  }

  importToMap(json: JsonResponse) {
    Object.keys(json.query)
      .forEach(key =>  this.query.set(key, json.query[key]));
    Object.keys(json.results)
      .forEach(key =>  this.results.set(key, json.results[key]));
  }
}
