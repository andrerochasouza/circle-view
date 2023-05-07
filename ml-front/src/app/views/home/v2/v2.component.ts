import { HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { PoUploadFileRestrictions } from '@po-ui/ng-components';
import { take } from 'rxjs';
import { Response, ResponseImage } from 'src/app/components/model/response';
import { V2Service } from 'src/app/components/services/v2/v2.service';
import { environment } from 'src/environments/environment.prod';


@Component({
  selector: 'app-v2',
  templateUrl: './v2.component.html',
  styleUrls: ['./v2.component.css']
})
export class V2Component {

  restrictions: PoUploadFileRestrictions = {
    allowedExtensions: ['.png', '.jpg', '.jpeg', '.bmp'],
    maxFileSize: 10000000,
    minFileSize: 0
  };
  upload: Array<any> = [];
  urlUpload: string = `${environment.api}/v2/cnn-cifar10/predict`;

  pathImage: SafeUrl = '';
  prediction: string = '';

  constructor(private v2Service: V2Service,
              private sanitizer: DomSanitizer) { }


  sucessoEnviarImagem(event: HttpResponse<Response<ResponseImage>>): void{
    let predict = event.body?.resource.predict;
    switch(predict){
      case 0:
        this.prediction = 'Avião';
        break;
      case 1:
        this.prediction = 'Carro';
        break;
      case 2:
        this.prediction = 'Pássaro';
        break;
      case 3:
        this.prediction = 'Gato';
        break;
      case 4:
        this.prediction = 'Cervo';
        break;
      case 5:
        this.prediction = 'Cachorro';
        break;
      case 6:
        this.prediction = 'Sapo';
        break;
      case 7:
        this.prediction = 'Cavalo';
        break;
      case 8:
        this.prediction = 'Navio';
        break;
      case 9:
        this.prediction = 'Caminhão';
        break;
      default:
        this.prediction = 'Erro ao prever a imagem';
        break;
    }
  }

  enviaImagemParaPredict(event: any) {
    let file: File = event.file.rawFile;
    this.pathImage = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(file));
  }
}