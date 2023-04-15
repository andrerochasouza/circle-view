package br.com.andre.ml.aplicacao;

public class NeuralNetworkTest {

    public void testNeuralNetwork() {
        NeuralNetwork nn = new NeuralNetwork(100, 10, 10);

        double[] inputs = new double[100];
        double[] target = {0,0, 0, 0, 0, 0, 0, 0, 1, 0};


        for (int i = 0; i < 100; i++) {
            inputs[i] = Math.random();
        }

        nn.train(inputs, target, 0.1, 1000);

        double[] output = nn.feedforward(inputs);
        for (int i = 0; i < output.length; i++) {
            System.out.println(output[i]);
        }
    }
}
