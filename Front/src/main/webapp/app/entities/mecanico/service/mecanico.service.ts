import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMecanico, getMecanicoIdentifier } from '../mecanico.model';

export type EntityResponseType = HttpResponse<IMecanico>;
export type EntityArrayResponseType = HttpResponse<IMecanico[]>;

@Injectable({ providedIn: 'root' })
export class MecanicoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/mecanicos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(mecanico: IMecanico): Observable<EntityResponseType> {
    return this.http.post<IMecanico>(this.resourceUrl, mecanico, { observe: 'response' });
  }

  update(mecanico: IMecanico): Observable<EntityResponseType> {
    return this.http.put<IMecanico>(`${this.resourceUrl}/${getMecanicoIdentifier(mecanico) as number}`, mecanico, { observe: 'response' });
  }

  partialUpdate(mecanico: IMecanico): Observable<EntityResponseType> {
    return this.http.patch<IMecanico>(`${this.resourceUrl}/${getMecanicoIdentifier(mecanico) as number}`, mecanico, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMecanico>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMecanico[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMecanicoToCollectionIfMissing(mecanicoCollection: IMecanico[], ...mecanicosToCheck: (IMecanico | null | undefined)[]): IMecanico[] {
    const mecanicos: IMecanico[] = mecanicosToCheck.filter(isPresent);
    if (mecanicos.length > 0) {
      const mecanicoCollectionIdentifiers = mecanicoCollection.map(mecanicoItem => getMecanicoIdentifier(mecanicoItem)!);
      const mecanicosToAdd = mecanicos.filter(mecanicoItem => {
        const mecanicoIdentifier = getMecanicoIdentifier(mecanicoItem);
        if (mecanicoIdentifier == null || mecanicoCollectionIdentifiers.includes(mecanicoIdentifier)) {
          return false;
        }
        mecanicoCollectionIdentifiers.push(mecanicoIdentifier);
        return true;
      });
      return [...mecanicosToAdd, ...mecanicoCollection];
    }
    return mecanicoCollection;
  }
}
