import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { FrameRequest, FramesRequest } from 'src/app/components/model/frameRequest';
import { Observable } from 'rxjs';
import { ResponseFeedFoward } from 'src/app/components/model/response';
import { ResponseTrain } from 'src/app/components/model/response';
import { ResponseList } from 'src/app/components/model/response';
import { Base64 } from 'js-base64';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  private readonly API = `${environment.api}`;
  private readonly USERNAME = 'andre.rsouza';
  private readonly PASSWORD = 'andre.rsouza123';
  private readonly AUTH = new HttpHeaders().set('Authorization', 'Basic ' + Base64.encode(this.USERNAME + ':' + this.PASSWORD));

  constructor(private http: HttpClient) { }

  enviaImagemFeedfoward(frame: FrameRequest, uuid: string): Observable<any> {
    console.log(frame);
    return this.http.post<any>(`${this.API}/feedfoward?uuid=${uuid}`, frame);
  }

  enviaImagemTrain(frame: FramesRequest, hiddenNodesSize: number, epochs: number, learningRate: number, uuid: string): Observable<ResponseTrain> {
    console.log(frame);
    const url: string = `${this.API}/train?hiddenNodesSize=${hiddenNodesSize}&epochs=${epochs}&learningRate=${learningRate}&uuid=${uuid}`;
    return this.http.post<ResponseTrain>(url, frame);
  }

  getUUIDs(): Observable<ResponseList> {
    return this.http.get<ResponseList>(`${this.API}/neuralnetwork/list`, { headers: this.AUTH });
  }
}
