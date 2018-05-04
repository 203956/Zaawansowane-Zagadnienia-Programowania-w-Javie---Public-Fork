import {JsonResponse} from './jsonResponse';

export class Rate {
  public success: boolean;
  public terms: string;
  public privacy: string;
  public source: string;
  public timestamp: number;
  public quotes: Map<string, number>;

  constructor() {
    this.quotes = new Map<string, number>();
  }

  importToMap(json: JsonResponse) {
    this.success = json.success;
    this.terms = json.terms;
    this.privacy = json.privacy;
    this.source = json.source;
    this.timestamp = json.timestamp;
    Object.keys(json.quotes)
      .forEach(key =>  this.quotes.set(key, json.quotes[key]));
  }
}
