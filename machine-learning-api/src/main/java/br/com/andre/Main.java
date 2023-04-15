package br.com.andre;

import br.com.andre.api.aplicacao.HttpClientApi;
import br.com.andre.util.YamlUtil;

public class Main {
    public static void main(String[] args)  {
        HttpClientApi.initServer((int) YamlUtil.get("port"), (boolean) YamlUtil.get("enableCors"), (boolean) YamlUtil.get("enableAuthentication"));
    }
}