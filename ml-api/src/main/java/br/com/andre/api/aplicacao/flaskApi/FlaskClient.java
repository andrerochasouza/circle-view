package br.com.andre.api.aplicacao.flaskApi;

import br.com.andre.util.YamlUtil;

import java.io.IOException;

public class FlaskClient {

    private OkHttpClient client = new OkHttpClient();

    public String predict(String imagePath) throws IOException{
        String url = YamlUtil.get("flaskUrl") + "/predict";

        RequestBody requestBody = new FormBody.Builder()
                .add("imagePath", imagePath)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Erro ao fazer solicitação para "+ url);
        }

        return response.body().string();
    }

}
