package br.com.andre.ml.aplicacao;

import br.com.andre.ml.dominio.Node;

import java.util.ArrayList;

import static br.com.andre.ml.aplicacao.Functions.customError;
import static br.com.andre.ml.aplicacao.Functions.sumNodesBySigmoid;

public class Calculation {

    public static double calculateErrorSum(ArrayList<ArrayList<Node>> layers, double[] expectedOutput, boolean print) {

        double errorSum = 0;
        for(ArrayList<Node> nodes : layers){
            errorSum += calculateError(nodes, expectedOutput, print);
        }

        System.out.println("Error: " + errorSum);
        return errorSum;
    }

    private static double calculateError(ArrayList<Node> nodes, double[] expectedOutput, boolean print) {

        double[] outputs = sumNodesBySigmoid(nodes);

        if(print){
            for (int i = 0; i < outputs.length; i++) {
                System.out.println("Output: " + outputs[i]);
            }
        }

        double error = customError(outputs, new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0});

        return error;
    }

}
