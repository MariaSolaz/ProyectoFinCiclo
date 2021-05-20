import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MecanicoDetailComponent } from './mecanico-detail.component';

describe('Component Tests', () => {
  describe('Mecanico Management Detail Component', () => {
    let comp: MecanicoDetailComponent;
    let fixture: ComponentFixture<MecanicoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MecanicoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mecanico: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MecanicoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MecanicoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mecanico on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mecanico).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
