import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MecanicoComponent } from './list/mecanico.component';
import { MecanicoDetailComponent } from './detail/mecanico-detail.component';
import { MecanicoUpdateComponent } from './update/mecanico-update.component';
import { MecanicoDeleteDialogComponent } from './delete/mecanico-delete-dialog.component';
import { MecanicoRoutingModule } from './route/mecanico-routing.module';

@NgModule({
  imports: [SharedModule, MecanicoRoutingModule],
  declarations: [MecanicoComponent, MecanicoDetailComponent, MecanicoUpdateComponent, MecanicoDeleteDialogComponent],
  entryComponents: [MecanicoDeleteDialogComponent],
})
export class MecanicoModule {}
