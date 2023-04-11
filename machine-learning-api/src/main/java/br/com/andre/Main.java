package br.com.andre;

import br.com.andre.data.dominio.Pixel;
import br.com.andre.ml.aplicacao.Calculation;

public class Main {
    public static void main(String[] args)  {


        Pixel[] inputs = new Pixel[2];
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = new Pixel(Math.round(100 * Math.random()) / 100.0, 1, 1);
        }

        System.out.println("---- Inputs ----");
        for (Pixel input : inputs) {
            System.out.println(input.getValue());
        }

        double[] biasOcult = {0,0};
        double[] bias = {0,0,0,0,0,0,0,0,0,0};
        double[] outputs = {1,0};


        Calculation.train(inputs, outputs, biasOcult, bias, true, true);

    }
}