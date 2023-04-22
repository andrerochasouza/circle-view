package br.com.andre.ml;

import java.util.Arrays;
import java.util.UUID;

public class NeuralNetwork {

    private UUID uuid;
    private double[][] hiddenWeights1;
    private double[][] hiddenWeights2;
    private double[][] outputWeights;
    private double[] bias1;
    private double[] bias2;
    private double[] bias3;
    private double[] hiddenOutputs1;
    private double[] hiddenOutputs2;
    private double[] outputs;

    public NeuralNetwork() {
        this.uuid = UUID.randomUUID();
    }

    public NeuralNetwork(int numInputs, int numHidden1, int numHidden2, int numOutputs) {
        this.uuid = UUID.randomUUID();
        hiddenWeights1 = new double[numHidden1][numInputs];
        hiddenWeights2 = new double[numHidden2][numHidden1];
        outputWeights = new double[numOutputs][numHidden2];
        bias1 = new double[numHidden1];
        bias2 = new double[numHidden2];
        bias3 = new double[numOutputs];
        hiddenOutputs1 = new double[numHidden1];
        hiddenOutputs2 = new double[numHidden2];

        // Inicialização de pesos utilizando a técnica de LeCun
        double scale = Math.sqrt(1.0 / numInputs);
        for (int i = 0; i < numHidden1; i++) {
            for (int j = 0; j < numInputs; j++) {
                hiddenWeights1[i][j] = Math.random() * scale;
            }
            bias1[i] = Math.random() * scale;
        }
        scale = Math.sqrt(1.0 / numHidden1);
        for (int i = 0; i < numHidden2; i++) {
            for (int j = 0; j < numHidden1; j++) {
                hiddenWeights2[i][j] = Math.random() * scale;
            }
            bias2[i] = Math.random() * scale;
        }
        scale = Math.sqrt(1.0 / numHidden2);
        for (int i = 0; i < numOutputs; i++) {
            for (int j = 0; j < numHidden2; j++) {
                outputWeights[i][j] = Math.random() * scale;
            }
            bias3[i] = Math.random() * scale;
        }
    }


    public double[] feedforward(double[] inputs) {
        double[] hiddenNodes1;
        double[] hiddenNodes2;
        double[] outputNodes;

        // Calcula a saída da primeira camada oculta
        hiddenNodes1 = calculateNodes(inputs, hiddenWeights1, bias1);

        // Calcula a saída da segunda camada oculta
        hiddenNodes2 = calculateNodes(hiddenNodes1, hiddenWeights2, bias2);

        // Calcula a saída da camada de saída
        outputNodes = calculateNodes(hiddenNodes2, outputWeights, bias3);

        return outputNodes;
    }


    public void train(double[] inputs, double[] target, double learningRate, int epochs) {
        double[][][] weights = {hiddenWeights1, hiddenWeights2, outputWeights};
        double[][] nodes = new double[3][];
        double[][] deltas = new double[3][];
        double[][] biases = {bias1, bias2, bias3};
        double momentum = 0.9;

        double[][][] prevWeightDeltas = new double[3][][];
        double[][] prevBiasDeltas = new double[3][];

        for (int layer = 0; layer < 3; layer++) {
            prevWeightDeltas[layer] = new double[weights[layer].length][weights[layer][0].length];
            prevBiasDeltas[layer] = new double[biases[layer].length];
        }

        double[][][] weightDeltas = new double[3][][];
        double[][] biasDeltas = new double[3][];
        for (int layer = 0; layer < 3; layer++) {
            weightDeltas[layer] = new double[weights[layer].length][weights[layer][0].length];
            biasDeltas[layer] = new double[biases[layer].length];
        }

        for (int epoch = 0; epoch < epochs; epoch++) {

            // Feedforward
            nodes[0] = calculateNodes(inputs, weights[0], biases[0]);
            nodes[1] = calculateNodes(nodes[0], weights[1], biases[1]);
            nodes[2] = calculateNodes(nodes[1], weights[2], biases[2]);

            // Backpropagation
            deltas[2] = calculateOutputDeltas(nodes[2], target);
            deltas[1] = calculateHiddenDeltas(nodes[1], deltas[2], weights[2]);
            deltas[0] = calculateHiddenDeltas(nodes[0], deltas[1], weights[1]);

            // Update weights and biases
            for (int layer = 0; layer < 3; layer++) {
                for (int i = 0; i < weights[layer].length; i++) {
                    for (int j = 0; j < weights[layer][i].length; j++) {
                        double delta = learningRate * deltas[layer][i] * (layer == 0 ? inputs[j] : nodes[layer - 1][j]);
                        weightDeltas[layer][i][j] = delta + momentum * prevWeightDeltas[layer][i][j];
                        weights[layer][i][j] += weightDeltas[layer][i][j];
                        prevWeightDeltas[layer][i][j] = weightDeltas[layer][i][j];
                    }
                    double delta = learningRate * deltas[layer][i];
                    biasDeltas[layer][i] = delta + momentum * prevBiasDeltas[layer][i];
                    biases[layer][i] += biasDeltas[layer][i];
                    prevBiasDeltas[layer][i] = biasDeltas[layer][i];
                }
            }
        }
    }

