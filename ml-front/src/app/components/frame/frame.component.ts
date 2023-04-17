import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-frame',
  templateUrl: './frame.component.html',
  styleUrls: ['./frame.component.css']
})
export class FrameComponent implements OnInit {

  @ViewChild('canvas', { static: true })
  canvas!: ElementRef<HTMLCanvasElement>;

  private ctx!: CanvasRenderingContext2D;
  private drawing = false;
  private lastX = 0;
  private lastY = 0;

  ngOnInit() {
    this.ctx = this.canvas.nativeElement.getContext('2d')!;
    this.ctx.lineWidth = 20;
    this.ctx.lineJoin = 'round';
    this.ctx.lineCap = 'round';
    this.ctx.strokeStyle = '#000000';
  }

  startDrawing(event: MouseEvent) {
    this.drawing = true;

    // Obtenha a posição absoluta do canvas na tela
    const rect = this.canvas.nativeElement.getBoundingClientRect();

    // Calcule as coordenadas relativas ao canvas usando a posição absoluta
    this.lastX = event.clientX - rect.left;
    this.lastY = event.clientY - rect.top;
  }

  draw(event: MouseEvent) {
    if (!this.drawing) {
      return;
    }

    // Obtenha a posição absoluta do canvas na tela
    const rect = this.canvas.nativeElement.getBoundingClientRect();

    // Calcule as coordenadas relativas ao canvas usando a posição absoluta
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    this.ctx.beginPath();
    this.ctx.moveTo(this.lastX, this.lastY);
    this.ctx.lineTo(x, y);
    this.ctx.stroke();

    this.lastX = x;
    this.lastY = y;
  }

  endDrawing() {
    this.drawing = false;
    this.lastX = 0;
    this.lastY = 0;
  }

  clearCanvas() {
    this.ctx.clearRect(0, 0, this.canvas.nativeElement.width, this.canvas.nativeElement.height);
  }
}