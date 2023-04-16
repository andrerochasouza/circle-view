package br.com.andre.util;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

public class YamlUtil {

    private static Logger log = Logger.getLogger(YamlUtil.class);
    private static String path;

    public static Object get(String key) {
        try {

            Yaml yaml = new Yaml();

            InputStream inputStream = YamlUtil.class.getClassLoader().getResourceAsStream("application.yaml");

            if(path != null){
                File file = new File(path);
                inputStream = new FileInputStream(file);
            }

            if(Objects.isNull(inputStream)) {
                log.info("Arquivo application.yaml não encontrado");
                throw new RuntimeException("Arquivo application.yaml não encontrado");
            }

            Map<String, Object> obj = yaml.load(inputStream);

            if(Objects.isNull(obj.get(key))) {
                log.info("Valor da chave não encontrada - " + key);
                throw new RuntimeException("Valor da chave não encontrada - " + key);
            }

            inputStream.close();
            return obj.get(key);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPath(String path) {
        YamlUtil.path = path;
    }

}
