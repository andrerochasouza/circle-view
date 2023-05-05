package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.dominio.ResponseResource;
import br.com.andre.api.dominio.TypeImage;
import br.com.andre.api.dominio.TypeStatus;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.MultiPartFormDataCompliance;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class HttpResponseV2 {

    private final static Logger log = Logger.getLogger(HttpResponseV2.class);

    public static ResponseResource newPredict(Request req, Response res){
        try{
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            byte[] imgBytes = req.raw().getPart("file").getInputStream().readAllBytes();
            log.info("Imagem recebida");
            JsonObject jsonResponse = MLControllerV2.newPredict(imgBytes);

            log.info("Predição realizada com sucesso! Resultado: " + jsonResponse.get("result").getAsString());
            return ResponseResource.of(res, jsonResponse, TypeStatus.OK);
        } catch (Exception e){
            log.error("Erro ao processar a imagem");
            return ResponseResource.ofError(res, "Erro ao processar a imagem", TypeStatus.BAD_REQUEST);
        }
    }

}
