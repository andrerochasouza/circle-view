package br.com.andre.api.aplicacao;

import br.com.andre.api.dominio.dtos.FrameDTO;
import br.com.andre.data.dominio.Pixel;
import br.com.andre.ml.aplicacao.NeuralNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class MLController {

    public static JsonObject trainAndReturnJsonNetworkWeights(ArrayList<FrameDTO> frames, ArrayList<double[]> targets,  int hiddenNodesSize, int epochs, double learningRate) {

        NeuralNetwork neuralNetwork = new NeuralNetwork(frames.get(0).getPixels().size(), hiddenNodesSize, 10);
        for (int i = 0; i < frames.size(); i++) {
            double[] inputs = frames.get(i).getPixels().stream().mapToDouble(Pixel::getValue).toArray();
            double[] target = targets.get(i);

            neuralNetwork.train(inputs, target, learningRate, epochs);
        }

        double[][] hiddenWeights = neuralNetwork.getHiddenWeights();
        double[][] outputWeights = neuralNetwork.getOutputWeights();
        double[] bias1 = neuralNetwork.getBias1();
        double[] bias2 = neuralNetwork.getBias2();

        JsonObject networkWeights = new JsonObject();
        networkWeights.add("hiddenWeights", new Gson().toJsonTree(hiddenWeights));
        networkWeights.add("outputWeights", new Gson().toJsonTree(outputWeights));
        networkWeights.add("bias1", new Gson().toJsonTree(bias1));
        networkWeights.add("bias2", new Gson().toJsonTree(bias2));

        return networkWeights;
    }

}
