import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CouponsComponent } from './coupons/coupons.component';
import { LoginComponent } from './login/login.component';
import { ClientComponent } from './client/client.component';
import { CouponDetailComponent } from './coupons/coupon-detail/coupon-detail.component';
import { CouponEditComponent } from './coupons/coupon-edit/coupon-edit.component';
import { CouponStartComponent } from './coupons/coupon-start/coupon-start.component';
import { ClientEditComponent } from './client/client-edit/client-edit.component';
import { ClientDetilComponent } from './client/client-detil/client-detil.component';

const routes: Routes = [
  { path: "", pathMatch: "full", redirectTo: "/login" },
  { path: "login", component: LoginComponent },
  {
    path: "client", component: ClientComponent, children: [
      { path: "", component: ClientDetilComponent },
      { path: "edit", component: ClientEditComponent },
    ]
  },
  {
    path: "coupons", component: CouponsComponent, children: [
      { path: "", component: CouponStartComponent, pathMatch: "full" },
      { path: "new", component: CouponEditComponent },
      { path: ":id", component: CouponDetailComponent },
      { path: ":id/edit", component: CouponEditComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
