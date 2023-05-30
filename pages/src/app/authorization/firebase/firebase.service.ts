import {Injectable} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {Router} from "@angular/router";
import {map, Observable} from "rxjs";
import firebase from "firebase/compat";

@Injectable({
  providedIn: 'root'
})
export class FirebaseService {

  token: String | null = null
  user: firebase.User | null = null
  authHeader: HttpHeaders | null = null

  constructor(private angularFireAuth: AngularFireAuth, private router: Router) {
  }

  public getFirebaseToken() {
    return this.angularFireAuth.idToken
  }

  public getFirebaseUser() {
    return this.angularFireAuth.user
  }

  public getAuthorizationHeader(): Observable<HttpHeaders> {
    return this.getFirebaseToken()
      .pipe(map(value => new HttpHeaders({Authorization: `Bearer ${value}`})))
  }

  /**
   * @deprecated
   */
  public buildAuthHeaders(): HttpHeaders {
    if (this.token == null) {
      this.router.navigateByUrl("/auth").then()
    }
    return new HttpHeaders({Authorization: `Bearer ${this.token}`})
  }
}
