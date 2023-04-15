package br.com.andre.api.dominio.dtos;

import br.com.andre.data.dominio.Pixel;
import br.com.andre.util.GsonUtil;

import java.util.ArrayList;

public class FrameDTO {

    private int id;
    private ArrayList<PixelDTO> pixels;

    public FrameDTO(int id, ArrayList<PixelDTO> pixels) {
        this.id = id;
        this.pixels = pixels;
    }

    public static FrameDTO fromJson(String json) {
        return GsonUtil.fromJson(json, FrameDTO.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<PixelDTO> getPixels() {
        return pixels;
    }

    public void setPixels(ArrayList<PixelDTO> pixels) {
        this.pixels = pixels;
    }
}
