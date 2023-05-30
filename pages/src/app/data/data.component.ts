import {Component, OnInit} from '@angular/core';
import {DataProviderService, StatsDto, UserSettingsDto} from "./spring/data-provider.service";

const loading = "loading...";

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css']
})
export class DataComponent implements OnInit {

  allStatistic: StatsDto | null = null;
  myStatistic: StatsDto | null = null;
  userSettings: UserSettingsDto | null = null;

  constructor(private dataService: DataProviderService) {
  }

  ngOnInit() {
    this.dataService.getAllStatistic().subscribe(value => this.allStatistic = value)
    this.dataService.getMyStatistic().subscribe(value => this.myStatistic = value)
    this.dataService.getUserSettings().subscribe(value => this.userSettings = value)
  }
}
