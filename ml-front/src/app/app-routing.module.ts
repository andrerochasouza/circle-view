import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './views/home/home.component';
import { V1Component } from './views/home/v1/v1.component';
import { V2Component } from './views/home/v2/v2.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      // home
      {path: '', redirectTo: 'home', pathMatch: 'full'},
      {path: 'home', component: HomeComponent},
      {path: 'v1-mnist', component: V1Component},
      {path: 'v2-cifar10', component: V2Component},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
