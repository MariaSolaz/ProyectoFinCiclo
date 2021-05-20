import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactura } from '../factura.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FacturaService } from '../service/factura.service';
import { FacturaDeleteDialogComponent } from '../delete/factura-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { ParseLinks } from 'app/core/util/parse-links.service';




@Component({
  selector: 'jhi-factura',
  templateUrl: './factura.component.html',
})
export class FacturaComponent implements OnInit {
  facturas?: IFactura[];
  isLoading = false;
  itemsPerPage?: number;
  page?: number;
  predicate?: string;
  ascending?: boolean;
  link:{[key:string]: number};
 
  vehiculo?: IVehiculo;

  constructor(
    protected facturaService: FacturaService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    protected parseLink: ParseLinks,

  ) {
    this.facturas = [];
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
    const filtros:Map<string,any> = new Map();
    filtros.set('vehiculoId.equals', this.vehiculo?.id);

    this.facturaService
    .query({
      filter:filtros,
      page: this.page,
      sice: this.itemsPerPage,
      sort: this.sort(),
    })
    .subscribe(
      (res: HttpResponse<IFactura[]>) =>{
        this.isLoading = false;
        this.paginateFacturas(res.body, res.headers);
      },
      ()=>{
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({vehiculo})=>{
      this.vehiculo = vehiculo;
      /*eslint-disable*/
      console.log(vehiculo.id);
      this.loadAll();
    })
    
  }

  reset():void{
    this.page = 0;
    this.facturas = [];
    this.loadAll();
  }

  loadPage(page: number):void{
    this.page = page;
    this.loadAll();

  }

  trackId(index: number, item: IFactura): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(factura: IFactura): void {
    const modalRef = this.modalService.open(FacturaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.factura = factura;
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


  protected paginateFacturas(data: IFactura[] | null, headers: HttpHeaders): void{
    this.link = this.parseLink.parse(headers.get('link') ?? '');
    if(data){
      for (const d of data){
        this.facturas?.push(d);
      }
    }
  } 
}
