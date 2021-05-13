import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'src/app/services/auth/user-route-access.service';
import {FacturaPage} from '../factura.page';
import { FacturaUpdate } from '../update/factura-update.page';
import { FacturaRoutingResolveService } from './factura-router.resolve';

const facturaroutes: Routes = [
    {
      path: '',
      component: FacturaPage,
    },

    {
      path: ':idFactura',
      component:FacturaPage,
    },

    {
      path: ':idFactura/edit',
    component: FacturaUpdate,
    resolve: {
      factura: FacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    }
];

@NgModule({
    imports: [RouterModule.forChild(facturaroutes)],
    exports: [RouterModule]
})

export class FacturaRoutingModule {}