package br.com.andre.api.aplicacao.v1;

import br.com.andre.api.dominio.ResponseResource;
import br.com.andre.api.dominio.TypeStatus;
import br.com.andre.api.dominio.dtos.FrameDTO;
import br.com.andre.data.aplicacao.SQLiteConnection;
import br.com.andre.data.dominio.Pixel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.UUID;

public class HttpResponseV1 {

    private final static Logger log = Logger.getLogger(HttpResponseV1.class);

    public static ResponseResource getNewModel(Request req, Response res){

        JsonObject uuidJson =  MLControllerV1.newModelNeuralNetwork();

        log.info("Novo modelo criado com sucesso!");
        return ResponseResource.of(res, uuidJson, TypeStatus.OK);
    }

    public static ResponseResource getTrainByMNIST(Request req, Response res){

        JsonObject uuidJson =  MLControllerV1.internTrainByMNISTAndReturnUUID();

        log.info("Treinamento realizado com sucesso!");
        return ResponseResource.of(res, uuidJson, TypeStatus.OK);
    }

    public static ResponseResource getRetrainByMNIST(Request req, Response res){

        if(req.queryParams("uuid").isEmpty()){
            return ResponseResource.ofError(res, "Parâmetro 'uuid' não informado!", TypeStatus.BAD_REQUEST);
        }

        UUID uuid = UUID.fromString(req.queryParams("uuid"));

        JsonObject uuidJson =  MLControllerV1.internRetrainByMNISTAndReturnUUID(uuid);

        log.info("Retreinamento realizado com sucesso!");
        return ResponseResource.of(res, uuidJson, TypeStatus.OK);
    }

    public static ResponseResource getHealthCheck(Request req, Response res) {

        boolean isConnectionOpen = SQLiteConnection.getInstance().testConnection();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Status SQLite", isConnectionOpen ? "OK" : "ERRO");
        jsonObject.addProperty("Endpoint Health Check", "OK");

        log.info("Health Check realizado com sucesso!");
        return ResponseResource.of(res, jsonObject, TypeStatus.OK);
    }

    public static ResponseResource newTrain(Request req, Response res) {

        if (req.body().isEmpty()
                || req.queryParams("hiddenNodesSize1").isEmpty()
                || req.queryParams("hiddenNodesSize2").isEmpty()
                || req.queryParams("learningRate").isEmpty()) {
            return ResponseResource.ofError(res, "Corpo da requisição está vazio!", TypeStatus.BAD_REQUEST);
        }

        String body = req.body();
        int hiddenNodesSize1 = Integer.parseInt(req.queryParams("hiddenNodesSize1"));
        int hiddenNodesSize2 = Integer.parseInt(req.queryParams("hiddenNodesSize2"));
        double learningRate = Double.parseDouble(req.queryParams("learningRate"));
        UUID uuid = req.queryParams("uuid") != null ? UUID.fromString(req.queryParams("uuid")) : null;
        ArrayList<FrameDTO> frames = convertJsonToListFrameDTO(body);
        ArrayList<double[]> targets = convertJsonToListTargetsDTO(body);

        JsonObject networkWeights = MLControllerV1.trainAndReturnJsonNetworkWeights(frames, targets, hiddenNodesSize1, hiddenNodesSize2,
                learningRate, uuid);

        log.info("Treinamento realizado com sucesso!");
        return ResponseResource.of(res, networkWeights, TypeStatus.OK);
    }

    public static ResponseResource newFeedfoward(Request req, Response res) {

        if (req.body().isEmpty() || req.queryParams("uuid").isEmpty()) {
            return ResponseResource.ofError(res, "Requisição inválida!", TypeStatus.BAD_REQUEST);
        }

        String body = req.body();
        UUID uuid = UUID.fromString(req.queryParams("uuid"));
        FrameDTO frame = convertJsonToFrameDTO(body);

        JsonObject outputsJson = MLControllerV1.feedfowardAndReturnJsonOutputs(frame, uuid);

        log.info("Feedfoward realizado com sucesso!");
        return ResponseResource.of(res, outputsJson, TypeStatus.OK);

    }