    private double[] calculateNodes(double[] inputs, double[][] weights, double[] biases) {
        double[] nodes = new double[weights.length];
        for (int i = 0; i < nodes.length; i++) {
            double sum = 0;
            for (int j = 0; j < inputs.length; j++) {
                sum += inputs[j] * weights[i][j];
            }
            nodes[i] = sigmoid(sum + biases[i]);
        }
        return nodes;
    }

    private double[] calculateOutputDeltas(double[] outputs, double[] target) {
        for (int i = 0; i < outputs.length; i++) {
            if(outputs[i] < 0.0 || outputs[i] > 1.0){
                throw new RuntimeException("Output is out of range: " + outputs[i]);
            }
        }
        double[] deltas = new double[outputs.length];
        for (int i = 0; i < deltas.length; i++) {
            deltas[i] = (target[i] - outputs[i]) * sigmoidDerivative(outputs[i]);
        }
        return deltas;
    }

    private double[] calculateHiddenDeltas(double[] nodes, double[] deltas, double[][] weights) {
        double[] hiddenDeltas = new double[nodes.length];
        for (int i = 0; i < hiddenDeltas.length; i++) {
            double sum = 0;
            for (int j = 0; j < deltas.length; j++) {
                sum += deltas[j] * weights[j][i];
            }
            hiddenDeltas[i] = sum * sigmoidDerivative(nodes[i]);
        }
        return hiddenDeltas;
    }

    public double sigmoidDerivative(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }

    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public double[][] getHiddenWeights1() {
        return hiddenWeights1;
    }

    public void setHiddenWeights1(double[][] hiddenWeights1) {
        this.hiddenWeights1 = hiddenWeights1;
    }

    public double[][] getHiddenWeights2() {
        return hiddenWeights2;
    }

    public void setHiddenWeights2(double[][] hiddenWeights2) {
        this.hiddenWeights2 = hiddenWeights2;
    }

    public double[][] getOutputWeights() {
        return outputWeights;
    }

    public void setOutputWeights(double[][] outputWeights) {
        this.outputWeights = outputWeights;
    }

    public double[] getBias1() {
        return bias1;
    }

    public void setBias1(double[] bias1) {
        this.bias1 = bias1;
    }

    public double[] getBias2() {
        return bias2;
    }

    public void setBias2(double[] bias2) {
        this.bias2 = bias2;
    }

    public double[] getBias3() {
        return bias3;
    }

    public void setBias3(double[] bias3) {
        this.bias3 = bias3;
    }

    public double[] getHiddenOutputs1() {
        return hiddenOutputs1;
    }

    public void setHiddenOutputs1(double[] hiddenOutputs1) {
        this.hiddenOutputs1 = hiddenOutputs1;
    }

    public double[] getHiddenOutputs2() {
        return hiddenOutputs2;
    }

    public void setHiddenOutputs2(double[] hiddenOutputs2) {
        this.hiddenOutputs2 = hiddenOutputs2;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public void setOutputs(double[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "NeuralNetwork{" +
                "uuid=" + uuid +
                ", hiddenWeights1=" + Arrays.toString(hiddenWeights1) +
                ", hiddenWeights2=" + Arrays.toString(hiddenWeights2) +
                ", outputWeights=" + Arrays.toString(outputWeights) +
                ", bias1=" + Arrays.toString(bias1) +
                ", bias2=" + Arrays.toString(bias2) +
                ", bias3=" + Arrays.toString(bias3) +
                ", hiddenOutputs1=" + Arrays.toString(hiddenOutputs1) +
                ", hiddenOutputs2=" + Arrays.toString(hiddenOutputs2) +
                ", outputs=" + Arrays.toString(outputs) +
                '}';
    }
}
