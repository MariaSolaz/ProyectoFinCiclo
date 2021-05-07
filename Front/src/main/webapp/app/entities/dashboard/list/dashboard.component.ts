import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';
import { IVehiculo} from '../../vehiculo/vehiculo.model';
import { VehiculoService} from '../../vehiculo/service/vehiculo.service';
import { DashboardDeleteDialogComponent } from '../delete/dashboard-delete-dialog.component';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  dashboards?: IDashboard[];
  isLoading = false;
  vehiculos: IVehiculo[] =[];

  constructor(protected dashboardService: DashboardService, protected modalService: NgbModal, protected vehiculoService: VehiculoService) {
    
  }

  loadAll(): void {
    this.isLoading = true;

    this.vehiculoService.query().subscribe(
      (res: HttpResponse<IVehiculo[]>) => {
        this.isLoading = false;
        this.vehiculos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDashboard): number {
    return item.id!;
  }

  delete(dashboard: IDashboard): void {
    const modalRef = this.modalService.open(DashboardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dashboard = dashboard;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
