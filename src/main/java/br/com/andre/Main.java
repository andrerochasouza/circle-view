package br.com.andre;

import br.com.andre.api.aplicacao.HttpClientApi;
import br.com.andre.util.YamlUtil;
import org.apache.log4j.Logger;

public class Main {
    public static void main(String[] args)  {

        Logger log = Logger.getLogger(Main.class);
        log.info("Iniciando aplicação...");

        if (args.length == 1) {
            log.info("Usando arquivo de configuração Yaml no caminho: " + args[0]);
            YamlUtil.setPath(args[0]);
        }

        HttpClientApi.initServer((int) YamlUtil.get("port"),
                (boolean) YamlUtil.get("enableCors"),
                (boolean) YamlUtil.get("enableAuthentication"));
    }
}