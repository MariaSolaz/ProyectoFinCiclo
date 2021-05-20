import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {VehiculoPage} from '../vehiculo.page';

const vehiculoroutes: Routes = [
    {
      path: '',
      component: VehiculoPage,
    },
    {
      path: ':idVehiculo',
      component: VehiculoPage,
    }
];

@NgModule({
    imports: [RouterModule.forChild(vehiculoroutes)],
    exports: [RouterModule]
})

export class VehiculoRoutingModule {}