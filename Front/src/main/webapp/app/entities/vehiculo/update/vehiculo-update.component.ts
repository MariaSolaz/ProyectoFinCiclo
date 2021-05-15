import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVehiculo, Vehiculo } from '../vehiculo.model';
import { VehiculoService } from '../service/vehiculo.service';
import{ IRegistro, Registro} from '../../registro/registro.model';
import { RegistroService} from '../../registro/service/registro.service'
import * as dayjs from 'dayjs';


@Component({
  selector: 'jhi-vehiculo-update',
  templateUrl: './vehiculo-update.component.html',
})
export class VehiculoUpdateComponent implements OnInit {
  isSaving = false;
  registro: Registro = new Registro();

  editForm = this.fb.group({
    id: [],
    matricula: [null, [Validators.required]],
    marca: [null, [Validators.required]],
    modelo: [null, [Validators.required]],
    anyo: [null, [Validators.required]],
    estado: [],
  });

  constructor(protected vehiculoService: VehiculoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder, protected registroservice:RegistroService) {
   
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehiculo }) => {
      this.updateForm(vehiculo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  newRegistro():void{
    const registro = this.createRegistro();
    /*eslint-disable*/
    console.log(registro)
    this.subscribeToSaveResponse(this.vehiculoService.create(registro));
  }

  updateRegistro():void{
    const registro = this.createRegistro();
    /*eslint-disable*/
    console.log(registro)
    this.subscribeToSaveResponse(this.vehiculoService.update(registro));
  }

  save(): void {
    this.isSaving = true;
    const vehiculo = this.createFromForm();
    if (vehiculo.id !== undefined) {
      this.subscribeToSaveResponse(this.vehiculoService.update(vehiculo));
      this.updateRegistro();
    } else {
      this.subscribeToSaveResponse(this.vehiculoService.create(vehiculo));
      this.newRegistro();
    }
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
      estado: vehiculo.estado,
    });
  }

  protected createFromForm(): IVehiculo {
    return {
      ...new Vehiculo(),
      id: this.editForm.get(['id'])!.value,
      matricula: this.editForm.get(['matricula'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      anyo: this.editForm.get(['anyo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }

  protected createRegistro(): IRegistro{
    return{
      ...new Registro(),
      fecha: dayjs(new Date()),
      estadoActual: this.editForm.get(['estado'])!.value,
      vehiculo: this.editForm.get(['id'])!.value,
    }
  }
}
