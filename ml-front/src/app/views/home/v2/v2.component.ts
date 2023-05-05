import { Component, ViewChild } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { PoModalAction, PoUploadComponent, PoUploadFileRestrictions } from '@po-ui/ng-components';
import { PoUploadFile } from '@po-ui/ng-components/lib/components/po-field/po-upload/po-upload-file';
import { take } from 'rxjs';
import { V2Service } from 'src/app/components/services/v2/v2.service';


@Component({
  selector: 'app-v2',
  templateUrl: './v2.component.html',
  styleUrls: ['./v2.component.css']
})
export class V2Component {

  pathImage: SafeUrl = '';
  prediction: number = 0;

  constructor(private v2Service: V2Service,
              private sanitizer: DomSanitizer) { }

  enviaImagemParaPredict() {
    let inputFile = document.getElementById("inputFile") as HTMLInputElement;
    let file: File;

    if (!inputFile.files || !inputFile.files[0]){
      alert('Selecione uma imagem');
      return;
    }

    file = inputFile.files[0];

    if (file.type != 'image/png' && file.type != 'image/jpeg' && file.type != 'image/jpg' && file.type != 'image/bmp') {
      alert('Formato de imagem não suportado');
      return;
    }
      
    this.pathImage = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(file));
    console.log(this.pathImage);

    this.v2Service.enviaImagemCifar10Predict(file)
        .pipe(take(1))
        .subscribe({
          next: response => {
            this.prediction = response.resource.result;
          },
          error: error => {
            alert('Erro ao realizar o feedfoward. Message Error - ' + error);
          }
    });
}
}