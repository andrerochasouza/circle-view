package br.com.andre.api.dominio.dtos;

import br.com.andre.data.dominio.Pixel;

import java.util.ArrayList;

public class FrameDTO {

    private ArrayList<Pixel> pixels;

    public FrameDTO(String json){
        // TODO implementar o construtor que recebe um json e transforma em um objeto FrameDTO
    }

    public FrameDTO(ArrayList<Pixel> pixels) {
        this.pixels = pixels;
    }

    public ArrayList<Pixel> getPixels() {
        return pixels;
    }

    public void setPixels(ArrayList<Pixel> pixels) {
        this.pixels = pixels;
    }
}
