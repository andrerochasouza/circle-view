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
        this.outputs = input;
        for (int i = 0; i < this.layers; i++) {
            double[] layerOutput = new double[this.weights[i].length];
            for (int j = 0; j < this.weights[i].length; j++) {
                double sum = 0;
                for (int k = 0; k < this.weights[i][j].length; k++) {
                    sum += this.weights[i][j][k] * this.outputs[k];
                }
                layerOutput[j] = sigmoid(sum + this.biases[i][j]);
            }
            this.outputs = layerOutput;
        }
        return this.outputs;
    }

    public void train(double[] input, double[] target) {
        double[][] layerOutputs = new double[this.layers][];
        layerOutputs[0] = input;

        // Feedforward
        for (int i = 1; i < this.layers; i++) {
            double[] layerOutput = new double[this.weights[i].length];
            for (int j = 0; j < this.weights[i].length; j++) {
                double sum = 0;
                for (int k = 0; k < this.weights[i][j].length; k++) {
                    sum += this.weights[i][j][k] * layerOutputs[i - 1][k];
                }
                layerOutput[j] = sigmoid(sum + this.biases[i][j]);
            }
            layerOutputs[i] = layerOutput;
        }

        // Backpropagation
        double[][] layerErrors = new double[this.layers][];
        double[] outputErrors = new double[this.outputNodes];
        for (int i = 0; i < outputErrors.length; i++) {
            outputErrors[i] = layerOutputs[this.layers - 1][i] - target[i];
        }
        layerErrors[this.layers - 1] = outputErrors;

        for (int i = this.layers - 2; i > 0; i--) {
            double[] layerError = new double[this.weights[i].length];
            double[] prevLayerOutput = layerOutputs[i - 1];
            for (int j = 0; j < this.weights[i].length; j++) {
                double error = 0;
                for (int k = 0; k < this.weights[i + 1].length; k++) {
                    error += layerErrors[i + 1][k] * this.weights[i + 1][k][j];
                }
                layerError[j] = error * sigmoidDerivative(layerOutputs[i][j]);
            }
            layerErrors[i] = layerError;
            updateWeights(i, layerError, prevLayerOutput);
        }

        updateWeights(0, layerErrors[1], input);
    }

    private void updateWeights(int layerIndex, double[] layerError, double[] prevLayerOutput) {
        for (int i = 0; i < this.weights[layerIndex].length; i++) {
            for (int j = 0; j < this.weights[layerIndex][i].length; j++) {
                this.weights[layerIndex][i][j] -= this.learningRate * layerError[i] * prevLayerOutput[j];
            }
            this.biases[layerIndex][i] -= this.learningRate * layerError[i];
        }
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
