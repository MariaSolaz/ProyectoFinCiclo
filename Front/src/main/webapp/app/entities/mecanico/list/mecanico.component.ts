import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMecanico } from '../mecanico.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { MecanicoService } from '../service/mecanico.service';
import { MecanicoDeleteDialogComponent } from '../delete/mecanico-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-mecanico',
  templateUrl: './mecanico.component.html',
})
export class MecanicoComponent implements OnInit {
  mecanicos: IMecanico[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected mecanicoService: MecanicoService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.mecanicos = [];
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

    this.mecanicoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IMecanico[]>) => {
          this.isLoading = false;
          this.paginateMecanicos(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.mecanicos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
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

  protected paginateMecanicos(data: IMecanico[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.mecanicos.push(d);
      }
    }
  }
}
