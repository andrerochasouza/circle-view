package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.aplicacao.flaskApi.FlaskClient;
import br.com.andre.api.dominio.TypeImage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MLControllerV2 {

    private final static FlaskClient flaskClient = new FlaskClient();
    private final static Logger log = Logger.getLogger(MLControllerV2.class);
    private final static Gson gson = new Gson();

    public static JsonObject newPredict(byte[] imgBytes) throws RuntimeException, IOException {

        log.info("Iniciando o processamentro da imagem...");

        TypeImage typeImage = TypeImage.fromBytes(imgBytes);

        File resourceDirectory = new File("src/main/resources");
        String resourcePath = resourceDirectory.getAbsolutePath();

        File file = new File(resourcePath + "/images/temp." + typeImage.getExtension());

        log.info("Criando imagem temporária");
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(imgBytes);
        outputStream.close();

        log.info("Realizando requisição para o Api Flask");
        String responsebody = flaskClient.predict(file.getPath());

        JsonObject responseJson = gson.fromJson(responsebody, JsonObject.class);
        int predictValue = Integer.parseInt(responseJson.get("value").getAsString());

        log.info("Deletando arquivo temporário");
        if(!file.delete()){
            log.error("Erro ao deletar arquivo temporário");
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("result", predictValue);

        return jsonResponse;
    }
}
