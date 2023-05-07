package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.dominio.ResponseResource;
import br.com.andre.api.dominio.TypeStatus;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;


public class HttpResponseV2 {

    private final static Logger log = Logger.getLogger(HttpResponseV2.class);

    public static ResponseResource newPredict(Request req, Response res){
        try{
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            if(req.raw().getPart("file") == null){
                log.error("Nenhuma imagem recebida");
                return ResponseResource.ofError(res, "Nenhuma imagem recebida", TypeStatus.BAD_REQUEST);
            }
            byte[] imgBytes = req.raw().getPart("file").getInputStream().readAllBytes();

            JsonObject jsonResponse = MLControllerV2.newPredict(imgBytes);
            log.info("Predição realizada com sucesso! Resultado: " + jsonResponse.get("predict").getAsInt());

            return ResponseResource.of(res, jsonResponse, TypeStatus.OK);
        } catch (Exception e){
            log.error("Erro ao processar a imagem");
            return ResponseResource.ofError(res, "Erro ao processar a imagem", TypeStatus.BAD_REQUEST);
        }
    }

}
