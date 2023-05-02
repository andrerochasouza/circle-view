package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.dominio.ResponseResource;
import br.com.andre.api.dominio.TypeStatus;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

public class HttpResponseV2 {

    private final static Logger log = Logger.getLogger(HttpResponseV2.class);

    public static ResponseResource newPredict(Request req, Response res){

        byte[] imgBytes = req.bodyAsBytes();

        JsonObject jsonResponse = MLControllerV2.newPredict(imgBytes);

        log.info("Predição realizada com sucesso! Resultado: " + jsonResponse.get("result").getAsString());
        return ResponseResource.of(res, jsonResponse, TypeStatus.OK);
    }

}
