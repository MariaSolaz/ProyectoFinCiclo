import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICliente, Cliente } from '../../../../interfaces';
import { ClienteService } from '../../../../services/Cliente/cliente.service';

@Injectable({ providedIn: 'root' })
export class ClienteRoutingResolveService implements Resolve<ICliente> {
  constructor(protected service: ClienteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICliente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cliente: HttpResponse<Cliente>) => {
          if (cliente.body) {
            return of(cliente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cliente());
  }
}