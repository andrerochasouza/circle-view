package br.com.andre.api.aplicacao.v1;

import br.com.andre.api.dominio.dtos.FrameDTO;
import br.com.andre.data.aplicacao.NeuralNetworkData;
import br.com.andre.data.dominio.Pixel;
import br.com.andre.ml.aplicacao.InternTrain;
import br.com.andre.ml.model.NeuralNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MLControllerV1 {

    private static NeuralNetworkData neuralNetworkData = new NeuralNetworkData();

    public static JsonObject newModelNeuralNetwork(){

        NeuralNetwork neuralNetwork = new NeuralNetwork(784, new int[]{30, 30}, 10, 0.1);

        neuralNetworkData.saveNeuralNetwork(neuralNetwork);

        JsonObject uuidJson = new JsonObject();
        uuidJson.add("uuid", new Gson().toJsonTree(neuralNetwork.getUuid()));

        return uuidJson;
    }

    public static JsonObject internTrainByMNISTAndReturnUUID() {

        Instant start = Instant.now();
        InternTrain internTrain = new InternTrain();
        UUID uuid = internTrain.trainInternByMNISTAndSaveNN();

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long min = duration.toMinutes();
        long seg = duration.minusMinutes(min).getSeconds();

        JsonObject uuidJson = new JsonObject();
        uuidJson.add("uuid", new Gson().toJsonTree(uuid));
        uuidJson.add("tempo", new Gson().toJsonTree(String.format("Tempo de treinamento: %d minutos e %d segundos", min, seg)));

        return uuidJson;
    }

    public static JsonObject internRetrainByMNISTAndReturnUUID(UUID uuid) {

        Instant start = Instant.now();
        InternTrain internTrain = new InternTrain();
        internTrain.retrainInternByMNISTAndSaveNN(uuid);

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long min = duration.toMinutes();
        long seg = duration.minusMinutes(min).getSeconds();

        JsonObject uuidJson = new JsonObject();
        uuidJson.add("uuid", new Gson().toJsonTree(uuid));
        uuidJson.add("tempo", new Gson().toJsonTree(String.format("Tempo de treinamento: %d minutos e %d segundos", min, seg)));

        return uuidJson;
    }

    public static JsonObject trainAndReturnJsonNetworkWeights(ArrayList<FrameDTO> frames, ArrayList<double[]> targets,  int hiddenNodesSize1, int hiddenNodesSize2, double learningRate, UUID uuid) {

        int[] hiddenNodes = {hiddenNodesSize1, hiddenNodesSize2};

        NeuralNetwork neuralNetwork = new NeuralNetwork(frames.get(0).getPixels().size(), hiddenNodes, 10, learningRate);

        if(Objects.nonNull(uuid)){
            neuralNetwork = neuralNetworkData.getNeuralNetwork(uuid);
        }

        for (int i = 0; i < frames.size(); i++) {
            double[] inputs = frames.get(i).getPixels().stream().mapToDouble(Pixel::getValue).toArray();
            double[] target = targets.get(i);

            neuralNetwork.train(inputs, target);
        }

        if(Objects.isNull(uuid)){
            neuralNetworkData.saveNeuralNetwork(neuralNetwork);
        } else {
            neuralNetworkData.updateNeuralNetwork(neuralNetwork);
        }

        UUID uuidJson = neuralNetwork.getUuid();
        double[] outputs = neuralNetwork.getOutputs();
        double[][] biases = neuralNetwork.getBiases();
        double[][][] weights = neuralNetwork.getWeights();


        JsonObject networkWeights = new JsonObject();
        networkWeights.add("uuid", new Gson().toJsonTree(uuidJson));
        networkWeights.add("outputs", new Gson().toJsonTree(outputs));
        networkWeights.add("biases", new Gson().toJsonTree(biases));
        networkWeights.add("weights", new Gson().toJsonTree(weights));

        return networkWeights;
    }

    public static JsonObject feedfowardAndReturnJsonOutputs(FrameDTO frame, UUID uuid) {

        NeuralNetwork neuralNetwork = neuralNetworkData.getNeuralNetwork(uuid);

        double[] inputs = frame.getPixels().stream().mapToDouble(Pixel::getValue).toArray();
        double[] outputs = neuralNetwork.feedforward(inputs);

        JsonObject outputsJson = new JsonObject();
        outputsJson.add("uuid", new Gson().toJsonTree(uuid));
        outputsJson.add("outputs", new Gson().toJsonTree(outputs));

        return outputsJson;
    }

    public static JsonObject getAllUUIDs() {
        ArrayList<UUID> uuids = neuralNetworkData.getAllUUIDs();
        JsonObject uuidsJson = new JsonObject();
        uuidsJson.add("uuids", new Gson().toJsonTree(uuids));
        return uuidsJson;
    }

    public static JsonObject getNeuralNetworkByUUID(UUID uuid) {
        NeuralNetwork neuralNetwork = neuralNetworkData.getNeuralNetwork(uuid);
        JsonObject neuralNetworkJson = new JsonObject();

        if(Objects.isNull(neuralNetwork)){
            return null;
        }

        neuralNetworkJson.add("neuralNetwork", new Gson().toJsonTree(neuralNetwork));
        return neuralNetworkJson;
    }

    public static JsonObject deleteNeuralNetworkByUUID(UUID uuid){
        boolean isDeleted = neuralNetworkData.getNeuralNetwork(uuid) == null;
        neuralNetworkData.deleteNeuralNetwork(uuid);
        JsonObject neuralNetworkJson = new JsonObject();

        if(isDeleted){
            return null;
        }

        neuralNetworkJson.add("neuralNetwork", new Gson().toJsonTree("Neural Network foi deletado - UUID: " + uuid.toString()));
        return neuralNetworkJson;
    }

    public static JsonObject deleteAllNeuralNetworks(){
        neuralNetworkData.deleteAllNeuralNetworks();
        JsonObject neuralNetworkJson = new JsonObject();
        neuralNetworkJson.add("neuralNetwork", new Gson().toJsonTree("Todas as Neural Networks foram deletadas"));
        return neuralNetworkJson;
    }

}
