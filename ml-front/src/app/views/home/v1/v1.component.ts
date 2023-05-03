import { Component, ViewChild } from '@angular/core';
import { take } from 'rxjs';
import { FrameComponent } from 'src/app/components/frame/frame.component';
import { FeedfowardBody } from 'src/app/components/model/feedfowardBody';
import { Pixel } from 'src/app/components/model/pixel';
import { ResponseNeuralNetwork } from 'src/app/components/model/response';
import { TrainBody } from 'src/app/components/model/trainBody';
import { ApiService } from 'src/app/components/services/v1/api.service';
import { NeuralnetworkService } from 'src/app/components/services/v1/neuralnetwork.service';

@Component({
    selector: 'app-v1',
    templateUrl: './v1.component.html',
    styleUrls: ['./v1.component.css']
})
export class V1Component {
    @ViewChild(FrameComponent) frameComponent!: FrameComponent;

    listUUIDs: string[] = [];
    uuidSelecionado: string = '';
    learningRate: number = 0.3;
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
        outputsNodes: 0,
        layers: 0,
        learningRate: 0.1,
        weights: [],
        biases: [],
        outputs: []
      }
    };
    output: number = 0;
  
  
    constructor(
      private apiService: ApiService,
      private neuralnetworkService: NeuralnetworkService,
    ) { }
  
    ngOnInit(): void {
      this.getUUIDsForSelects();
    }
  
    selecionaUUID(uuid: string) {
      this.uuidSelecionado = uuid;
      this.getNeuralNetworkByUUID(this.uuidSelecionado);
    }
  
    realizarFeedfowardByUUID(): void{
  
      if(this.uuidSelecionado == ''){
        alert('O campo UUID está vazio, por favor selecione um modelo.');
        return;
      }
  
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
  
      let outputs: number[] = [];
      
      this.apiService.enviaImagemFeedfoward(feedfowardBody, this.uuidSelecionado)
        .pipe(take(1))
        .subscribe({
          next: response => {
            outputs = response.resource.outputs;
  
            const indexMaisProximo = outputs.reduce((indiceMaisProximo, numeroAtual, indiceAtual, array) => {
              const diferencaAtual = Math.abs(1 - numeroAtual);
              const diferencaMaisProxima = Math.abs(1 - array[indiceMaisProximo]);
              return diferencaAtual < diferencaMaisProxima ? indiceAtual : indiceMaisProximo;
            }, 0);
            
            this.output = indexMaisProximo;
          },
          error: error => {
            alert('Erro ao realizar o feedfoward. Message Error - ' + error);
          }
        });
    }
  
    realizarTreino(): void{
  
      if(this.uuidSelecionado == ''){
        this.hiddenNodesSize = 30;
        alert('O campo UUID está vazio, será utilizado o valor padrão de 30 neurônios nas camadas ocultas.');
      } else {
        this.hiddenNodesSize = this.neuralNetwork.neuralNetwork.weights.length;
        if(this.hiddenNodesSize == 0){
          alert('O modelo selecionado não possui neurônios na camada oculta, por favor selecione outro modelo.');
          return;
        }
      }
  
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
            arrayTarget: this.arrayTarget[this.numeroSelecionadoTarget]
          }
        ]
      };
  
      this.apiService.enviaImagemTrain(trainBody, this.hiddenNodesSize, this.hiddenNodesSize, this.learningRate, this.uuidSelecionado)
        .pipe(take(1))
        .subscribe({
          next: response => {
            alert('Treino realizado com sucesso! UUID: ' + response.resource.uuid);
            this.getUUIDsForSelects();
          },
          error: error => {
            alert('Erro ao realizar o treino. Message Error - ' + error);
          }
        });
    }
  
    novoModeloByTrain(){
      if(confirm('Deseja realmente criar um novo modelo?')){
        this.neuralnetworkService.getNovoModeloNeural()
          .pipe(take(1))
          .subscribe({
            next: response => {
              alert('Modelo criado com sucesso! UUID: ' + response.resource.uuid);
              this.getUUIDsForSelects();
            },
            error: error => {
              alert('Erro ao criar um novo modelo. Message Error - ' + error);
            }
        });
      }
    }
  
    deletarTodosUUIDs(){
      if(confirm('Deseja realmente deletar todos os modelos?')){
        this.neuralnetworkService.deleteTodosUUIDs()
          .pipe(take(1))
          .subscribe({
            next: response => {
              alert('Modelos deletados com sucesso!');
            },
            error: error => {
              alert('Erro ao deletar os modelos. Message Error - ' + error);
            }
        });
      }
    }
  
    getUUIDsForSelects(): void{
      this.neuralnetworkService.getUUIDs()
        .pipe(take(1))
        .subscribe({
          next: response => {
            this.listUUIDs = response.resource.uuids;
          },
          error: error => {
            alert('Erro ao carregar os modelos. Message Error - ' + error);
          }
      });
    }
    
    getNeuralNetworkByUUID(uuid: string): void{
      this.neuralnetworkService.getNeuralNetworkByUUID(uuid)
        .pipe(take(1))    
        .subscribe( {
          next: response => {
            this.neuralNetwork = response.resource;
          },
          error: error => {
            alert('Erro ao carregar o modelo. Message Error - ' + error);
          }
      });
    }
  
    limparTela(): void{
      this.frameComponent.limparCanvas();
    }

}