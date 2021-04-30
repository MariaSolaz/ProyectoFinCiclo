import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMecanico, Mecanico } from '../mecanico.model';

import { MecanicoService } from './mecanico.service';

describe('Service Tests', () => {
  describe('Mecanico Service', () => {
    let service: MecanicoService;
    let httpMock: HttpTestingController;
    let elemDefault: IMecanico;
    let expectedResult: IMecanico | IMecanico[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MecanicoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        apellido: 'AAAAAAA',
        dNI: 'AAAAAAA',
        telefono: 'AAAAAAA',
        correo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Mecanico', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Mecanico()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mecanico', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
            dNI: 'BBBBBB',
            telefono: 'BBBBBB',
            correo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Mecanico', () => {
        const patchObject = Object.assign(
          {
            telefono: 'BBBBBB',
          },
          new Mecanico()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mecanico', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
            dNI: 'BBBBBB',
            telefono: 'BBBBBB',
            correo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Mecanico', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMecanicoToCollectionIfMissing', () => {
        it('should add a Mecanico to an empty array', () => {
          const mecanico: IMecanico = { id: 123 };
          expectedResult = service.addMecanicoToCollectionIfMissing([], mecanico);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mecanico);
        });

        it('should not add a Mecanico to an array that contains it', () => {
          const mecanico: IMecanico = { id: 123 };
          const mecanicoCollection: IMecanico[] = [
            {
              ...mecanico,
            },
            { id: 456 },
          ];
          expectedResult = service.addMecanicoToCollectionIfMissing(mecanicoCollection, mecanico);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mecanico to an array that doesn't contain it", () => {
          const mecanico: IMecanico = { id: 123 };
          const mecanicoCollection: IMecanico[] = [{ id: 456 }];
          expectedResult = service.addMecanicoToCollectionIfMissing(mecanicoCollection, mecanico);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mecanico);
        });

        it('should add only unique Mecanico to an array', () => {
          const mecanicoArray: IMecanico[] = [{ id: 123 }, { id: 456 }, { id: 53261 }];
          const mecanicoCollection: IMecanico[] = [{ id: 123 }];
          expectedResult = service.addMecanicoToCollectionIfMissing(mecanicoCollection, ...mecanicoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mecanico: IMecanico = { id: 123 };
          const mecanico2: IMecanico = { id: 456 };
          expectedResult = service.addMecanicoToCollectionIfMissing([], mecanico, mecanico2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mecanico);
          expect(expectedResult).toContain(mecanico2);
        });

        it('should accept null and undefined values', () => {
          const mecanico: IMecanico = { id: 123 };
          expectedResult = service.addMecanicoToCollectionIfMissing([], null, mecanico, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mecanico);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
