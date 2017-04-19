import { Component } from '@angular/core';
import {DataService} from "./data.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [DataService]
})
export class AppComponent {
  employees = [];

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.dataService.getEmployees().subscribe((data) => this.employees = data);
  }
}
