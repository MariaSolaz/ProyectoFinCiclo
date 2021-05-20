import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMecanico } from '../mecanico.model';
import { MecanicoService } from '../service/mecanico.service';

@Component({
  templateUrl: './mecanico-delete-dialog.component.html',
})
export class MecanicoDeleteDialogComponent {
  mecanico?: IMecanico;

  constructor(protected mecanicoService: MecanicoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mecanicoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
