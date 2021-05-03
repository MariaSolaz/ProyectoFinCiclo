import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';

import { FacturaDetailComponent } from './detail/factura-detail.component';
import { FacturaUpdateComponent } from './update/factura-update.component';
import { FacturaDeleteDialogComponent } from './delete/factura-delete-dialog.component';
import { FacturaRoutingModule } from './route/factura-routing.module';

@NgModule({
  imports: [SharedModule, FacturaRoutingModule],
  declarations: [ FacturaDetailComponent, FacturaUpdateComponent, FacturaDeleteDialogComponent],
  entryComponents: [FacturaDeleteDialogComponent],
})
export class FacturaModule {}
