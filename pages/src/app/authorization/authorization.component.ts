import {Component, OnInit} from "@angular/core"
import {ActivatedRoute, Router} from "@angular/router"
import {FirebaseUISignInFailure, FirebaseUISignInSuccessWithAuthResult} from "firebaseui-angular"
import {AngularFireAuth} from "@angular/fire/compat/auth"
import {SpringAuthorizationService} from "./spring/spring-authorization.service"
import {FirebaseService} from "./firebase/firebase.service";
import {lastValueFrom} from "rxjs";

export const authRecordParameter = "auth-record-uuid"

@Component({
  selector: "app-authorization",
  templateUrl: "./authorization.component.html",
  styleUrls: ["./authorization.component.css"],
})
export class AuthorizationComponent implements OnInit {

  public authRecordUuid: string | null = null;

  constructor(
    public angularFireAuth: AngularFireAuth,
    public springAuthorizationService: SpringAuthorizationService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
    public firebaseService: FirebaseService,
  ) {
  }

  async ngOnInit() {
    let authRecordUuid = this.activatedRoute.snapshot.params[authRecordParameter]
    this.activatedRoute.params.subscribe(value => this.authRecordUuid = value[authRecordParameter])
    if (await lastValueFrom(this.angularFireAuth.user) != null) {
      console.log("Юзер авторизован, делаем редирект...")
      this.router.navigateByUrl("/data").then()
    } else {
      this.angularFireAuth.signOut().then()
    }
  }

  onSignInFailure($event: FirebaseUISignInFailure) {
    alert(`Провал: ${JSON.stringify($event)}`)
  }

  async onSignInSuccessWithAuthResult($event: FirebaseUISignInSuccessWithAuthResult) {
    console.log(`Успех: ${JSON.stringify($event)}`)
    // let user = $event.authResult.user
    // let creds = $event.authResult.credential
    // let userUid = $event.authResult.user?.uid!
    // let idToken = await $event.authResult.user?.getIdToken(true)!
    // this.firebaseService.authToken = idToken
    // // let authResult = await this.springAuthorizationService.authorizeSpringServer(userUid, idToken)
    // let authResult = await this.springAuthorizationService.authNew(idToken)
    // console.log(`Успех: ${JSON.stringify(authResult)}`)
    await lastValueFrom(this.springAuthorizationService.separateAuth());
    this.router.navigateByUrl("/data").then()
  }

  onLogoutClick($event: MouseEvent) {
    this.angularFireAuth.signOut().then()
    this.router.navigateByUrl("/auth").then()
  }
}
