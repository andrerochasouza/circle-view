package br.com.andre.data.dominio;

public class Pixel {

    private double value;
    private int x;
    private int y;

    public Pixel(double value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
