package br.com.andre.api.aplicacao;

import com.google.gson.Gson;

import static spark.Spark.*;

public class HttpClientApi {

    private static final Gson gson = new Gson();

    public static void initServer(int port, boolean enableHealthCheck, boolean enableCORS, boolean enableAuth) {


        // Conexão com o banco de dados
        // if(!SQLiteConnection.getInstance().testConnection()){
        //     System.out.println("Erro ao conectar com o banco de dados - SQLiteConnection");
        //     System.exit(0);
        // }

        // Configurações do Spark
        port(port);
        threadPool(8, 2, 30000);

        // CORS e Headers de Segurança && Autenticação
        before("/*", (req, res) -> {

            // CORS e Headers de Segurança
            if(enableCORS){
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                res.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Content-Length, Accept, Origin,");
                res.header("X-Frame-Options", "SAMEORIGIN");
                res.header("X-XSS-Protection", "1; mode=block");
                res.header("X-Content-Type-Options", "nosniff");
                res.header("Content-Security-Policy", "default-src 'self'");
                res.header("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
                res.header("Referrer-Policy", "no-referrer-when-downgrade");
            }

            if(enableAuth){
                // TODO: Implementar autenticação
            }

        });

        // HealthCheck
        if(enableHealthCheck){
            get("/healthcheck", (req, res) -> "OK");

            if (!HealthCheck.healthCheckByCurl(port) && enableHealthCheck) {
                System.out.println("Erro ao iniciar o servidor - HealthCheck");
                System.exit(0);
            }
        }

        // Mapeamento de Paths do API
        path("/api", () -> {
            get("", (req, res) -> HttpResponse.getFirstFrame(req, res), gson::toJson);
            post("/train", (req, res) -> HttpResponse.newTrain(req, res), gson::toJson);
            post("/feedfoward", (req, res) -> HttpResponse.newFeedfoward(req, res), gson::toJson);
        });

        System.out.println("Servidor rodando na porta " + port);
    }
}
