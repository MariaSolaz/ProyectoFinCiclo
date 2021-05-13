import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map} from 'rxjs/operators';

import {IVehiculo, Vehiculo, IFactura, Factura} from '../../../../interfaces';
import { FacturaService } from 'src/app/services/Factura/factura.service';
import { VehiculoService } from 'src/app/services/Vehiculo/vehiculo.service';

@Component({
  selector: 'app-factura-update',
  templateUrl: './factura-update.html',
})
export class FacturaUpdate implements OnInit {
  isSaving = false;
  idVehiculo?:number;
  vehiculosSharedCollection: IVehiculo[] = [];

  /*editForm = this.fb.group({
    id: [],
    fecha: [null, [Validators.required]],
    diagnostico: [null, [Validators.required]],
    precio: [null, [Validators.required]],
    estado: [],
    vehiculo: [],
  });*/

  constructor(
    protected facturaService: FacturaService,
    protected vehiculoService: VehiculoService,
    protected activatedRoute: ActivatedRoute,
    //protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
   /* this.activatedRoute.data.subscribe(({ factura }) => {
      this.updateForm(factura);
      this.loadRelationshipsOptions();
    });

    if(this.activatedRoute.snapshot.params.idVehiculo){
      this.idVehiculo = this.activatedRoute.snapshot.params.idVehiculo;
    }*/
  }

 
 /* previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();

    if(this.idVehiculo){
        const vehiculo:Vehiculo = new Vehiculo();
        vehiculo.id = this.idVehiculo;
        factura.vehiculo = vehiculo;
    }
    
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  trackVehiculoById(index: number, item: IVehiculo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.editForm.patchValue({
      id: factura.id,
      fecha: factura.fecha,
      diagnostico: factura.diagnostico,
      precio: factura.precio,
      estado: factura.estado,
      vehiculo: factura.vehiculo,
    });

    this.vehiculosSharedCollection = this.vehiculoService.addVehiculoToCollectionIfMissing(
      this.vehiculosSharedCollection,
      factura.vehiculo
    );
  }

  protected loadRelationshipsOptions():void{
    this.vehiculoService
    .query({
      sort:['estado', 'asc']
    })
    .pipe(map((res:HttpResponse<IVehiculo[]>) => res.body ?? []))
    .pipe(
      map((vehiculos: IVehiculo[]) =>
        this.vehiculoService.addVehiculoToCollectionIfMissing(vehiculos, this.editForm.get('estado')!.value)
      )
    )
    .subscribe((vehiculos: IVehiculo[]) =>(this.vehiculosSharedCollection = vehiculos));

  }

  protected createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      diagnostico: this.editForm.get(['diagnostico'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      vehiculo: this.editForm.get(['vehiculo'])!.value,
    };
  }*/
}
