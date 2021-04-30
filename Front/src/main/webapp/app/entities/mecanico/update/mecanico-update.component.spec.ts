jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MecanicoService } from '../service/mecanico.service';
import { IMecanico, Mecanico } from '../mecanico.model';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { VehiculoService } from 'app/entities/vehiculo/service/vehiculo.service';

import { MecanicoUpdateComponent } from './mecanico-update.component';

describe('Component Tests', () => {
  describe('Mecanico Management Update Component', () => {
    let comp: MecanicoUpdateComponent;
    let fixture: ComponentFixture<MecanicoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mecanicoService: MecanicoService;
    let vehiculoService: VehiculoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MecanicoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MecanicoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MecanicoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mecanicoService = TestBed.inject(MecanicoService);
      vehiculoService = TestBed.inject(VehiculoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vehiculo query and add missing value', () => {
        const mecanico: IMecanico = { id: 456 };
        const vehiculo: IVehiculo = { id: 11508 };
        mecanico.vehiculo = vehiculo;

        const vehiculoCollection: IVehiculo[] = [{ id: 15097 }];
        spyOn(vehiculoService, 'query').and.returnValue(of(new HttpResponse({ body: vehiculoCollection })));
        const additionalVehiculos = [vehiculo];
        const expectedCollection: IVehiculo[] = [...additionalVehiculos, ...vehiculoCollection];
        spyOn(vehiculoService, 'addVehiculoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mecanico });
        comp.ngOnInit();

        expect(vehiculoService.query).toHaveBeenCalled();
        expect(vehiculoService.addVehiculoToCollectionIfMissing).toHaveBeenCalledWith(vehiculoCollection, ...additionalVehiculos);
        expect(comp.vehiculosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mecanico: IMecanico = { id: 456 };
        const vehiculo: IVehiculo = { id: 12088 };
        mecanico.vehiculo = vehiculo;

        activatedRoute.data = of({ mecanico });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mecanico));
        expect(comp.vehiculosSharedCollection).toContain(vehiculo);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mecanico = { id: 123 };
        spyOn(mecanicoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mecanico });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mecanico }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mecanicoService.update).toHaveBeenCalledWith(mecanico);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mecanico = new Mecanico();
        spyOn(mecanicoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mecanico });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mecanico }));
        saveSubject.complete();

        // THEN
        expect(mecanicoService.create).toHaveBeenCalledWith(mecanico);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mecanico = { id: 123 };
        spyOn(mecanicoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mecanico });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mecanicoService.update).toHaveBeenCalledWith(mecanico);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVehiculoById', () => {
        it('Should return tracked Vehiculo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVehiculoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
