package br.com.andre.ml;

import java.util.Random;
import java.util.UUID;

public class NeuralNetwork {

    private UUID uuid;
    private int outputNodes;
    private int layers;
    private double learningRate;
    private double[][][] weights;
    private double[][] biases;
    private double[] outputs;

    public NeuralNetwork() {
        this.uuid = UUID.randomUUID();
    }

    public NeuralNetwork(int inputNodes, int[] hiddenNodes, int outputNodes, double learningRate) {
        this.uuid = UUID.randomUUID();
        this.outputNodes = outputNodes;
        this.layers = hiddenNodes.length + 1;
        this.learningRate = learningRate;

        this.weights = new double[this.layers][][];
        this.biases = new double[this.layers][];
        Random random = new Random();

        // Inicializar pesos e bias com inicialização Xavier
        for (int i = 0; i < this.layers; i++) {
            int prevNodes = i == 0 ? inputNodes : hiddenNodes[i - 1];
            int currNodes = i == this.layers - 1 ? this.outputNodes : hiddenNodes[i];

            double variance = 2.0 / (prevNodes + currNodes);
            double stdDev = Math.sqrt(variance);

            this.weights[i] = new double[currNodes][prevNodes];
            this.biases[i] = new double[currNodes];
            for (int j = 0; j < currNodes; j++) {
                for (int k = 0; k < prevNodes; k++) {
                    this.weights[i][j][k] = random.nextGaussian() * stdDev;
                }
                this.biases[i][j] = 0;
            }
        }
    }

    public double[] feedforward(double[] input) {

        // Feedforward
        double[] hiddenFirstNodes = feedforwardLayer(input, this.weights[0], this.biases[0]);
        double[] hiddenSecondNodes = feedforwardLayer(hiddenFirstNodes, this.weights[1], this.biases[1]);
        double[] outputNodes = feedforwardLayer(hiddenSecondNodes, this.weights[2], this.biases[2]);

        for (int i = 0; i < outputNodes.length; i++) {
            outputNodes[i] = Math.round(outputNodes[i] * 1000.0) / 1000.0;
        }

        this.outputs = outputNodes;

        return outputs;
    }

    public void train(double[] input, double[] target) {
        double[] hiddenFirstNodes;
        double[] hiddenSecondNodes;
        double[] outputNodes;
        double[] hiddenFirstDeltas;
        double[] hiddenSecondDeltas;
        double[] outputDeltas;

        // Feedforward
        hiddenFirstNodes = feedforwardLayer(input, this.weights[0], this.biases[0]);
        hiddenSecondNodes = feedforwardLayer(hiddenFirstNodes, this.weights[1], this.biases[1]);
        outputNodes = feedforwardLayer(hiddenSecondNodes, this.weights[2], this.biases[2]);

        // Backpropagation
        outputDeltas = backpropagationOutputLayer(target, outputNodes);
        hiddenSecondDeltas = backpropagationLayer(outputDeltas, this.weights[2], hiddenSecondNodes);
        hiddenFirstDeltas = backpropagationLayer(hiddenSecondDeltas, this.weights[1], hiddenFirstNodes);

        // Atualização dos pesos e biases
        this.weights[2] = updateWeights(this.weights[2], hiddenSecondNodes, outputDeltas, this.learningRate);
        this.biases[2] = updateBiases(this.biases[2], outputDeltas, this.learningRate);
        this.weights[1] = updateWeights(this.weights[1], hiddenFirstNodes, hiddenSecondDeltas, this.learningRate);
        this.biases[1] = updateBiases(this.biases[1], hiddenSecondDeltas, this.learningRate);
        this.weights[0] = updateWeights(this.weights[0], input, hiddenFirstDeltas, this.learningRate);
        this.biases[0] = updateBiases(this.biases[0], hiddenFirstDeltas, this.learningRate);
    }

    private double[] feedforwardLayer(double[] input, double[][] weights, double[] biases) {
        double[] nodes = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            double sum = 0;
            for (int j = 0; j < weights[i].length; j++) {
                sum += weights[i][j] * input[j];
            }
            nodes[i] = sigmoid(sum + biases[i]);
        }
        return nodes;
    }

    private double[] backpropagationOutputLayer(double[] target, double[] outputNodes) {
        double[] deltas = new double[outputNodes.length];
        for (int i = 0; i < deltas.length; i++) {
            double error = target[i] - outputNodes[i];
            deltas[i] = error * sigmoidDerivative(outputNodes[i]);
        }
        return deltas;
    }

    private double[] backpropagationLayer(double[] deltas, double[][] weights, double[] nodes) {
        double[] newDeltas = new double[nodes.length];
        for (int i = 0; i < newDeltas.length; i++) {
            double error = 0;
            for (int j = 0; j < deltas.length; j++) {
                error += deltas[j] * weights[j][i];
            }
            newDeltas[i] = error * sigmoidDerivative(nodes[i]);
        }
        return newDeltas;
    }

    private double[][] updateWeights(double[][] weights, double[] nodes, double[] deltas, double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] += nodes[j] * deltas[i] * learningRate;
            }
        }
        return weights;
    }

    private double[] updateBiases(double[] biases, double[] deltas, double learningRate) {
        for (int i = 0; i < biases.length; i++) {
            biases[i] += deltas[i] * learningRate;
        }
        return biases;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getOutputNodes() {
        return outputNodes;
    }

    public void setOutputNodes(int outputNodes) {
        this.outputNodes = outputNodes;
    }

    public int getLayers() {
        return layers;
    }

    public void setLayers(int layers) {
        this.layers = layers;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    public double[][] getBiases() {
        return biases;
    }

    public void setBiases(double[][] biases) {
        this.biases = biases;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public void setOutputs(double[] outputs) {
        this.outputs = outputs;
    }
}
