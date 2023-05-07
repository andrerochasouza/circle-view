package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.aplicacao.FlaskClient;
import br.com.andre.api.dominio.fbs.Image;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.io.IOException;

public class MLControllerV2 {

    private final static FlaskClient flaskClient = new FlaskClient();
    private final static Logger log = Logger.getLogger(MLControllerV2.class);

    public static JsonObject newPredict(byte[] imgBytes) throws RuntimeException, IOException {

        FlatBufferBuilder builder = new FlatBufferBuilder();
        int img = Image.createDataVector(builder, imgBytes);
        Image.startImage(builder);
        Image.addData(builder, img);
        int image = Image.endImage(builder);
        builder.finish(image);

        int predict = flaskClient.predict(builder);
        log.info("Requisição realizada com sucesso");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("predict", predict);

        return jsonResponse;
    }
}