    public static ResponseResource getAllUUIDs(Request req, Response res) {
        JsonObject uuidsJson = MLControllerV1.getAllUUIDs();
        log.info("Listagem de UUIDs realizada com sucesso!");
        return ResponseResource.of(res, uuidsJson, TypeStatus.OK);
    }

    public static ResponseResource getNeuralNetworkByUUID(Request req, Response res) {
        UUID uuid = UUID.fromString(req.params("uuid"));
        JsonObject neuralNetworkJson = MLControllerV1.getNeuralNetworkByUUID(uuid);

        if(neuralNetworkJson == null){
            log.info("UUID não encontrado!");
            return ResponseResource.ofError(res, "UUID não encontrado!", TypeStatus.NOT_FOUND);
        }

        log.info("Busca de rede neural por UUID realizada com sucesso!");
        return ResponseResource.of(res, neuralNetworkJson, TypeStatus.OK);
    }

    public static ResponseResource deleteNeuralNetworkByUUID(Request req, Response res) {
        UUID uuid = UUID.fromString(req.params("uuid"));
        JsonObject uuidJson = MLControllerV1.deleteNeuralNetworkByUUID(uuid);

        if(uuidJson == null){
            log.info("UUID não encontrado ou Neural Network já está deletado");
            return ResponseResource.ofError(res, "UUID não encontrado ou Neural Network já está deletado",
                    TypeStatus.NOT_FOUND);
        }

        log.info("Exclusão de rede neural por UUID realizada com sucesso!");
        return ResponseResource.of(res, uuidJson, TypeStatus.OK);
    }

    public static ResponseResource deleteAllNeuralNetworks(Request req, Response res) {
        JsonObject uuidsJson = MLControllerV1.deleteAllNeuralNetworks();

        log.info("Exclusão de todas as redes neurais realizada com sucesso!");
        return ResponseResource.of(res, uuidsJson, TypeStatus.OK);
    }


    private static ArrayList<FrameDTO> convertJsonToListFrameDTO(String body){
        ArrayList<FrameDTO> frames = new ArrayList<>();

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        JsonArray framesJsonArray = json.getAsJsonArray("frames");
        for (int i = 0; i < framesJsonArray.size(); i++) {
            JsonObject frameJson = framesJsonArray.get(i).getAsJsonObject();

            int id = frameJson.get("id").getAsInt();
            JsonArray pixelsJsonArray = frameJson.getAsJsonArray("pixels");

            ArrayList<Pixel> pixels = new ArrayList<>();
            for (int j = 0; j < pixelsJsonArray.size(); j++) {
                JsonObject pixelJson = pixelsJsonArray.get(j).getAsJsonObject();
                double value = pixelJson.get("value").getAsDouble();
                Pixel pixel = new Pixel(value);
                pixels.add(pixel);
            }

            FrameDTO frame = new FrameDTO(id, pixels);
            frames.add(frame);
        }

        return frames;
    }

    private static ArrayList<double[]> convertJsonToListTargetsDTO(String body){
        ArrayList<double[]> targets = new ArrayList<>();

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        JsonArray targetsJsonArray = json.getAsJsonArray("targets");
        for (int i = 0; i < targetsJsonArray.size(); i++) {
            JsonObject targetJson = targetsJsonArray.get(i).getAsJsonObject();
            JsonArray arrayTargetJson = targetJson.getAsJsonArray("arrayTarget");
            double[] target = new double[arrayTargetJson.size()];
            for (int j = 0; j < arrayTargetJson.size(); j++) {
                target[j] = arrayTargetJson.get(j).getAsDouble();
            }
            targets.add(target);
        }

        return targets;
    }

    private static FrameDTO convertJsonToFrameDTO(String body){
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        JsonObject frameJson = json.getAsJsonObject("frame");

        int id = frameJson.get("id").getAsInt();
        JsonArray pixelsJsonArray = frameJson.getAsJsonArray("pixels");

        ArrayList<Pixel> pixels = new ArrayList<>();
        for (int j = 0; j < pixelsJsonArray.size(); j++) {
            JsonObject pixelJson = pixelsJsonArray.get(j).getAsJsonObject();
            double value = pixelJson.get("value").getAsDouble();
            Pixel pixel = new Pixel(value);
            pixels.add(pixel);
        }

        FrameDTO frame = new FrameDTO(id, pixels);

        return frame;
    }

}
