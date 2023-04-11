package br.com.andre.ml.aplicacao;

import br.com.andre.ml.dominio.Node;

import java.util.ArrayList;

public class Functions {

    public static double sigmoidNode(double input) {
        if(input < -10){
            return 0;
        } else if(input > 10){
            return 1;
        } else {
            return Math.round((1 / (1 + Math.exp(-input))) * 100.0) / 100.0;
        }
    }

    public static double sumNodeBySigmoid(Node node) {
        double sum = 0;
        for (int i = 0; i < node.getInputs().length; i++) {
            sum += Math.round((node.getInputs()[i] * node.getWeights()[i]) * 100.0) / 100.0;
        }

        return sigmoidNode(sum + node.getBias());
    }

    public static double[] sumNodesBySigmoid(ArrayList<Node> nodes) {
        double[] inputs = new double[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            inputs[i] = sumNodeBySigmoid(nodes.get(i));
        }
        return inputs;
    }

    public static double customError(double[] outputs, double[] outputsPerfection) {

        if(outputs.length != outputsPerfection.length){
            throw new IllegalArgumentException("Outputs e outputsPerfection devem ter o mesmo tamanho");
        }

        double sum = 0;
        for (int i = 0; i < outputs.length; i++) {
            sum += Math.pow(outputs[i] - outputsPerfection[i], 2);
        }
        return Math.round(sum * 100.0) / 100.0;
    }

}
