import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMecanico, Mecanico } from '../mecanico.model';
import { MecanicoService } from '../service/mecanico.service';

@Component({
  selector: 'jhi-mecanico-update',
  templateUrl: './mecanico-update.component.html',
})
export class MecanicoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    apellido: [null, [Validators.required]],
    dNI: [null, [Validators.required]],
    telefono: [null, [Validators.required]],
    correo: [null, [Validators.required]],
  });

  constructor(protected mecanicoService: MecanicoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mecanico }) => {
      this.updateForm(mecanico);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mecanico = this.createFromForm();
    if (mecanico.id !== undefined) {
      this.subscribeToSaveResponse(this.mecanicoService.update(mecanico));
    } else {
      this.subscribeToSaveResponse(this.mecanicoService.create(mecanico));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMecanico>>): void {
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

  protected updateForm(mecanico: IMecanico): void {
    this.editForm.patchValue({
      id: mecanico.id,
      nombre: mecanico.nombre,
      apellido: mecanico.apellido,
      dNI: mecanico.dNI,
      telefono: mecanico.telefono,
      correo: mecanico.correo,
    });
  }

  protected createFromForm(): IMecanico {
    return {
      ...new Mecanico(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      dNI: this.editForm.get(['dNI'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      correo: this.editForm.get(['correo'])!.value,
    };
  }
}
