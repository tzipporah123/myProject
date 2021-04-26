import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { CouponsComponent } from './coupons/coupons.component';
import { CouponListComponent } from './coupons/coupon-list/coupon-list.component';
import { CouponItemComponent } from './coupons/coupon-list/coupon-item/coupon-item.component';
import { CouponDetailComponent } from './coupons/coupon-detail/coupon-detail.component';
import { CouponEditComponent } from './coupons/coupon-edit/coupon-edit.component';
import { LoginComponent } from './login/login.component';
import { ClientComponent } from './client/client.component';
import { ClientEditComponent } from './client/client-edit/client-edit.component';
import { ClientDetilComponent } from './client/client-detil/client-detil.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http'
import { StorageService } from './common/storage.service';
import { ClientService } from './client/client.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CompanyComponent } from './coupons/company/company.component';
import { CustomerComponent } from './coupons/customer/customer.component';
import { CouponService } from './coupons/coupon.service';
import { LoginService } from './login/login.service';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CouponsComponent,
    CouponListComponent,
    CouponItemComponent,
    CouponDetailComponent,
    CouponEditComponent,
    LoginComponent,
    ClientComponent,
    ClientEditComponent,
    ClientDetilComponent,
    CompanyComponent,
    CustomerComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatFormFieldModule
  ],
  exports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
  ],
  providers: [ClientService, StorageService,CouponService,LoginService],
  bootstrap: [AppComponent]
})
export class AppModule { }
