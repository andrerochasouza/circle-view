package br.com.andre.ml;

import br.com.andre.data.dominio.MNIST;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NeuralNetworkTest {

    @Test
    public void testFeedforward() {
        NeuralNetwork nn = new NeuralNetwork(2, new int[]{2, 2}, 1, 0.1);
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
        NeuralNetwork nn = new NeuralNetwork(2, new int[]{2, 2}, 1, 0.1);
        double[] inputs = {1, 0};
        double[] target = {1};
        nn.train(inputs, target);
        double[] output = nn.feedforward(inputs);
        assertEquals(1, output.length);
        assertTrue(output[0] > 0.9);

        inputs = new double[]{0, 1};
        target = new double[]{0};
        nn.train(inputs, target);
        output = nn.feedforward(inputs);
        assertEquals(1, output.length);
        assertTrue(output[0] < 0.1);
    }

    @Test
    public void testGetWeights() {
        NeuralNetwork nn = new NeuralNetwork(2, new int[]{2, 2}, 1, 0.1);
        double[][][] hiddenWeights = nn.getWeights();

        assertNotNull(hiddenWeights);
        assertEquals(2, hiddenWeights.length);
    }
}
