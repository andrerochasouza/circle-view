package br.com.andre.api.aplicacao;

import br.com.andre.api.dominio.ResponseResource;
import br.com.andre.api.dominio.TypeStatus;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

public class HttpResponse {

    private final static Logger log = Logger.getLogger(HttpResponse.class);

    public static ResponseResource getHealthCheck(Request req, Response res) {
        log.info("Health Check");
        return ResponseResource.of(res, "OK", TypeStatus.OK);
    }

    public static ResponseResource newTrain(Request req, Response res) {
        return null;
    }

    public static ResponseResource newFeedfoward(Request req, Response res) {
        return null;
    }

}
