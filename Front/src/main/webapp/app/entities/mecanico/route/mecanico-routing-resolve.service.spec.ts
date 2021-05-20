jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMecanico, Mecanico } from '../mecanico.model';
import { MecanicoService } from '../service/mecanico.service';

import { MecanicoRoutingResolveService } from './mecanico-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mecanico routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MecanicoRoutingResolveService;
    let service: MecanicoService;
    let resultMecanico: IMecanico | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MecanicoRoutingResolveService);
      service = TestBed.inject(MecanicoService);
      resultMecanico = undefined;
    });

    describe('resolve', () => {
      it('should return IMecanico returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMecanico = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMecanico).toEqual({ id: 123 });
      });

      it('should return new IMecanico if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMecanico = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMecanico).toEqual(new Mecanico());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMecanico = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMecanico).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
