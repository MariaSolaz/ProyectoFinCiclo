import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVehiculo, Vehiculo } from '../vehiculo.model';

import { VehiculoService } from './vehiculo.service';

describe('Service Tests', () => {
  describe('Vehiculo Service', () => {
    let service: VehiculoService;
    let httpMock: HttpTestingController;
    let elemDefault: IVehiculo;
    let expectedResult: IVehiculo | IVehiculo[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VehiculoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        matricula: 'AAAAAAA',
        marca: 'AAAAAAA',
        modelo: 'AAAAAAA',
        anyo: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            anyo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Vehiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            anyo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            anyo: currentDate,
          },
          returnedFromService
        );

        service.create(new Vehiculo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vehiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            matricula: 'BBBBBB',
            marca: 'BBBBBB',
            modelo: 'BBBBBB',
            anyo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            anyo: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Vehiculo', () => {
        const patchObject = Object.assign(
          {
            matricula: 'BBBBBB',
            modelo: 'BBBBBB',
          },
          new Vehiculo()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            anyo: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vehiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            matricula: 'BBBBBB',
            marca: 'BBBBBB',
            modelo: 'BBBBBB',
            anyo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            anyo: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Vehiculo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVehiculoToCollectionIfMissing', () => {
        it('should add a Vehiculo to an empty array', () => {
          const vehiculo: IVehiculo = { id: 123 };
          expectedResult = service.addVehiculoToCollectionIfMissing([], vehiculo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vehiculo);
        });

        it('should not add a Vehiculo to an array that contains it', () => {
          const vehiculo: IVehiculo = { id: 123 };
          const vehiculoCollection: IVehiculo[] = [
            {
              ...vehiculo,
            },
            { id: 456 },
          ];
          expectedResult = service.addVehiculoToCollectionIfMissing(vehiculoCollection, vehiculo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vehiculo to an array that doesn't contain it", () => {
          const vehiculo: IVehiculo = { id: 123 };
          const vehiculoCollection: IVehiculo[] = [{ id: 456 }];
          expectedResult = service.addVehiculoToCollectionIfMissing(vehiculoCollection, vehiculo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vehiculo);
        });

        it('should add only unique Vehiculo to an array', () => {
          const vehiculoArray: IVehiculo[] = [{ id: 123 }, { id: 456 }, { id: 50840 }];
          const vehiculoCollection: IVehiculo[] = [{ id: 123 }];
          expectedResult = service.addVehiculoToCollectionIfMissing(vehiculoCollection, ...vehiculoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vehiculo: IVehiculo = { id: 123 };
          const vehiculo2: IVehiculo = { id: 456 };
          expectedResult = service.addVehiculoToCollectionIfMissing([], vehiculo, vehiculo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vehiculo);
          expect(expectedResult).toContain(vehiculo2);
        });

        it('should accept null and undefined values', () => {
          const vehiculo: IVehiculo = { id: 123 };
          expectedResult = service.addVehiculoToCollectionIfMissing([], null, vehiculo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vehiculo);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
