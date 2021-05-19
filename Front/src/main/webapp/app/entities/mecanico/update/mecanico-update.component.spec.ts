jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MecanicoService } from '../service/mecanico.service';
import { IMecanico, Mecanico } from '../mecanico.model';

import { MecanicoUpdateComponent } from './mecanico-update.component';

describe('Component Tests', () => {
  describe('Mecanico Management Update Component', () => {
    let comp: MecanicoUpdateComponent;
    let fixture: ComponentFixture<MecanicoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mecanicoService: MecanicoService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const mecanico: IMecanico = { id: 456 };

        activatedRoute.data = of({ mecanico });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mecanico));
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
  });
});
