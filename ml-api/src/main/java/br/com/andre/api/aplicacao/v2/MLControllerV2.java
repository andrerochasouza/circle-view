package br.com.andre.api.aplicacao.v2;

import br.com.andre.api.dominio.TypeImage;
import br.com.andre.util.YamlUtil;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.io.*;

public class MLControllerV2 {

    private final static Logger log = Logger.getLogger(MLControllerV2.class);

    public static JsonObject newPredict(byte[] imgBytes) {

        try {
            log.info("Iniciando o processamentro da imagem...");

            TypeImage typeImage = TypeImage.fromBytes(imgBytes);

            File tempFile = new File(YamlUtil.get("pathPython") + "/temp." + typeImage.getExtension());

            FileOutputStream outputStream = new FileOutputStream(tempFile);
            outputStream.write(imgBytes);
            outputStream.close();

            log.info("Imagem temporária criada com sucesso!");

            int result = runPythonModel(typeImage.getExtension());

            if(!tempFile.delete()){
                log.error("Erro ao excluir o arquivo temporário");
                throw new RuntimeException("Erro ao excluir o arquivo temporário");
            }

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("result", result);

            return jsonResponse;

        } catch (IOException e) {
            log.error("Erro ao salvar arquivo temporário");
            throw new RuntimeException("Erro ao salvar arquivo temporário", e);
        }
    }

    private static int runPythonModel(String extension) {
        try {
            log.info("Iniciando o processamento do modelo em Python...");
            log.info("Comando: " + YamlUtil.get("pathPython") + "/.env/Scripts/python.exe " + YamlUtil.get("pathPython") + "/run.py " + " " + YamlUtil.get("pathPython") + "/temp." + extension);
            Process process = Runtime.getRuntime().exec(YamlUtil.get("pathPython") + "/.env/Scripts/python.exe " + YamlUtil.get("pathPython") + "/run.py " + " " + YamlUtil.get("pathPython") + "/temp." + extension);
            int exitCode = process.waitFor();
            if(exitCode != 0){
                log.error("Erro ao executar o arquivo run.py em Python");
                throw new RuntimeException("Erro ao executar o arquivo run.py em Python");
            }

            log.info("Processamento do modelo em Python finalizado com sucesso!");

            String lastLine = null;
            try(BufferedReader reader = new BufferedReader(new FileReader(YamlUtil.get("pathPython") + "/output.txt"))){
                String line;
                while ((line = reader.readLine()) != null) {
                    lastLine = line;
                }
            }

            if(lastLine == null){
                log.error("Não foi possível ler a saída do modelo em Python");
                throw new RuntimeException("Não foi possível ler a saída do modelo em Python");
            }

            return Integer.parseInt(lastLine);

        } catch (IOException | InterruptedException e) {
            log.error("Erro ao executar o modelo em Python", e);
            throw new RuntimeException("Erro ao executar o modelo em Python", e);
        }
    }
}
