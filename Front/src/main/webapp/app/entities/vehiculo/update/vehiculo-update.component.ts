import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVehiculo, Vehiculo } from '../vehiculo.model';
import { VehiculoService } from '../service/vehiculo.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IMecanico } from 'app/entities/mecanico/mecanico.model';
import { MecanicoService } from 'app/entities/mecanico/service/mecanico.service';

@Component({
  selector: 'jhi-vehiculo-update',
  templateUrl: './vehiculo-update.component.html',
})
export class VehiculoUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: ICliente[] = [];
  mecanicosSharedCollection: IMecanico[] = [];

  editForm = this.fb.group({
    id: [],
    matricula: [null, [Validators.required]],
    marca: [null, [Validators.required]],
    modelo: [null, [Validators.required]],
    anyo: [null, [Validators.required]],
    cliente: [],
    mecanico: [],
  });

  constructor(
    protected vehiculoService: VehiculoService,
    protected clienteService: ClienteService,
    protected mecanicoService: MecanicoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehiculo }) => {
      this.updateForm(vehiculo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehiculo = this.createFromForm();
    if (vehiculo.id !== undefined) {
      this.subscribeToSaveResponse(this.vehiculoService.update(vehiculo));
    } else {
      this.subscribeToSaveResponse(this.vehiculoService.create(vehiculo));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackMecanicoById(index: number, item: IMecanico): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehiculo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vehiculo: IVehiculo): void {
    this.editForm.patchValue({
      id: vehiculo.id,
      matricula: vehiculo.matricula,
      marca: vehiculo.marca,
      modelo: vehiculo.modelo,
      anyo: vehiculo.anyo,
      cliente: vehiculo.cliente,
      mecanico: vehiculo.mecanico,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, vehiculo.cliente);
    this.mecanicosSharedCollection = this.mecanicoService.addMecanicoToCollectionIfMissing(
      this.mecanicosSharedCollection,
      vehiculo.mecanico
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query({
        sort: ['nombre','asc']
      })
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.mecanicoService
      .query({
        sort: ['nombre','asc'],
      })
      .pipe(map((res: HttpResponse<IMecanico[]>) => res.body ?? []))
      .pipe(
        map((mecanicos: IMecanico[]) =>
          this.mecanicoService.addMecanicoToCollectionIfMissing(mecanicos, this.editForm.get('mecanico')!.value)
        )
      )
      .subscribe((mecanicos: IMecanico[]) => (this.mecanicosSharedCollection = mecanicos));
  }

  protected createFromForm(): IVehiculo {
    return {
      ...new Vehiculo(),
      id: this.editForm.get(['id'])!.value,
      matricula: this.editForm.get(['matricula'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      anyo: this.editForm.get(['anyo'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      mecanico: this.editForm.get(['mecanico'])!.value,
    };
  }
}
