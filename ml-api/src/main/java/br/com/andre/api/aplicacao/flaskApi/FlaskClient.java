package br.com.andre.api.aplicacao.flaskApi;

import br.com.andre.api.aplicacao.v2.MLControllerV2;
import br.com.andre.util.YamlUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlaskClient {

    private OkHttpClient client = new OkHttpClient();
    private final static Logger log = Logger.getLogger(FlaskClient.class);

    public String predict(String imagePath) throws IOException {
        String url = YamlUtil.get("flaskUrl") + "/predict";

        Path path = Paths.get(imagePath);
        String fileName = path.getFileName().toString();

        byte[] fileContent = Files.readAllBytes(path);

        log.info("Criando requisição para " + url);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", fileName,
                        RequestBody.create(MediaType.parse("image/*"), fileContent))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Erro ao fazer solicitação para " + url);
        }

        if(response.body() == null){
            throw new IOException("Resposta vazia");
        }

        return response.body().string();
    }
}
