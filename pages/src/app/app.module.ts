import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AngularFireModule} from "@angular/fire/compat";
import {AngularFireAuthModule} from "@angular/fire/compat/auth";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app-routing.module";
import {AuthorizationComponent} from "./authorization/authorization.component";
import {FirebaseUIModule} from "firebaseui-angular";
import {environment} from "../environments/environment";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterOutlet} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import {HttpClientModule} from "@angular/common/http";
import {MatToolbarModule} from "@angular/material/toolbar";
import { DataComponent } from './data/data.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthorizationComponent,
    DataComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireAuthModule,
    FirebaseUIModule.forRoot(environment.firebaseUiAuthConfig),
    BrowserAnimationsModule,
    RouterOutlet,
    MatButtonModule,
    MatToolbarModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
