import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ICliente } from 'src/app/interfaces';
import { environment } from 'src/environments/environment';
import { createRequestOption } from '../utils/request-utils';

export type EntityResponseType = HttpResponse<ICliente>;
export type EntityArrayResponseType = HttpResponse<ICliente[]>;


@Injectable({
  providedIn: 'root'
})
export class ApiService {
  public static API_URL = environment.apiUrl;

  constructor(public http: HttpClient) {}

  get(endpoint: string, params?: any, reqOpts?: any) {
    if (!reqOpts) {
      reqOpts = {
        params: new HttpParams()
      };
    }

    // Support easy query params for GET requests
    if (params) {
      reqOpts.params = new HttpParams();
      for (let k in params) {
        reqOpts.params.set(k, params[k]);
      }
    }

    return this.http.get(ApiService.API_URL + '/' + endpoint, reqOpts);
  }

  post(endpoint: string, body: any, reqOpts?: any) {
    return this.http.post(ApiService.API_URL + '/' + endpoint, body, reqOpts);
  }

  put(endpoint: string, body: any, reqOpts?: any) {
    return this.http.put(ApiService.API_URL + '/' + endpoint, body, reqOpts);
  }

  delete(endpoint: string, reqOpts?: any) {
    return this.http.delete(ApiService.API_URL + '/' + endpoint, reqOpts);
  }

  patch(endpoint: string, body: any, reqOpts?: any) {
    return this.http.put(ApiService.API_URL + '/' + endpoint, body, reqOpts);
  }

}
