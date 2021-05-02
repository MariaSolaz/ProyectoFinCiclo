import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MecanicoComponent } from '../list/mecanico.component';
import { MecanicoDetailComponent } from '../detail/mecanico-detail.component';
import { MecanicoUpdateComponent } from '../update/mecanico-update.component';
import { MecanicoRoutingResolveService } from './mecanico-routing-resolve.service';

const mecanicoRoute: Routes = [
  {
    path: '',
    component: MecanicoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MecanicoDetailComponent,
    resolve: {
      mecanico: MecanicoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MecanicoUpdateComponent,
    resolve: {
      mecanico: MecanicoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MecanicoUpdateComponent,
    resolve: {
      mecanico: MecanicoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mecanicoRoute)],
  exports: [RouterModule],
})
export class MecanicoRoutingModule {}
