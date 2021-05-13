import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'src/app/services/auth/user-route-access.service';
import { FacturaPage } from '../../Facturas/factura.page';
import { FacturaRoutingResolveService } from '../../Facturas/router/factura-router.resolve';
import { VehiculoRoutingModule } from '../../Vehiculos/router/vehiculo.routing.module';
import { VehiculoPage } from '../../Vehiculos/vehiculo.page';
import {ClientePage} from '../cliente.page';

const clienteroutes: Routes = [
    {
      path: '',
      component: ClientePage,
    },

    {
      path:':nameuser',
      component: ClientePage,
    },

    {
      path:':nameuser/:idVehiculo',
      component: VehiculoPage,
      resolve:{
        vehiculo: VehiculoRoutingModule,
      },
      canActivate: [UserRouteAccessService],
    },

    {
      path:':nameuser/:idVehiculo/:idFactura',
      component: FacturaPage,
      resolve:{
        factura: FacturaRoutingResolveService,
      },
      canActivate: [UserRouteAccessService],
    }


];

@NgModule({
    imports: [RouterModule.forChild(clienteroutes)],
    exports: [RouterModule]
})

export class ClienteRoutingModule {}