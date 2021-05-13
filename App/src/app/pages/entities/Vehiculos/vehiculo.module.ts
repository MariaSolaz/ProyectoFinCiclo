import { NgModule } from '@angular/core';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { CommonModule } from '@angular/common';
import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ApiService } from 'src/app/services/api/api.service';

import { VehiculoRoutingModule } from './router/vehiculo.routing.module';
import { RouteReuseStrategy } from '@angular/router';
import { VehiculoPage } from './vehiculo.page';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    VehiculoRoutingModule,
    HttpClientModule,
  ],
  providers:[
    StatusBar,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    ApiService,
  ],
  declarations: [VehiculoPage]
})
export class VehiculoModule {}