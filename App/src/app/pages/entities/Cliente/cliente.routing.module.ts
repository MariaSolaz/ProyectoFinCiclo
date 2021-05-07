import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientePage} from './cliente.page';

const routes: Routes = [
    {
      path: '',
      component: ClientePage,
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class ClienteRoutingModule {}