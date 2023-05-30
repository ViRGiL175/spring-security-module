import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {mergeMap, Observable} from "rxjs";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {FirebaseService} from "../../authorization/firebase/firebase.service";

export interface UserSettingsDto {
  name: any
}

export interface StatsDto {
  boxes: any,
  trucks: any,
  orders: any,
}

@Injectable({
  providedIn: 'root'
})
export class DataProviderService {

  constructor(
    private http: HttpClient,
    private angularFireAuth: AngularFireAuth,
    private firebaseService: FirebaseService) {
  }

  public getAllStatistic(): Observable<StatsDto> {
    return this.withAuthHeader()
      .pipe(mergeMap(value => this.http.get<StatsDto>(
        `${environment.serverUrl}/stats/all`, this.withParams(value))))
  }

  public getMyStatistic(): Observable<StatsDto> {
    return this.withAuthHeader()
      .pipe(mergeMap(value => this.http.get<StatsDto>(
        `${environment.serverUrl}/stats/my`, this.withParams(value))))
  }

  public getUserSettings(): Observable<UserSettingsDto> {
    return this.withAuthHeader()
      .pipe(mergeMap(value => this.http.get<UserSettingsDto>(
        `${environment.serverUrl}/user_settings`, this.withParams(value))))
  }

  private withAuthHeader() {
    return this.firebaseService.getAuthorizationHeader();
  }

  private withParams(headers: HttpHeaders) {
    return {headers: headers, withCredentials: true}
  }
}
