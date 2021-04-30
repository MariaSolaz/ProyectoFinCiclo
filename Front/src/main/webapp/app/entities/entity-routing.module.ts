import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'Clientes' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'mecanico',
        data: { pageTitle: 'Mecanicos' },
        loadChildren: () => import('./mecanico/mecanico.module').then(m => m.MecanicoModule),
      },
      {
        path: 'vehiculo',
        data: { pageTitle: 'Vehiculos' },
        loadChildren: () => import('./vehiculo/vehiculo.module').then(m => m.VehiculoModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'Facturas' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },

      {
        path: 'dashboard',
        data: { pageTitle: 'Dashboards' },
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
