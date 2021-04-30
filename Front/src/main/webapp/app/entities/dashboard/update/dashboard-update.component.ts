import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDashboard, Dashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';

@Component({
  selector: 'jhi-dashboard-update',
  templateUrl: './dashboard-update.component.html',
})
export class DashboardUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected dashboardService: DashboardService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dashboard }) => {
      this.updateForm(dashboard);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dashboard = this.createFromForm();
    if (dashboard.id !== undefined) {
      this.subscribeToSaveResponse(this.dashboardService.update(dashboard));
    } else {
      this.subscribeToSaveResponse(this.dashboardService.create(dashboard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDashboard>>): void {
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

  protected updateForm(dashboard: IDashboard): void {
    this.editForm.patchValue({
      id: dashboard.id,
    });
  }

  protected createFromForm(): IDashboard {
    return {
      ...new Dashboard(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
