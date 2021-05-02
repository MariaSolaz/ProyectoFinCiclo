jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClienteService } from '../service/cliente.service';
import { ICliente, Cliente } from '../cliente.model';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { VehiculoService } from 'app/entities/vehiculo/service/vehiculo.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Component Tests', () => {
  describe('Cliente Management Update Component', () => {
    let comp: ClienteUpdateComponent;
    let fixture: ComponentFixture<ClienteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clienteService: ClienteService;
    let vehiculoService: VehiculoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClienteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClienteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClienteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clienteService = TestBed.inject(ClienteService);
      vehiculoService = TestBed.inject(VehiculoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vehiculo query and add missing value', () => {
        const cliente: ICliente = { id: 456 };
        const vehiculo: IVehiculo = { id: 88029 };
        cliente.vehiculo = vehiculo;

        const vehiculoCollection: IVehiculo[] = [{ id: 30041 }];
        spyOn(vehiculoService, 'query').and.returnValue(of(new HttpResponse({ body: vehiculoCollection })));
        const additionalVehiculos = [vehiculo];
        const expectedCollection: IVehiculo[] = [...additionalVehiculos, ...vehiculoCollection];
        spyOn(vehiculoService, 'addVehiculoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        expect(vehiculoService.query).toHaveBeenCalled();
        expect(vehiculoService.addVehiculoToCollectionIfMissing).toHaveBeenCalledWith(vehiculoCollection, ...additionalVehiculos);
        expect(comp.vehiculosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cliente: ICliente = { id: 456 };
        const vehiculo: IVehiculo = { id: 44927 };
        cliente.vehiculo = vehiculo;

        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cliente));
        expect(comp.vehiculosSharedCollection).toContain(vehiculo);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cliente = { id: 123 };
        spyOn(clienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cliente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clienteService.update).toHaveBeenCalledWith(cliente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cliente = new Cliente();
        spyOn(clienteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cliente }));
        saveSubject.complete();

        // THEN
        expect(clienteService.create).toHaveBeenCalledWith(cliente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cliente = { id: 123 };
        spyOn(clienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clienteService.update).toHaveBeenCalledWith(cliente);
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
