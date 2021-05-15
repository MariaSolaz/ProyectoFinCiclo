import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { EstadoVehiculo } from 'app/entities/enumerations/estado-vehiculo.model';
import { IRegistro, Registro } from '../registro.model';

import { RegistroService } from './registro.service';

describe('Service Tests', () => {
  describe('Registro Service', () => {
    let service: RegistroService;
    let httpMock: HttpTestingController;
    let elemDefault: IRegistro;
    let expectedResult: IRegistro | IRegistro[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RegistroService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        fecha: currentDate,
        estadoActual: EstadoVehiculo.NoRevisado,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fecha: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Registro', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fecha: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fecha: currentDate,
          },
          returnedFromService
        );

        service.create(new Registro()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Registro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fecha: currentDate.format(DATE_FORMAT),
            estadoActual: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fecha: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Registro', () => {
        const patchObject = Object.assign(
          {
            fecha: currentDate.format(DATE_FORMAT),
          },
          new Registro()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fecha: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Registro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fecha: currentDate.format(DATE_FORMAT),
            estadoActual: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fecha: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Registro', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRegistroToCollectionIfMissing', () => {
        it('should add a Registro to an empty array', () => {
          const registro: IRegistro = { id: 123 };
          expectedResult = service.addRegistroToCollectionIfMissing([], registro);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(registro);
        });

        it('should not add a Registro to an array that contains it', () => {
          const registro: IRegistro = { id: 123 };
          const registroCollection: IRegistro[] = [
            {
              ...registro,
            },
            { id: 456 },
          ];
          expectedResult = service.addRegistroToCollectionIfMissing(registroCollection, registro);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Registro to an array that doesn't contain it", () => {
          const registro: IRegistro = { id: 123 };
          const registroCollection: IRegistro[] = [{ id: 456 }];
          expectedResult = service.addRegistroToCollectionIfMissing(registroCollection, registro);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(registro);
        });

        it('should add only unique Registro to an array', () => {
          const registroArray: IRegistro[] = [{ id: 123 }, { id: 456 }, { id: 35265 }];
          const registroCollection: IRegistro[] = [{ id: 123 }];
          expectedResult = service.addRegistroToCollectionIfMissing(registroCollection, ...registroArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const registro: IRegistro = { id: 123 };
          const registro2: IRegistro = { id: 456 };
          expectedResult = service.addRegistroToCollectionIfMissing([], registro, registro2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(registro);
          expect(expectedResult).toContain(registro2);
        });

        it('should accept null and undefined values', () => {
          const registro: IRegistro = { id: 123 };
          expectedResult = service.addRegistroToCollectionIfMissing([], null, registro, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(registro);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
