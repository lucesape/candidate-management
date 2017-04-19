import { Injectable } from '@angular/core';
import { Http } from "@angular/http";

@Injectable()
export class DataService {

  constructor(private http: Http) { }

  getEmployees() {
    return this.http.get('/api/employees').map((res) => res.json());
  }

}
