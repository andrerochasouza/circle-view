package br.com.andre.api.aplicacao;

import br.com.andre.util.Curl;
import br.com.andre.util.YamlUtil;
import spark.utils.IOUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HealthCheck {

    public static String healthCheckByHttpUrl(int port, boolean enableAuth) {
        String url = "http://localhost:" + port + "/healthcheck";
        String response = "";

        if(enableAuth) {
            String username = YamlUtil.get("username").toString();
            String password = YamlUtil.get("password").toString();

            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setRequestProperty("Authorization", authHeader);

                response = IOUtils.toString(connection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                response = IOUtils.toString(connection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return response;
    }

}
