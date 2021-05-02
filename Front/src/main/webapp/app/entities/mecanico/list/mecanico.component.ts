import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMecanico } from '../mecanico.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { MecanicoService } from '../service/mecanico.service';
import { MecanicoDeleteDialogComponent } from '../delete/mecanico-delete-dialog.component';
import { FormBuilder } from '@angular/forms';
import { MecanicoFilter } from './mecanico.filter';
@Component({
  selector: 'jhi-mecanico',
  templateUrl: './mecanico.component.html',
})
export class MecanicoComponent implements OnInit {
  mecanicos?: IMecanico[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  filterForm = this.fb.group({
    filterNombre: [],
    filterApellido: []
  })

  filtros: MecanicoFilter = new MecanicoFilter();

  constructor(
    protected mecanicoService: MecanicoService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  filter():void{
    this.createFilterFromForm();
    this.loadPage();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.mecanicoService
      .query({
        filter: this.filtros.toMap(),
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IMecanico[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IMecanico): number {
    return item.id!;
  }

  delete(mecanico: IMecanico): void {
    const modalRef = this.modalService.open(MecanicoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mecanico = mecanico;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
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

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IMecanico[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/mecanico'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.mecanicos = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected createFilterFromForm():void{
    this.filtros.nombre = this.filterForm.get(['filterNombre'])?.value;
    this.filtros.apellido = this.filterForm.get(['filterApellido'])?.value
  }
}
