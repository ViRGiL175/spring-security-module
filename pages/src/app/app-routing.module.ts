import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {AuthorizationComponent} from "./authorization/authorization.component";
import {DataComponent} from "./data/data.component";

const routes: Routes = [
  {path: "", redirectTo: "/auth", pathMatch: "full"},
  {path: "auth", component: AuthorizationComponent},
  {path: "data", component: DataComponent},
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes),
    CommonModule,
  ],
})
export class AppRoutingModule {
}
