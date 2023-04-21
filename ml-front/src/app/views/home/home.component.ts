import { Component, ViewChild } from '@angular/core';
import { FrameComponent } from 'src/app/components/frame/frame.component';
import { ApiService } from 'src/app/components/services/api.service';
import { NeuralnetworkService } from 'src/app/components/services/neuralnetwork.service';
import { Router } from '@angular/router';

import { Pixel } from 'src/app/components/model/pixel';
import { FeedfowardBody } from 'src/app/components/model/feedfowardBody';
import { Response, ResponseFeedfoward, ResponseNeuralNetwork, ResponseTrain, ResponseUUIDs } from 'src/app/components/model/response';
import { TrainBody } from 'src/app/components/model/trainBody';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  @ViewChild(FrameComponent) frameComponent!: FrameComponent;

  listUUIDs: string[] = [];
  uuidSelecionado: string = '';
  epochs: number = 1;
  learningRate: number = 0.1;
  hiddenNodesSize: number = 0;
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
  numeroSelecionadoTarget: number = 0;
  neuralNetwork: ResponseNeuralNetwork = {
    neuralNetwork: {
      uuid: '',
      hiddenWeights: [[]],
      outputWeights: [[]],
      bias1: [],
      bias2: [],
      outputs: []
    }
  };
  output: number = 0;


  constructor(
    private apiService: ApiService,
    private neuralnetworkService: NeuralnetworkService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.neuralnetworkService.getUUIDs().subscribe((response: Response<ResponseUUIDs>) => {
      this.listUUIDs = response.resource.uuids;
      }, (error: any) => {
        console.log(error);
      });
  }

  selecionaUUID(uuid: string) {
    this.uuidSelecionado = uuid;
    this.getNeuralNetworkByUUID(this.uuidSelecionado);
  }

  realizarFeedfowardByUUID(): void{

    const arrayImagem: number[] = this.frameComponent.getDesenho();
    this.limparTela();

    if(this.uuidSelecionado == ''){
      alert('O campo UUID está vazio, por favor selecione um modelo.');
      return;
    }

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

    let outputs: number[] = [];
    
    this.apiService.enviaImagemFeedfoward(feedfowardBody, this.uuidSelecionado).subscribe((response: Response<ResponseFeedfoward>) => {
      console.log(response);
      outputs = response.resource.outputs;

      const indexMaisProximo = outputs.reduce((indiceMaisProximo, numeroAtual, indiceAtual, array) => {
        const diferencaAtual = Math.abs(1 - numeroAtual);
        const diferencaMaisProxima = Math.abs(1 - array[indiceMaisProximo]);
        return diferencaAtual < diferencaMaisProxima ? indiceAtual : indiceMaisProximo;
      }, 0);
      
      this.output = indexMaisProximo;
    }, (error: any) => {
      console.log(error);
    });
  }

  realizarTreino(): void{

    const arrayImagem: number[] = this.frameComponent.getDesenho();
    this.limparTela();

    if(this.uuidSelecionado == ''){
      this.hiddenNodesSize = 10;
      alert('O campo UUID está vazio, será utilizado o valor padrão de 10 neurônios na camada oculta.');
    } else {
      this.hiddenNodesSize = this.neuralNetwork.neuralNetwork.hiddenWeights.length;
      if(this.hiddenNodesSize == 0){
        alert('O modelo selecionado não possui neurônios na camada oculta, por favor selecione outro modelo.');
        return;
      }
    }
  
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
          arrayTarget: this.arrayTarget[this.numeroSelecionadoTarget]
        }
      ]
    };

    this.apiService.enviaImagemTrain(trainBody, this.hiddenNodesSize, this.epochs, this.learningRate, this.uuidSelecionado).subscribe((response: Response<ResponseTrain>) => {
      console.log(response);
    }, (error: any) => {
      console.log(error);
    });

  }
  
  getNeuralNetworkByUUID(uuid: string): void{
    this.neuralnetworkService.getNeuralNetworkByUUID(uuid).subscribe((response: Response<ResponseNeuralNetwork>) => {
      this.neuralNetwork = response.resource;
      }, (error: any) => {
        console.log(error);
      });
  }

  limparTela(): void{
    this.frameComponent.limparCanvas();
  }
}