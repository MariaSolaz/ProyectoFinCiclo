import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { getClienteIdentifier, ICliente } from 'src/app/interfaces';
import { environment } from 'src/environments/environment';
import { isPresent } from '../utils/operatos';
import { createRequestOption } from '../utils/request-utils';

export type EntityResponseType = HttpResponse<ICliente>;
export type EntityArrayResponseType = HttpResponse<ICliente[]>;


@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  public static API_URL = environment.apiUrl;

  constructor(public http: HttpClient) {}

 

  obtenerClientes(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICliente[]>(ClienteService.API_URL + "/clientes", { params: options, observe: 'response' });
  }

  create(cliente: ICliente): Observable<EntityResponseType> {
    return this.http.post<ICliente>(ClienteService.API_URL, cliente, { observe: 'response' });
  }

  update(cliente: ICliente): Observable<EntityResponseType> {
    return this.http.put<ICliente>(`${ClienteService.API_URL}/${getClienteIdentifier(cliente) as number}`, cliente, { observe: 'response' });
  }

  partialUpdate(cliente: ICliente): Observable<EntityResponseType> {
    return this.http.patch<ICliente>(`${ClienteService.API_URL}/${getClienteIdentifier(cliente) as number}`, cliente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICliente>(`${ClienteService.API_URL}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICliente[]>(ClienteService.API_URL, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${ClienteService.API_URL}/${id}`, { observe: 'response' });
  }

  addClienteToCollectionIfMissing(clienteCollection: ICliente[], ...clientesToCheck: (ICliente | null | undefined)[]): ICliente[] {
    const clientes: ICliente[] = clientesToCheck.filter(isPresent);
    if (clientes.length > 0) {
      const clienteCollectionIdentifiers = clienteCollection.map(clienteItem => getClienteIdentifier(clienteItem)!);
      const clientesToAdd = clientes.filter(clienteItem => {
        const clienteIdentifier = getClienteIdentifier(clienteItem);
        if (clienteIdentifier == null || clienteCollectionIdentifiers.includes(clienteIdentifier)) {
          return false;
        }
        clienteCollectionIdentifiers.push(clienteIdentifier);
        return true;
      });
      return [...clientesToAdd, ...clienteCollection];
    }
    return clienteCollection;
  }


}
