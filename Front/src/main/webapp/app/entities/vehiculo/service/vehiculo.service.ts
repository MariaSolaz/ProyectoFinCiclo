import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehiculo, getVehiculoIdentifier } from '../vehiculo.model';

export type EntityResponseType = HttpResponse<IVehiculo>;
export type EntityArrayResponseType = HttpResponse<IVehiculo[]>;

@Injectable({ providedIn: 'root' })
export class VehiculoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/vehiculos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .post<IVehiculo>(this.resourceUrl , copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .put<IVehiculo>(`${this.resourceUrl}/${getVehiculoIdentifier(vehiculo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .patch<IVehiculo>(`${this.resourceUrl}/${getVehiculoIdentifier(vehiculo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVehiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVehiculoToCollectionIfMissing(vehiculoCollection: IVehiculo[], ...vehiculosToCheck: (IVehiculo | null | undefined)[]): IVehiculo[] {
    const vehiculos: IVehiculo[] = vehiculosToCheck.filter(isPresent);
    if (vehiculos.length > 0) {
      const vehiculoCollectionIdentifiers = vehiculoCollection.map(vehiculoItem => getVehiculoIdentifier(vehiculoItem)!);
      const vehiculosToAdd = vehiculos.filter(vehiculoItem => {
        const vehiculoIdentifier = getVehiculoIdentifier(vehiculoItem);
        if (vehiculoIdentifier == null || vehiculoCollectionIdentifiers.includes(vehiculoIdentifier)) {
          return false;
        }
        vehiculoCollectionIdentifiers.push(vehiculoIdentifier);
        return true;
      });
      return [...vehiculosToAdd, ...vehiculoCollection];
    }
    return vehiculoCollection;
  }

  protected convertDateFromClient(vehiculo: IVehiculo): IVehiculo {
    return Object.assign({}, vehiculo, {
      anyo: vehiculo.anyo?.isValid() ? vehiculo.anyo.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.anyo = res.body.anyo ? dayjs(res.body.anyo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vehiculo: IVehiculo) => {
        vehiculo.anyo = vehiculo.anyo ? dayjs(vehiculo.anyo) : undefined;
      });
    }
    return res;
  }
}
