import { Component, ViewChild } from '@angular/core';
import { FrameComponent } from 'src/app/components/frame/frame.component';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  @ViewChild(FrameComponent) frameComponent!: FrameComponent;


  constructor() { }

  ngOnInit(): void {
  }

  realizarFeedfoward(): void{

  }

  routeTrainPage(): void{

  }

  limparTela(): void{
    this.frameComponent.clearCanvas();
  }
}
