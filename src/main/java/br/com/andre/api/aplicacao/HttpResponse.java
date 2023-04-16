package br.com.andre.api.aplicacao;

import br.com.andre.api.dominio.ResponseResource;
import br.com.andre.api.dominio.TypeStatus;
import br.com.andre.api.dominio.dtos.FrameDTO;
import br.com.andre.data.dominio.Pixel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class HttpResponse {

    private final static Logger log = Logger.getLogger(HttpResponse.class);

    public static ResponseResource getHealthCheck(Request req, Response res) {
        log.info("Health Check realizado com sucesso!");
        return ResponseResource.of(res, "OK", TypeStatus.OK);
    }

    public static ResponseResource newTrain(Request req, Response res) {

        if (req.body().isEmpty()
                || req.queryParams("hiddenNodesSize").isEmpty()
                || req.queryParams("epochs").isEmpty()
                || req.queryParams("learningRate").isEmpty()) {
            return ResponseResource.ofError(res, "Corpo da requisição está vazio!", TypeStatus.BAD_REQUEST);
        }

        String body = req.body();
        int hiddenNodesSize = Integer.parseInt(req.queryParams("hiddenNodesSize"));
        int epochs = Integer.parseInt(req.queryParams("epochs"));
        double learningRate = Double.parseDouble(req.queryParams("learningRate"));
        ArrayList<FrameDTO> frames = convertJsonToListFrameDTO(body);
        ArrayList<double[]> targets = convertJsonToListTargetsDTO(body);

        JsonObject networkWeights = MLController.trainAndReturnJsonNetworkWeights(frames, targets, hiddenNodesSize, epochs, learningRate);

        log.info("Treinamento realizado com sucesso!");
        return ResponseResource.of(res, networkWeights, TypeStatus.OK);
    }

    public static ResponseResource newFeedfoward(Request req, Response res) {

        if (req.body().isEmpty() || req.queryParams("hiddenNodesSize").isEmpty()) {
            return ResponseResource.ofError(res, "Requisição inválida!", TypeStatus.BAD_REQUEST);
        }

        String body = req.body();
        int hiddenNodesSize = Integer.parseInt(req.queryParams("hiddenNodesSize"));
        FrameDTO frame = convertJsonToFrameDTO(body);

        JsonObject outputsJson = MLController.feedfowardAndReturnJsonOutputs(frame, hiddenNodesSize);

        log.info("Feedfoward realizado com sucesso!");
        return ResponseResource.of(res, outputsJson, TypeStatus.OK);

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
                int x = pixelJson.get("x").getAsInt();
                int y = pixelJson.get("y").getAsInt();
                Pixel pixel = new Pixel(value, x, y);
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
            int x = pixelJson.get("x").getAsInt();
            int y = pixelJson.get("y").getAsInt();
            Pixel pixel = new Pixel(value, x, y);
            pixels.add(pixel);
        }

        FrameDTO frame = new FrameDTO(id, pixels);

        return frame;
    }

}
