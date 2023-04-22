import { Component, Input, OnInit } from '@angular/core';
import * as p5 from 'p5';

@Component({
  selector: 'app-frame',
  templateUrl: './frame.component.html',
  styleUrls: ['./frame.component.css']
})
export class FrameComponent implements OnInit {
  private p5Instance: p5 | null = null;
  public sketchId: string = `p5sketch-${Math.random().toString(36).substr(2, 9)}`;
  @Input() enviarPixels?: (pixels: any) => void;

  ngOnInit() {
    this.p5Instance = new p5(this.sketch);
  }

  private sketch = (p: p5) => {
    let canvas: p5.Renderer;

    p.setup = () => {
      canvas = p.createCanvas(280, 280);
      canvas.parent(this.sketchId);
      p.background(255);
    };

    p.draw = () => {
      if (p.mouseIsPressed) {
        p.strokeWeight(24);
        p.line(p.mouseX, p.mouseY, p.pmouseX, p.pmouseY);
      }
    };

    this.enviarPixels = (pixels: any) => {};

    p.keyReleased = () => {
      if (p.keyCode === p.ENTER || p.keyCode === p.RETURN) {
        this.getDesenho();
        p.background(255);
      }
    };
  }

  limparCanvas() {
    if (this.p5Instance) {
      this.p5Instance.background(255);
    }
  }

  getDesenho(): number[] {
    if (this.enviarPixels && this.p5Instance) {
      // Obter a imagem original
      const originalImage = this.p5Instance.get(0, 0, this.p5Instance.width, this.p5Instance.height);
  
      // Redimensionar a imagem para 28x28 pixels
      const resizedImage = this.p5Instance.createImage(28, 28);
      resizedImage.copy(originalImage, 0, 0, originalImage.width, originalImage.height, 0, 0, 28, 28);
  
      // Obter os pixels da imagem redimensionada
      resizedImage.loadPixels();
      const pixelArray: number[] = [];
  
      // Transforma os valores RGB em escala de cinza
      for (let i = 0; i < resizedImage.pixels.length; i += 4) {
        const r = resizedImage.pixels[i];
        const g = resizedImage.pixels[i + 1];
        const b = resizedImage.pixels[i + 2];
        const grayValue = (r + g + b) / 3;
  
        // Normaliza o valor entre 0 e 1 e adiciona ao array
        pixelArray.push(1 - grayValue / 255);
      }

    // Desenhar a matriz de pixels no console
    for (let row = 0; row < 28; row++) {
      let rowString = "";
      for (let col = 0; col < 28; col++) {
        const pixelValue = pixelArray[row * 28 + col];
        rowString += pixelValue > 0 ? "@" : ".";
      }
      console.log(rowString);
    }
  
      return pixelArray;
    }
  
    return [];
  }
}