import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';
import { DashboardDeleteDialogComponent } from '../delete/dashboard-delete-dialog.component';

import { ActivatedRoute } from '@angular/router';
import { Vehiculo } from '../../vehiculo/vehiculo.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  dashboards?: IDashboard[];
  isLoading = false;
  vehiculos: Vehiculo[] = [];
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  parseLinks: any;

  constructor(protected dashboardService: DashboardService, protected modalService: NgbModal, protected activatedRoute:ActivatedRoute) {
    this.vehiculos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;
    
    this.dashboardService
    .query({
      page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
    })
    .subscribe(
      (res: HttpResponse<IDashboard[]>) => {
        this.isLoading = false;
        this.paginateDasboard(res.body, res.headers);
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({vehiculo}) =>{
      this.vehiculos.push(vehiculo);
      this.loadAll();
    })

      
    
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

  protected paginateDasboard(data: IDashboard[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.vehiculos.push(d);
      }
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
