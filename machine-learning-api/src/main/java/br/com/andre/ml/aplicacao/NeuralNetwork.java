package br.com.andre.ml.aplicacao;

public class NeuralNetwork {

    private double[][] hiddenWeights;
    private double[][] outputWeights;
    private double[] bias1;
    private double[] bias2;

    public NeuralNetwork(int numInputs, int numHidden, int numOutputs) {
        hiddenWeights = new double[numHidden][numInputs];
        outputWeights = new double[numOutputs][numHidden];
        bias1 = new double[numHidden];
        bias2 = new double[numOutputs];

        // Inicializa os pesos com valores aleatórios entre 0 e 1
        for (int i = 0; i < numHidden; i++) {
            for (int j = 0; j < numInputs; j++) {
                hiddenWeights[i][j] = Math.random();
            }
            bias1[i] = Math.random();
        }
        for (int i = 0; i < numOutputs; i++) {
            for (int j = 0; j < numHidden; j++) {
                outputWeights[i][j] = Math.random();
            }
            bias2[i] = Math.random();
        }
    }

    public double[] feedforward(double[] inputs) {
        double[] hiddenNodes = new double[hiddenWeights.length];
        double[] outputNodes = new double[outputWeights.length];

        // Calcula a saída da camada oculta
        for (int i = 0; i < hiddenNodes.length; i++) {
            double sum = 0;
            for (int j = 0; j < inputs.length; j++) {
                sum += inputs[j] * hiddenWeights[i][j];
            }
            hiddenNodes[i] = sigmoid(sum + bias1[i]);
        }

        // Calcula a saída da camada de saída
        for (int i = 0; i < outputNodes.length; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenNodes.length; j++) {
                sum += hiddenNodes[j] * outputWeights[i][j];
            }
            outputNodes[i] = sigmoid(sum + bias2[i]);
        }

        return outputNodes;
    }

    public void train(double[] inputs, double[] target, double learningRate, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            double[] hiddenNodes = new double[hiddenWeights.length];
            double[] outputNodes = new double[outputWeights.length];
            double[] hiddenDeltas = new double[hiddenWeights.length];
            double[] outputDeltas = new double[outputWeights.length];

            // Feedforward
            for (int i = 0; i < hiddenNodes.length; i++) {
                double sum = 0;
                for (int j = 0; j < inputs.length; j++) {
                    sum += inputs[j] * hiddenWeights[i][j];
                }
                hiddenNodes[i] = sigmoid(sum + bias1[i]);
            }
            for (int i = 0; i < outputNodes.length; i++) {
                double sum = 0;
                for (int j = 0; j < hiddenNodes.length; j++) {
                    sum += hiddenNodes[j] * outputWeights[i][j];
                }
                outputNodes[i] = sigmoid(sum + bias2[i]);
            }

            // Calcula os deltas da camada de saída
            for (int i = 0; i < outputDeltas.length; i++) {
                double error = target[i] - outputNodes[i];
                outputDeltas[i] = error * sigmoidDerivative(outputNodes[i]);
            }

            // Calcula os deltas da camada oculta
            for (int i = 0; i < hiddenDeltas.length; i++) {
                double error = 0;
                for (int j = 0; j < outputDeltas.length; j++) {
                    error += outputDeltas[j] * outputWeights[j][i];
                }
                hiddenDeltas[i] = error * sigmoidDerivative(hiddenNodes[i]);
            }

            // Atualiza os pesos da camada de saída
            for (int i = 0; i < outputWeights.length; i++) {
                for (int j = 0; j < outputWeights[i].length; j++) {
                    outputWeights[i][j] += learningRate * outputDeltas[i] * hiddenNodes[j];
                }
                bias2[i] += learningRate * outputDeltas[i];
            }

            // Atualiza os pesos da camada oculta
            for (int i = 0; i < hiddenWeights.length; i++) {
                for (int j = 0; j < hiddenWeights[i].length; j++) {
                    hiddenWeights[i][j] += learningRate * hiddenDeltas[i] * inputs[j];
                }
                bias1[i] += learningRate * hiddenDeltas[i];
            }
        }
    }

    public void print() {
        System.out.println("Hidden Weights:");
        for (int i = 0; i < hiddenWeights.length; i++) {
            for (int j = 0; j < hiddenWeights[i].length; j++) {
                System.out.print(hiddenWeights[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Output Weights:");
        for (int i = 0; i < outputWeights.length; i++) {
            for (int j = 0; j < outputWeights[i].length; j++) {
                System.out.print(outputWeights[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Bias 1:");
        for (int i = 0; i < bias1.length; i++) {
            System.out.print(bias1[i] + " ");
        }
        System.out.println();
        System.out.println("Bias 2:");
        for (int i = 0; i < bias2.length; i++) {
            System.out.print(bias2[i] + " ");
        }
        System.out.println();
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }
}
