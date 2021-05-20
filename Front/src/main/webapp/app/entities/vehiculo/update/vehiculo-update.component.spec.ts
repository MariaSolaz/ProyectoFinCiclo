jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VehiculoService } from '../service/vehiculo.service';
import { IVehiculo, Vehiculo } from '../vehiculo.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IMecanico } from 'app/entities/mecanico/mecanico.model';
import { MecanicoService } from 'app/entities/mecanico/service/mecanico.service';

import { VehiculoUpdateComponent } from './vehiculo-update.component';

describe('Component Tests', () => {
  describe('Vehiculo Management Update Component', () => {
    let comp: VehiculoUpdateComponent;
    let fixture: ComponentFixture<VehiculoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vehiculoService: VehiculoService;
    let clienteService: ClienteService;
    let mecanicoService: MecanicoService;

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
      clienteService = TestBed.inject(ClienteService);
      mecanicoService = TestBed.inject(MecanicoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Cliente query and add missing value', () => {
        const vehiculo: IVehiculo = { id: 456 };
        const cliente: ICliente = { id: 8136 };
        vehiculo.cliente = cliente;

        const clienteCollection: ICliente[] = [{ id: 62330 }];
        spyOn(clienteService, 'query').and.returnValue(of(new HttpResponse({ body: clienteCollection })));
        const additionalClientes = [cliente];
        const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
        spyOn(clienteService, 'addClienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        expect(clienteService.query).toHaveBeenCalled();
        expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
        expect(comp.clientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Mecanico query and add missing value', () => {
        const vehiculo: IVehiculo = { id: 456 };
        const mecanico: IMecanico = { id: 58196 };
        vehiculo.mecanico = mecanico;

        const mecanicoCollection: IMecanico[] = [{ id: 64716 }];
        spyOn(mecanicoService, 'query').and.returnValue(of(new HttpResponse({ body: mecanicoCollection })));
        const additionalMecanicos = [mecanico];
        const expectedCollection: IMecanico[] = [...additionalMecanicos, ...mecanicoCollection];
        spyOn(mecanicoService, 'addMecanicoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        expect(mecanicoService.query).toHaveBeenCalled();
        expect(mecanicoService.addMecanicoToCollectionIfMissing).toHaveBeenCalledWith(mecanicoCollection, ...additionalMecanicos);
        expect(comp.mecanicosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vehiculo: IVehiculo = { id: 456 };
        const cliente: ICliente = { id: 48385 };
        vehiculo.cliente = cliente;
        const mecanico: IMecanico = { id: 21342 };
        vehiculo.mecanico = mecanico;

        activatedRoute.data = of({ vehiculo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vehiculo));
        expect(comp.clientesSharedCollection).toContain(cliente);
        expect(comp.mecanicosSharedCollection).toContain(mecanico);
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

    describe('Tracking relationships identifiers', () => {
      describe('trackClienteById', () => {
        it('Should return tracked Cliente primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClienteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMecanicoById', () => {
        it('Should return tracked Mecanico primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMecanicoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
