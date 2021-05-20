import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DATE_FORMAT } from 'src/app/confing/input.constants';
import { getVehiculoIdentifier, IVehiculo } from 'src/app/interfaces';
import { environment } from 'src/environments/environment';
import { isPresent } from '../utils/operatos';
import { createRequestOption } from '../utils/request-utils';
import * as dayjs from 'dayjs';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<IVehiculo>;
export type EntityArrayResponseType = HttpResponse<IVehiculo[]>;

@Injectable({
  providedIn: 'root'
})
export class VehiculoService {
  public static API_URL = environment.apiUrl;

  constructor(public http: HttpClient) {}

  obtenerVehiculos(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehiculo[]>(VehiculoService.API_URL + "/vehiculos", { params: options, observe: 'response' });
  }

  create(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .post<IVehiculo>(VehiculoService.API_URL, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .put<IVehiculo>(`${VehiculoService.API_URL}/${getVehiculoIdentifier(vehiculo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .patch<IVehiculo>(`${VehiculoService.API_URL}/${getVehiculoIdentifier(vehiculo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVehiculo>(`${VehiculoService.API_URL}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehiculo[]>(VehiculoService.API_URL, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${VehiculoService.API_URL}/${id}`, { observe: 'response' });
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
