import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistro } from '../registro.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RegistroService } from '../service/registro.service';
import { RegistroDeleteDialogComponent } from '../delete/registro-delete-dialog.component';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-registro',
  templateUrl: './registro.component.html',
})
export class RegistroComponent implements OnInit {
  registros?: IRegistro[];
  vehiculo?: IVehiculo; 
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  link:{[key:string]: number};

  constructor(
    protected registroService: RegistroService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected parseLink: ParseLinks,
  ) {
    this.registros = [];
    this.link ={
      last:0,
    };
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page =0 ;

    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll():void{
    this.isLoading = true;
    const filtros:Map<string,any> = new Map();
    filtros.set('vehiculoId.equals', this.vehiculo?.id);

    this.registroService
    .query({
      filter:filtros,
      page: this.page,
      sice: this.itemsPerPage,
      sort: this.sort(),
    })
    .subscribe(
      (res: HttpResponse<IRegistro[]>) =>{
        this.isLoading = false;
        this.paginateRegistro(res.body, res.headers);
      },
      ()=>{
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({vehiculo}) =>{
      this.vehiculo = vehiculo;
      /*eslint-disable*/
      console.log(vehiculo.id);
      this.loadAll();
    })
  
  }

  reset():void{
    this.page = 0;
    this.registros = [];
    this.loadAll();
  }

  loadPage(page: number):void{
    this.page = page;
    this.loadAll();

  }

  trackId(index: number, item: IRegistro): number {
    return item.id!;
  }

  delete(registro: IRegistro): void {
    const modalRef = this.modalService.open(RegistroDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.registro = registro;
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

  protected paginateRegistro(data: IRegistro[] | null, headers: HttpHeaders): void{
    this.link = this.parseLink.parse(headers.get('link') ?? '');
    if(data){
      for (const d of data){
        this.registros?.push(d);
      }
    }
  } 
}
