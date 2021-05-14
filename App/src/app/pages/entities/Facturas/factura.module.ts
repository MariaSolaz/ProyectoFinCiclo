import { NgModule } from '@angular/core';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { CommonModule } from '@angular/common';
import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ApiService } from 'src/app/services/api/api.service';


import { FacturaRoutingModule } from './router/factura.routing.module';
import { FacturaPage } from './factura.page';
import { RouteReuseStrategy } from '@angular/router';
import { FacturaUpdate} from './update/factura-update.page';




@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule,
    FacturaRoutingModule,
    HttpClientModule,
   

  ],
  providers:[
    StatusBar,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    ApiService,
  ],
  declarations: [FacturaPage, FacturaUpdate]
})
export class FacturaModule {}