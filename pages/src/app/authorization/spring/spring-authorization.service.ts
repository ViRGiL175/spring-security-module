import { Injectable } from "@angular/core"
import { HttpClient, HttpHeaders } from "@angular/common/http"
import { environment } from "../../../environments/environment"
import {lastValueFrom, mergeMap, Observable} from "rxjs"
import {FirebaseService} from "../firebase/firebase.service";
import {UserSettingsDto} from "../../data/spring/data-provider.service";

interface AuthorizationHeader {

  authScheme: "FIREBASE"
  principal: string
  credentials: string
}

@Injectable({
  providedIn: "root",
})
export class SpringAuthorizationService {

  constructor(private http: HttpClient, private firebaseService: FirebaseService) {
  }

  private withAuthHeader() {
    return this.firebaseService.getAuthorizationHeader();
  }

  private withParams(headers: HttpHeaders) {
    return {headers: headers, withCredentials: true}
  }

  separateAuth() {
    return this.withAuthHeader()
      .pipe(mergeMap(value => this.http.get<void>(
        `${environment.serverUrl}/oauth/firebase`, this.withParams(value))))
  }

  async authorizeSpringServer(userUid: string, userIdToken: string) {
    let authorizationHeader: AuthorizationHeader = {
      authScheme: "FIREBASE",
      principal: userUid,
      credentials: userIdToken,
    }
    let httpHeaders = new HttpHeaders({ Authorization: JSON.stringify(authorizationHeader) })
    console.log(JSON.stringify(httpHeaders))
    let authResult = await lastValueFrom(this.http.get(
      `${environment.serverUrl}/auth/firebase`,
      {
        headers: httpHeaders,
        withCredentials: true,
        observe: "response",
      }))
    console.log(`Auth result: ${JSON.stringify(authResult)}`)
    let boxes = await lastValueFrom(this.http.get(
      `${environment.serverUrl}/box`,
      {
        withCredentials: true,
        observe: "response",
      }))
    console.log(`Boxes: ${JSON.stringify(boxes)}`)
    return authResult
  }

  async authNew(token: string) {
    let httpHeaders = new HttpHeaders({ Authorization: `Bearer ${token}` })
    let authResult = await lastValueFrom(this.http.get(
      `${environment.serverUrl}/auth`,
      {
        headers: httpHeaders,
        // withCredentials: true,
        observe: "response",
      }))
    console.log(`Auth result: ${JSON.stringify(authResult)}`)
    let boxes = await lastValueFrom(this.http.get(
      `${environment.serverUrl}/box?page=0&size=5`,
      {
        headers: httpHeaders,
        // withCredentials: true,
        observe: "response",
      }))
    console.log(`Boxes: ${JSON.stringify(boxes)}`)
    return boxes
  }

}
