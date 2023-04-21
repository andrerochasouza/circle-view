import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { Observable } from 'rxjs';
import { Base64 } from 'js-base64';

import { Response, ResponseNeuralNetwork, ResponseUUIDs } from '../model/response';

@Injectable({
  providedIn: 'root'
})
export class NeuralnetworkService {

  private readonly API = `${environment.api}/neuralnetwork`;
  private readonly USERNAME = 'andre.rsouza';
  private readonly PASSWORD = 'andre.rsouza123';
  private readonly AUTH = new HttpHeaders().set('Authorization', 'Basic ' + Base64.encode(this.USERNAME + ':' + this.PASSWORD));

  constructor(private http: HttpClient) { }

  getUUIDs(): Observable<Response<ResponseUUIDs>> {
    return this.http.get<Response<ResponseUUIDs>>(`${this.API}/list`, { headers: this.AUTH });
  }

  getNeuralNetworkByUUID(uuid: string): Observable<Response<ResponseNeuralNetwork>> {
    return this.http.get<Response<ResponseNeuralNetwork>>(`${this.API}/list/${uuid}`, { headers: this.AUTH });
  }
}
