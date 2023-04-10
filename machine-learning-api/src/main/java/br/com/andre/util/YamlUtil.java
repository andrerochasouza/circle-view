package br.com.andre.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

public class YamlUtil {

    public static Object get(String key) {

        Yaml yaml = new Yaml();
        InputStream inputStream = YamlUtil.class.getClassLoader().getResourceAsStream("application.yaml");

        if(Objects.isNull(inputStream)) {
            throw new RuntimeException("Arquivo application.yaml não encontrado");
        }

        Map<String, Object> obj = yaml.load(inputStream);

        if(Objects.isNull(obj.get(key))) {
            throw new RuntimeException("Valor da chave não encontrada - " + key);
        }

        return obj.get(key).toString();
    }

}
