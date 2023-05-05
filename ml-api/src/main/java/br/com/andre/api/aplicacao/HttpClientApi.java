package br.com.andre.api.aplicacao;

import br.com.andre.api.aplicacao.v1.HttpResponseV1;
import br.com.andre.api.aplicacao.v2.HttpResponseV2;
import br.com.andre.data.aplicacao.SQLiteConnection;
import br.com.andre.util.YamlUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import spark.Spark;

import javax.servlet.MultipartConfigElement;
import java.util.Base64;

import static spark.Spark.*;

public class HttpClientApi {

    private static final Gson gson = new Gson();
    private static final Logger log = Logger.getLogger(HttpClientApi.class);

    public static void initServer(int port, boolean enableCORS, boolean enableAuth) {

         // Conexão com o banco de dados
         if(!SQLiteConnection.getInstance().testConnection()){
             log.info("Erro ao conectar com o banco de dados");
             System.exit(0);
         }

        // Configurações do Spark
        port(port);
        threadPool(8, 2, 30000);

        // CORS e Headers de Segurança
        if(enableCORS) CorsFilter.apply();

        // Autenticação
        before("/*", (req, res) -> {

            if(enableAuth){
                // basic auth

                String auth = req.headers("Authorization");
                String username = YamlUtil.get("username").toString();
                String password = YamlUtil.get("password").toString();
                boolean authenticated = false;

                if (auth != null && auth.startsWith("Basic ")) {
                    String base64Credentials = auth.substring("Basic".length()).trim();
                    String credentials = new String(Base64.getDecoder().decode(base64Credentials),"UTF-8");

                    final String[] values = credentials.split(":", 2);
                    if (values.length == 2) {
                        if (username.equals(values[0]) && password.equals(values[1])) {
                            authenticated = true;
                        }
                    }
                }

                if (!authenticated) {
                    halt(401, "Não autorizado - Basic Auth");
                }

            }

        });

        // Mapeamento de Paths do API
        path("/api", () -> {
            get("/healthcheck", HttpResponseV1::getHealthCheck, gson::toJson);
            path("/v1", () -> {
                get("/trainbymnist", HttpResponseV1::getTrainByMNIST, gson::toJson);
                get("/retrainbymnist", HttpResponseV1::getRetrainByMNIST, gson::toJson);
                post("/train", HttpResponseV1::newTrain, gson::toJson);
                post("/feedfoward", HttpResponseV1::newFeedfoward, gson::toJson);
                path("/neuralnetwork", () -> {
                    get("/newmodel", HttpResponseV1::getNewModel, gson::toJson);
                    get("/list", HttpResponseV1::getAllUUIDs, gson::toJson);
                    get("/list/:uuid", HttpResponseV1::getNeuralNetworkByUUID, gson::toJson);
                    delete("/delete/:uuid", HttpResponseV1::deleteNeuralNetworkByUUID, gson::toJson);
                    delete("/deleteall", HttpResponseV1::deleteAllNeuralNetworks, gson::toJson);
                });
            });
            path("/v2", ()->{
                path("/cnn-cifar10", ()->{
                    post("/predict", HttpResponseV2::newPredict, gson::toJson);
                });
            });
        });

        log.info("SERVER INICIADO NA PORTA - " + port);
    }
}
