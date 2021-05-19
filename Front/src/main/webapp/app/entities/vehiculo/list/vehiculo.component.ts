import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehiculo } from '../vehiculo.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { VehiculoService } from '../service/vehiculo.service';
import { VehiculoDeleteDialogComponent } from '../delete/vehiculo-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-vehiculo',
  templateUrl: './vehiculo.component.html',
})
export class VehiculoComponent implements OnInit {
  vehiculos: IVehiculo[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  idCliente?:number;

  constructor(
    protected vehiculoService: VehiculoService, 
    protected modalService: NgbModal, 
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,

    ) {
    this.vehiculos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'matricula';
    this.ascending = true;
  }

  loadVehiculosCliente(): void {
    this.isLoading = true;
    const filtros:Map<string,any> = new Map();
    filtros.set('clienteId.equals', this.idCliente);
    this.vehiculoService
      .query({
        filter: filtros,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IVehiculo[]>) => {
          this.isLoading = false;
          this.paginateVehiculos(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  loadVehiculos():void{
    this.isLoading = true;
    this.vehiculoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IVehiculo[]>) => {
          this.isLoading = false;
          this.paginateVehiculos(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.vehiculos = [];
    this.loadVehiculosCliente();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadVehiculosCliente();
  }

  ngOnInit(): void {
    this.idCliente = this.activatedRoute.snapshot.params.idCliente;
    if(this.idCliente != null){
      this.loadVehiculosCliente();
    }else{
      this.loadVehiculos();
    }
  }

  trackId(index: number, item: IVehiculo): number {
    return item.id!;
  }

  delete(vehiculo: IVehiculo): void {
    const modalRef = this.modalService.open(VehiculoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vehiculo = vehiculo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVehiculos(data: IVehiculo[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.vehiculos.push(d);
      }
    }
  }
}
