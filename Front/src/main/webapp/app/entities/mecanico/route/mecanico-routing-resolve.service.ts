import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMecanico, Mecanico } from '../mecanico.model';
import { MecanicoService } from '../service/mecanico.service';

@Injectable({ providedIn: 'root' })
export class MecanicoRoutingResolveService implements Resolve<IMecanico> {
  constructor(protected service: MecanicoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMecanico> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mecanico: HttpResponse<Mecanico>) => {
          if (mecanico.body) {
            return of(mecanico.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mecanico());
  }
}
