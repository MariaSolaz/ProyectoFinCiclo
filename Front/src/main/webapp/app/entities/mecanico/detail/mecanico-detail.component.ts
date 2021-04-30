import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMecanico } from '../mecanico.model';

@Component({
  selector: 'jhi-mecanico-detail',
  templateUrl: './mecanico-detail.component.html',
})
export class MecanicoDetailComponent implements OnInit {
  mecanico: IMecanico | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mecanico }) => {
      this.mecanico = mecanico;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
