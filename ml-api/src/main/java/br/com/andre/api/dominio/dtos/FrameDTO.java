package br.com.andre.api.dominio.dtos;

import br.com.andre.data.dominio.Pixel;

import java.util.ArrayList;

public class FrameDTO {

    private int id;
    private ArrayList<Pixel> pixels;

    public FrameDTO(int id, ArrayList<Pixel> pixels) {
        this.id = id;
        this.pixels = pixels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Pixel> getPixels() {
        return pixels;
    }

    public void setPixels(ArrayList<Pixel> pixels) {
        this.pixels = pixels;
    }
}
