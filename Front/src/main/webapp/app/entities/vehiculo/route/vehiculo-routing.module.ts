import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VehiculoComponent } from '../list/vehiculo.component';
import { VehiculoDetailComponent } from '../detail/vehiculo-detail.component';
import { VehiculoUpdateComponent } from '../update/vehiculo-update.component';
import { VehiculoRoutingResolveService } from './vehiculo-routing-resolve.service';
import { FacturaComponent } from 'app/entities/factura/list/factura.component';
import { FacturaUpdateComponent} from '../../factura/update/factura-update.component'
import { FacturaRoutingResolveService } from 'app/entities/factura/route/factura-routing-resolve.service';

const vehiculoRoute: Routes = [
  {
    path: '',
    component: VehiculoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehiculoDetailComponent,
    resolve: {
      vehiculo: VehiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehiculoUpdateComponent,
    resolve: {
      vehiculo: VehiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehiculoUpdateComponent,
    resolve: {
      vehiculo: VehiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/facturas',
    component: FacturaComponent,
    resolve: {
      vehiculo: VehiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idVehiculo/facturas/new',
    component: FacturaUpdateComponent,
    resolve: {
      factura: FacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },

];

@NgModule({
  imports: [RouterModule.forChild(vehiculoRoute)],
  exports: [RouterModule],
})
export class VehiculoRoutingModule {}
