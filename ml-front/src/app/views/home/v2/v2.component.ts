import { Component, ViewChild } from '@angular/core';
import { PoModalAction, PoUploadComponent, PoUploadFileRestrictions } from '@po-ui/ng-components';
import { PoUploadFile } from '@po-ui/ng-components/lib/components/po-field/po-upload/po-upload-file';


@Component({
  selector: 'app-v2',
  templateUrl: './v2.component.html',
  styleUrls: ['./v2.component.css']
})
export class V2Component {


  imageUrl: string = '';
  fileRestrictions: PoUploadFileRestrictions = {
    allowedExtensions: ['.jpg', '.jpeg', '.png', '.bmp']
  };

  constructor() { }

  resumeUploadSuccess(event: any) {
    console.log(event);
    this.imageUrl = event.url;
  }

  resumeUploadError(event: any) {
    console.log(event);
  }

}