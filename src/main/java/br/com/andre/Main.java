package br.com.andre;

import br.com.andre.api.aplicacao.HttpClientApi;
import br.com.andre.util.YamlUtil;

public class Main {
    public static void main(String[] args)  {

        if (args.length == 1) YamlUtil.setPath(args[0]);

        HttpClientApi.initServer((int) YamlUtil.get("port"),
                (boolean) YamlUtil.get("enableCors"),
                (boolean) YamlUtil.get("enableAuthentication"));
    }
}