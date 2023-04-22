package br.com.andre.ml;

import br.com.andre.data.dominio.MNIST;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NeuralNetworkTest {

    @Test
    public void testFeedforward() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 2, 1);
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
        NeuralNetwork nn = new NeuralNetwork(2, 2, 2, 1);
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
        NeuralNetwork nn = new NeuralNetwork(2, 2, 2, 1);
        double[][] hiddenWeights1 = nn.getHiddenWeights1();
        double[][] hiddenWeights2 = nn.getHiddenWeights2();

        assertNotNull(hiddenWeights1);
        assertNotNull(hiddenWeights2);
        assertEquals(2, hiddenWeights1.length);
        assertEquals(2, hiddenWeights1[0].length);
        assertEquals(2, hiddenWeights2.length);
        assertEquals(2, hiddenWeights2[0].length);
    }

    @Test
    public void testGetOutputWeights() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 2, 1);
        double[][] outputWeights = nn.getOutputWeights();
        assertNotNull(outputWeights);
        assertEquals(1, outputWeights.length);
        assertEquals(2, outputWeights[0].length);
    }
}
