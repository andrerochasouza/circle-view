package br.com.andre.data.aplicacao;

import br.com.andre.ml.dominio.Node;

import java.util.ArrayList;

public class FakeData {

    public static ArrayList<ArrayList<Node>> randomNodes(int qtdLayers, double[] weights, double bias) {
        ArrayList<ArrayList<Node>> layers = new ArrayList<>();

        for (int i = 0; i < qtdLayers; i++){
            ArrayList<Node> nodes = new ArrayList<>();
            for (int j = 0; j < 10; j++){
                double[] inputs = new double[weights.length];
                for (int k = 0; k < weights.length; k++){
                    inputs[k] = Math.random();
                }
                nodes.add(new Node(inputs, weights, bias));
            }
            layers.add(nodes);
        }

        return layers;
    }
}
