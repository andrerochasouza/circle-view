package br.com.andre.api.aplicacao;

import br.com.andre.data.aplicacao.SQLiteConnection;
import br.com.andre.util.YamlUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import spark.Filter;
import spark.Spark;

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
            get("/trainbymnist", (req, res) -> HttpResponse.getTrainByMNIST(req, res), gson::toJson);
            get("/retrainbymnist", (req, res) -> HttpResponse.getRetrainByMNIST(req, res), gson::toJson);
            get("/healthcheck", (req, res) -> HttpResponse.getHealthCheck(req, res), gson::toJson);
            post("/train", (req, res) -> HttpResponse.newTrain(req, res), gson::toJson);
            post("/feedfoward", (req, res) -> HttpResponse.newFeedfoward(req, res), gson::toJson);
            path("/neuralnetwork", () -> {
                get("/list", (req, res) -> HttpResponse.getAllUUIDs(req, res), gson::toJson);
                get("/list/:uuid", (req, res) -> HttpResponse.getNeuralNetworkByUUID(req, res), gson::toJson);
                delete("/delete/:uuid", (req, res) -> HttpResponse.deleteNeuralNetworkByUUID(req, res), gson::toJson);
                delete("/deleteAll", (req, res) -> HttpResponse.deleteAllNeuralNetworks(req, res), gson::toJson);
            });
        });

        log.info("SERVER INICIADO NA PORTA - " + port);
    }
}
