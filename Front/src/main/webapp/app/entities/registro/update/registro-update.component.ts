import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegistro, Registro } from '../registro.model';
import { RegistroService } from '../service/registro.service';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { VehiculoService } from 'app/entities/vehiculo/service/vehiculo.service';

@Component({
  selector: 'jhi-registro-update',
  templateUrl: './registro-update.component.html',
})
export class RegistroUpdateComponent implements OnInit {
  isSaving = false;

  vehiculosSharedCollection: IVehiculo[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [null, [Validators.required]],
    estadoActual: [null, [Validators.required]],
    vehiculo: [],
  });

  constructor(
    protected registroService: RegistroService,
    protected vehiculoService: VehiculoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registro }) => {
      this.updateForm(registro);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registro = this.createFromForm();
    if (registro.id !== undefined) {
      this.subscribeToSaveResponse(this.registroService.update(registro));
    } else {
      this.subscribeToSaveResponse(this.registroService.create(registro));
    }
  }

  trackVehiculoById(index: number, item: IVehiculo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistro>>): void {
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

  protected updateForm(registro: IRegistro): void {
    this.editForm.patchValue({
      id: registro.id,
      fecha: registro.fecha,
      estadoActual: registro.estadoActual,
      vehiculo: registro.vehiculo,
    });

    this.vehiculosSharedCollection = this.vehiculoService.addVehiculoToCollectionIfMissing(
      this.vehiculosSharedCollection,
      registro.vehiculo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vehiculoService
      .query()
      .pipe(map((res: HttpResponse<IVehiculo[]>) => res.body ?? []))
      .pipe(
        map((vehiculos: IVehiculo[]) =>
          this.vehiculoService.addVehiculoToCollectionIfMissing(vehiculos, this.editForm.get('vehiculo')!.value)
        )
      )
      .subscribe((vehiculos: IVehiculo[]) => (this.vehiculosSharedCollection = vehiculos));
  }

  protected createFromForm(): IRegistro {
    return {
      ...new Registro(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      estadoActual: this.editForm.get(['estadoActual'])!.value,
      vehiculo: this.editForm.get(['vehiculo'])!.value,
    };
  }
}
