package br.com.andre.api.aplicacao;

import br.com.andre.util.Curl;

public class HealthCheck {

    public static boolean healthCheckByCurl(int port) {
        String url = "http://localhost:" + port + "/healthcheck";
        String result = new Curl().get(url);
        return result.contains("OK");
    }

}
