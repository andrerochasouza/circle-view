import { Component, ViewChild } from '@angular/core';
import { FrameComponent } from 'src/app/components/frame/frame.component';
import { HomeService } from '../../components/services/api.service';
import { Router } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import { PoListBoxLiterals } from '@po-ui/ng-components/lib/components/po-listbox/interfaces/po-listbox-literals.interface';

import { Pixel } from 'src/app/components/model/pixel';
import { FeedfowardBody } from 'src/app/components/model/feedfowardBody';
import { Response, ResponseFeedfoward, ResponseTrain, ResponseUUIDs } from 'src/app/components/model/response';
import { TrainBody } from 'src/app/components/model/trainBody';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  @ViewChild(FrameComponent) frameComponent!: FrameComponent;

  uuidInit: string = '';
  epochs: number = 1;
  learningRate: number = 0.1;
  hiddenNodesSize: number = 10;
  arrayTarget: number[][] = [[1, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                            [0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
                            [0, 0, 1, 0, 0, 0, 0, 0, 0, 0],
                            [0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
                            [0, 0, 0, 0, 1, 0, 0, 0, 0, 0],
                            [0, 0, 0, 0, 0, 1, 0, 0, 0, 0],
                            [0, 0, 0, 0, 0, 0, 1, 0, 0, 0],
                            [0, 0, 0, 0, 0, 0, 0, 1, 0, 0],
                            [0, 0, 0, 0, 0, 0, 0, 0, 1, 0],
                            [0, 0, 0, 0, 0, 0, 0, 0, 0, 1]];
  numeroSelecionado: number = 0;

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

    const pixels: Pixel[] = [];
    arrayImagem.forEach((value: number) => {
      pixels.push({value: value});
    });

    const feedfowardBody: FeedfowardBody = {
      frame: {
        id: 1,
        pixels: pixels
      }
    };
    
    this.homeService.enviaImagemFeedfoward(feedfowardBody, this.uuidInit).subscribe((response: Response<ResponseFeedfoward>) => {
      console.log(response);
    }, (error: any) => {
      console.log(error);
    });
    
  }

  realizarTreino(): void{

    const arrayImagem: number[] = this.frameComponent.getDesenho();
    this.limparTela();

    const pixels: Pixel[] = [];
    arrayImagem.forEach((value: number) => {
      pixels.push({value: value});
    });

    const trainBody: TrainBody = {
      frames: [
        {
          id: 1,
          pixels: pixels
        }
      ],
      targets: [
        {
          arrayTarget: this.arrayTarget[this.numeroSelecionado]
        }
      ]
    };

    this.homeService.enviaImagemTrain(trainBody, this.hiddenNodesSize, this.epochs, this.learningRate, this.uuidInit).subscribe((response: Response<ResponseTrain>) => {
      console.log(response);
    }, (error: any) => {
      console.log(error);
    });

  }

  limparTela(): void{
    this.frameComponent.limparCanvas();
  }

  initUUID(): void{
    this.homeService.getUUIDs().subscribe((response: Response<ResponseUUIDs>) => {
      this.uuidInit = response.resource.uuids[0];
      console.log(this.uuidInit);
    }, (error: any) => {
      console.log(error);
    });
  }
}