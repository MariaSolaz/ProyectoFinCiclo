import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { isPresent } from '../utils/operatos';
import { createRequestOption } from '../utils/request-utils';
import { environment } from 'src/environments/environment';
import * as dayjs from 'dayjs';
import { DATE_FORMAT } from 'src/app/confing/input.constants';
import { IRegistro, getRegistroIdentifier } from 'src/app/interfaces';

export type EntityResponseType = HttpResponse<IRegistro>;
export type EntityArrayResponseType = HttpResponse<IRegistro[]>;

@Injectable({ providedIn: 'root' })
export class RegistroService {
    public static API_URL = environment.apiUrl + "/registros";

    constructor(public http: HttpClient) {}
  create(registro: IRegistro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registro);
    return this.http
      .post<IRegistro>(RegistroService.API_URL, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  obtenerRegistros(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegistro[]>(RegistroService.API_URL , { params: options, observe: 'response' });
  }

  update(registro: IRegistro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registro);
    return this.http
      .put<IRegistro>(`${RegistroService.API_URL}/${getRegistroIdentifier(registro) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(registro: IRegistro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registro);
    return this.http
      .patch<IRegistro>(`${RegistroService.API_URL}/${getRegistroIdentifier(registro) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegistro>(`${RegistroService.API_URL}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegistro[]>(RegistroService.API_URL, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${RegistroService.API_URL}/${id}`, { observe: 'response' });
  }

  addRegistroToCollectionIfMissing(registroCollection: IRegistro[], ...registrosToCheck: (IRegistro | null | undefined)[]): IRegistro[] {
    const registros: IRegistro[] = registrosToCheck.filter(isPresent);
    if (registros.length > 0) {
      const registroCollectionIdentifiers = registroCollection.map(registroItem => getRegistroIdentifier(registroItem)!);
      const registrosToAdd = registros.filter(registroItem => {
        const registroIdentifier = getRegistroIdentifier(registroItem);
        if (registroIdentifier == null || registroCollectionIdentifiers.includes(registroIdentifier)) {
          return false;
        }
        registroCollectionIdentifiers.push(registroIdentifier);
        return true;
      });
      return [...registrosToAdd, ...registroCollection];
    }
    return registroCollection;
  }

  protected convertDateFromClient(registro: IRegistro): IRegistro {
    return Object.assign({}, registro, {
      fecha: registro.fecha?.isValid() ? registro.fecha.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((registro: IRegistro) => {
        registro.fecha = registro.fecha ? dayjs(registro.fecha) : undefined;
      });
    }
    return res;
  }
}
