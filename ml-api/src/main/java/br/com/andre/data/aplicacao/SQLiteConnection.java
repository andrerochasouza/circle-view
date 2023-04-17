package br.com.andre.data.aplicacao;

import br.com.andre.util.YamlUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static Logger log = Logger.getLogger(SQLiteConnection.class);
    private static SQLiteConnection instance;
    private Connection connection;

    private SQLiteConnection() {
        try {

            String path = YamlUtil.get("pathDB").toString() + "/db.sqlite";

            File file = new File(path);

            if(!file.exists()){
                log.info("Arquivo db.sqlite não encontrado");
                log.info("Criando arquivo db.sqlite...");

                boolean fileCreated = file.createNewFile();

                if(fileCreated){
                    log.info("Arquivo db.sqlite criado com sucesso!");
                } else {
                    log.info("Erro ao criar arquivo db.sqlite");
                }
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        } catch (SQLException e) {
            log.info("Erro ao conectar com o banco de dados");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SQLiteConnection getInstance() {
        if (instance == null) {
            instance = new SQLiteConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean testConnection() {
        if (connection != null) {
            log.info("Conexão com o banco de dados estabelecida.");
            return true;
        } else {
            log.info("Conexão com o banco de dados não estabelecida.");
            return false;
        }
    }


}
