package br.com.andre.ml;

import br.com.andre.data.dominio.MNIST;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NeuralNetworkTest {

    @Test
    public void testFeedforward() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 1);
        double[] inputs = {1, 0};
        double[] output = nn.feedforward(inputs);
        assertNotNull(output);
        assertEquals(1, output.length);

        inputs = new double[]{0, 1};
        output = nn.feedforward(inputs);
        assertNotNull(output);
        assertEquals(1, output.length);
    }

    @Test
    public void testTrain() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 1);
        double[] inputs = {1, 0};
        double[] target = {1};
        nn.train(inputs, target, 0.1, 1000);
        double[] output = nn.feedforward(inputs);
        assertEquals(1, output.length);
        assertTrue(output[0] > 0.9);

        inputs = new double[]{0, 1};
        target = new double[]{0};
        nn.train(inputs, target, 0.1, 1000);
        output = nn.feedforward(inputs);
        assertEquals(1, output.length);
        assertTrue(output[0] < 0.1);
    }

    @Test
    public void testGetHiddenWeights() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 1);
        double[][] hiddenWeights = nn.getHiddenWeights();
        assertNotNull(hiddenWeights);
        assertEquals(2, hiddenWeights.length);
        assertEquals(2, hiddenWeights[0].length);
    }

    @Test
    public void testGetOutputWeights() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 1);
        double[][] outputWeights = nn.getOutputWeights();
        assertNotNull(outputWeights);
        assertEquals(1, outputWeights.length);
        assertEquals(2, outputWeights[0].length);
    }

    @Test
    public void testTrainByDataTrainingMNIST() throws IOException {
        // Carrega os dados do MNIST
        MNIST mnist = new MNIST();
        mnist.loadTrainingData();

        // Cria uma rede neural com 784 entradas, 30 neurônios na camada oculta e 10 saídas
        NeuralNetwork nn = new NeuralNetwork(784, 30, 10);

        // Define os hiperparâmetros do treinamento
        double learningRate = 0.5;
        int epochs = 10;

        // Treina a rede neural com o conjunto de treinamento do MNIST
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < mnist.numImages(); i++) {
                double[] inputs = mnist.getImage(i);
                double[] targets = mnist.getOneHotLabel(i);
                nn.train(inputs, targets, learningRate, 2);
            }
        }

        // Calcula a saída da rede neural para uma imagem do MNIST
        double[] inputs = mnist.getImage(0);
        double[] output = nn.feedforward(inputs);

        // Verifica se a saída da rede neural é próxima o suficiente da saída esperada
        double[] expectedOutput = mnist.getOneHotLabel(0);
        assertArrayEquals(expectedOutput, output, 0.1);
    }
}
