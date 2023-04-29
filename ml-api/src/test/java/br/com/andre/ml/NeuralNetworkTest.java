package br.com.andre.ml;

import br.com.andre.ml.model.NeuralNetwork;
import org.junit.Test;

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
}
