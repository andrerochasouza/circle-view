import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { Observable } from 'rxjs';
import { Base64 } from 'js-base64';

import { FeedfowardBody } from 'src/app/components/model/feedfowardBody';
import { TrainBody } from 'src/app/components/model/trainBody';
import { Response, ResponseFeedfoward, ResponseTrain } from '../../model/response';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private readonly API = `${environment.api}`;
  private readonly USERNAME = 'andre.rsouza';
  private readonly PASSWORD = 'andre.rsouza123';
  private readonly AUTH = new HttpHeaders().set('Authorization', 'Basic ' + Base64.encode(this.USERNAME + ':' + this.PASSWORD));

  constructor(private http: HttpClient) { }

  enviaImagemFeedfoward(feedfowardBody: FeedfowardBody, uuid: string): Observable<Response<ResponseFeedfoward>> {
    console.log(feedfowardBody);
    return this.http.post<Response<ResponseFeedfoward>>(`${this.API}/v1/feedfoward?uuid=${uuid}`, feedfowardBody);
  }

  enviaImagemTrain(trainBody: TrainBody, hiddenNodesSize1: number, hiddenNodesSize2: number, learningRate: number, uuid: string): Observable<Response<ResponseTrain>> {

    console.log(trainBody);
    if(uuid == '' || uuid == undefined || uuid == null){
      const url: string = `${this.API}/v1/train?hiddenNodesSize1=${hiddenNodesSize1}&hiddenNodesSize2=${hiddenNodesSize2}&learningRate=${learningRate}`;
      return this.http.post<Response<ResponseTrain>>(url, trainBody);
    }
    const url: string = `${this.API}/v1/train?hiddenNodesSize1=${hiddenNodesSize1}&hiddenNodesSize2=${hiddenNodesSize2}&learningRate=${learningRate}&uuid=${uuid}`;
    return this.http.post<Response<ResponseTrain>>(url, trainBody);
  }
}
