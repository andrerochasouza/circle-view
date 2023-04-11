package br.com.andre;

import br.com.andre.data.aplicacao.FakeData;
import br.com.andre.ml.aplicacao.Calculation;
import br.com.andre.ml.dominio.Node;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args)  {

        ArrayList<ArrayList<Node>> layers = new ArrayList<>();
        double bias = 0;

        layers = FakeData.randomNodes(5, new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1}, bias);

        Calculation.calculateErrorSum(layers, new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, false);
    }
}