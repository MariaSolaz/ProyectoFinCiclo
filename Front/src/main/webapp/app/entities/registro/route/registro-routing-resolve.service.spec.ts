jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRegistro, Registro } from '../registro.model';
import { RegistroService } from '../service/registro.service';

import { RegistroRoutingResolveService } from './registro-routing-resolve.service';

describe('Service Tests', () => {
  describe('Registro routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RegistroRoutingResolveService;
    let service: RegistroService;
    let resultRegistro: IRegistro | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RegistroRoutingResolveService);
      service = TestBed.inject(RegistroService);
      resultRegistro = undefined;
    });

    describe('resolve', () => {
      it('should return IRegistro returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRegistro = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRegistro).toEqual({ id: 123 });
      });

      it('should return new IRegistro if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRegistro = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRegistro).toEqual(new Registro());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRegistro = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRegistro).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
