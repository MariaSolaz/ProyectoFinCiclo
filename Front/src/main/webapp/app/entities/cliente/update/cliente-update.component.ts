import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { VehiculoService } from 'app/entities/vehiculo/service/vehiculo.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  vehiculosSharedCollection: IVehiculo[] = [];

  editForm = this.fb.group({
    nombre: [null, [Validators.required]],
    apellido: [null, [Validators.required]],
    dNI: [null, [Validators.required]],
    telefono: [null, [Validators.required]],
    correo: [null, [Validators.required]],
    user: [],
    vehiculo: [],
  });

  constructor(
    protected clienteService: ClienteService,
    protected userService: UserService,
    protected vehiculoService: VehiculoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackVehiculoById(index: number, item: IVehiculo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      dNI: cliente.dNI,
      telefono: cliente.telefono,
      correo: cliente.correo,
      user: cliente.user,
      vehiculo: cliente.vehiculo,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, cliente.user);
    this.vehiculosSharedCollection = this.vehiculoService.addVehiculoToCollectionIfMissing(
      this.vehiculosSharedCollection,
      cliente.vehiculo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.vehiculoService
      .query({
        sort:['matricula', 'asc']
      })
      .pipe(map((res: HttpResponse<IVehiculo[]>) => res.body ?? []))
      .pipe(
        map((vehiculos: IVehiculo[]) =>
          this.vehiculoService.addVehiculoToCollectionIfMissing(vehiculos, this.editForm.get('vehiculo')!.value)
        )
      )
      .subscribe((vehiculos: IVehiculo[]) => (this.vehiculosSharedCollection = vehiculos));
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      dNI: this.editForm.get(['dNI'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      correo: this.editForm.get(['correo'])!.value,
      user: this.editForm.get(['user'])!.value,
      vehiculo: this.editForm.get(['vehiculo'])!.value,
    };
  }
}
