import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehiculo } from '../vehiculo.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { VehiculoService } from '../service/vehiculo.service';
import { VehiculoDeleteDialogComponent } from '../delete/vehiculo-delete-dialog.component';


import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-vehiculo',
  templateUrl: './vehiculo.component.html',
})
export class VehiculoComponent implements OnInit {
  vehiculos?: IVehiculo[];
  isLoading = false;
  itemsPerPage?: number;
  page?: number;
  predicate?: string;
  ascending?: boolean;
  link:{[key:string]: number};
 
  constructor(
    protected vehiculoService: VehiculoService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    protected parseLink: ParseLinks,

  ) {
    this.vehiculos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page =0 ;
    this.link ={
      last:0,
    };

    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll():void{
    this.isLoading = true;
 

    this.vehiculoService
    .query({
      page: this.page,
      sice: this.itemsPerPage,
      sort:['marca','asc']
    })
    .subscribe(
      (res: HttpResponse<IVehiculo[]>) =>{
        this.isLoading = false;
        this.paginateVehiculo(res.body, res.headers);
      },
      ()=>{
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  reset():void{
    this.page = 0;
    this.vehiculos = [];
    this.loadAll();
  }

  loadPage(page: number):void{
    this.page = page;
    this.loadAll();

  }

  trackId(index: number, item: IVehiculo): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
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

 


  protected paginateVehiculo(data: IVehiculo[] | null, headers: HttpHeaders): void{
    this.link = this.parseLink.parse(headers.get('link') ?? '');
    if(data){
      for (const d of data){
        this.vehiculos?.push(d);
      }
    }
  } 
}
