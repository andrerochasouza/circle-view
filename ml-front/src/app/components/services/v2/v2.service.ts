import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { Observable } from 'rxjs';
import { Base64 } from 'js-base64';
import { ResponseImage } from '../../model/response';
import { Response } from '../../model/response';


@Injectable({
  providedIn: 'root'
})
export class V2Service {

  private readonly API = `${environment.api}/v2`;
  private readonly USERNAME = 'andre.rsouza';
  private readonly PASSWORD = 'andre.rsouza123';
  private readonly AUTH = new HttpHeaders().set('Authorization', 'Basic ' + Base64.encode(this.USERNAME + ':' + this.PASSWORD));

  constructor(private http: HttpClient) { }

  enviaImagemCifar10Predict(file: File): Observable<Response<ResponseImage>> {
    return this.http.post<Response<ResponseImage>>(`${this.API}/cnn-cifar10/predict`, file, { headers: this.AUTH });
  }
}