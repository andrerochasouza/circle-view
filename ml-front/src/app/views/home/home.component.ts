import { Component, ViewChild } from '@angular/core';
import { FrameComponent } from 'src/app/components/frame/frame.component';
import { HomeService } from './home.service';
import { Router } from '@angular/router';
import { ResponseFeedFoward } from 'src/app/components/model/response';
import { ResponseTrain } from 'src/app/components/model/response';
import { ResponseList } from 'src/app/components/model/response';
import { Observable, catchError, map, of } from 'rxjs';
import { FrameRequest, FramesRequest } from 'src/app/components/model/frameRequest';
import { PoListBoxLiterals } from '@po-ui/ng-components/lib/components/po-listbox/interfaces/po-listbox-literals.interface';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  @ViewChild(FrameComponent) frameComponent!: FrameComponent;

  uuidInit: string = '';
  epochs: number = 100000;
  learningRate: number = 0.1;
  hiddenNodesSize: number = 10;
  arrayTarget: number[] = [1, 0, 0, 0, 0, 0, 0, 0, 0, 0];

  onItemChange(event: any, index: number) {
    this.arrayTarget[index] = parseFloat(event.target.value);
  }

  constructor(
    private homeService: HomeService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.initUUID();
  }

  realizarFeedfowardByUUID(): void{

    const arrayImagem: number[] = this.frameComponent.getDesenho();
    this.limparTela();

    const pixels: any[] = [];
    arrayImagem.forEach((value: number) => {
      pixels.push({value: value});
    });

    const frame: FrameRequest = {
      frame: {
        id: 1,
        pixels: pixels
      }
    };
    
    this.homeService.enviaImagemFeedfoward(frame, this.uuidInit).subscribe((response: ResponseFeedFoward) => {
      console.log(response);
    }, (error: any) => {
      console.log(error);
    });
    
  }

  realizarTreino(): void{

    const arrayImagem: number[] = this.frameComponent.getDesenho();
    this.limparTela();

    const pixels: any[] = [];
    arrayImagem.forEach((value: number) => {
      pixels.push({value: value});
    });

    const frame: FramesRequest = {
      frames: [
        {
          id: 1,
          pixels: pixels
        }
      ],
      targets: [
        {
          arrayTarget: this.arrayTarget
        }
      ]
    };

    this.homeService.enviaImagemTrain(frame, this.hiddenNodesSize, this.epochs, this.learningRate, this.uuidInit).subscribe((response: ResponseTrain) => {
      console.log(response);
    }, (error: any) => {
      console.log(error);
    });

  }

  limparTela(): void{
    this.frameComponent.limparCanvas();
  }

  initUUID(): void{
    this.homeService.getUUIDs().subscribe((response: ResponseList) => {
      this.uuidInit = response.resource.uuids[0];
    }, (error: any) => {
      console.log(error);
    });
  }
}