jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VehiculoService } from '../service/vehiculo.service';
import { IVehiculo, Vehiculo } from '../vehiculo.model';

import { VehiculoUpdateComponent } from './vehiculo-update.component';

describe('Component Tests', () => {
  describe('Vehiculo Management Update Component', () => {
    let comp: VehiculoUpdateComponent;
    let fixture: ComponentFixture<VehiculoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vehiculoService: VehiculoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VehiculoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VehiculoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VehiculoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vehiculoService = TestBed.inject(VehiculoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const vehiculo: IVehiculo = { id: 456 };

        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vehiculo));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vehiculo = { id: 123 };
        spyOn(vehiculoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vehiculo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vehiculoService.update).toHaveBeenCalledWith(vehiculo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vehiculo = new Vehiculo();
        spyOn(vehiculoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vehiculo }));
        saveSubject.complete();

        // THEN
        expect(vehiculoService.create).toHaveBeenCalledWith(vehiculo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vehiculo = { id: 123 };
        spyOn(vehiculoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vehiculoService.update).toHaveBeenCalledWith(vehiculo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
