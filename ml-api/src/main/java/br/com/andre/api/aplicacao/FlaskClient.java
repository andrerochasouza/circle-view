package br.com.andre.api.aplicacao;

import br.com.andre.api.dominio.fbs.Predict;
import br.com.andre.util.YamlUtil;
import com.google.flatbuffers.FlatBufferBuilder;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FlaskClient {

    private final OkHttpClient client = new OkHttpClient();
    private final static Logger log = Logger.getLogger(FlaskClient.class);

    public int predict(FlatBufferBuilder builder) throws IOException, NullPointerException {
        String url = YamlUtil.get("flaskUrl") + "/predict";

        RequestBody requestBody = RequestBody.create(builder.sizedByteArray(),
                MediaType.parse("application/octet-stream"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {

            ByteBuffer bufferResponse = ByteBuffer.wrap(response.body().bytes());
            Predict predictObj = Predict.getRootAsPredict(bufferResponse);

            return predictObj.predict();
        }
    }
}
