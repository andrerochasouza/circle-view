package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.aplicacao.flaskApi.FlaskClient;
import br.com.andre.api.dominio.TypeImage;
import br.com.andre.util.YamlUtil;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class MLControllerV2 {

    private final static FlaskClient flaskClient = new FlaskClient();
    private final static Logger log = Logger.getLogger(MLControllerV2.class);

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
        String response = flaskClient.predict(file.getPath());

        log.info("Deletando arquivo temporário");
        file.delete();

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("result", response);

        return jsonResponse;
    }
}
