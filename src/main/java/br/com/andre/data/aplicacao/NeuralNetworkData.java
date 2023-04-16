package br.com.andre.data.aplicacao;

import br.com.andre.ml.NeuralNetwork;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import static br.com.andre.util.GsonUtil.toJson;

public class NeuralNetworkData {

    private static Logger log = Logger.getLogger(NeuralNetworkData.class);
    private Connection connection;

    public NeuralNetworkData() {
        this.connection = SQLiteConnection.getInstance().getConnection();
        this.createTable();
    }

    private void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS neural_networks (\n"
                + "	uuid TEXT PRIMARY KEY,\n"
                + "	hidden_weights TEXT NOT NULL,\n"
                + "	output_weights TEXT NOT NULL,\n"
                + "	bias1 TEXT NOT NULL,\n"
                + "	bias2 TEXT NOT NULL,\n"
                + "	outputs TEXT \n"
                + ");";

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
            log.info("Tabela Neural Network criada com sucesso!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveNeuralNetwork(NeuralNetwork network) throws SQLException {
        String sql = "INSERT INTO neural_networks (id, hidden_weights, output_weights, bias1, bias2, outputs) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);

        if(network.getUuid().toString() == null){
            network.setUuid(UUID.randomUUID());
        }

        stmt.setString(1, network.getUuid().toString());
        stmt.setString(2, toJson(network.getHiddenWeights()));
        stmt.setString(3, toJson(network.getOutputWeights()));
        stmt.setString(4, toJson(network.getBias1()));
        stmt.setString(5, toJson(network.getBias2()));
        stmt.setString(6, toJson(network.getOutputs()));
        stmt.executeUpdate();
    }

    public void updateNeuralNetwork(NeuralNetwork network) throws SQLException {
        String sql = "UPDATE neural_networks SET hidden_weights = ?, output_weights = ?, bias1 = ?, bias2 = ?, outputs = ? WHERE uuid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, toJson(network.getHiddenWeights()));
        stmt.setString(2, toJson(network.getOutputWeights()));
        stmt.setString(3, toJson(network.getBias1()));
        stmt.setString(4, toJson(network.getBias2()));
        stmt.setString(5, toJson(network.getOutputs()));
        stmt.setString(6, network.getUuid().toString());
        stmt.executeUpdate();
    }

    public void deleteNeuralNetwork(UUID uuid) throws SQLException {
        String sql = "DELETE FROM neural_networks WHERE uuid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uuid.toString());
        stmt.executeUpdate();
    }

    public NeuralNetwork getNeuralNetwork(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM neural_networks WHERE uuid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uuid.toString());
        stmt.executeQuery();
        return null;
    }

    public ArrayList<NeuralNetwork> getAllNeuralNetworks() throws SQLException {
        String sql = "SELECT * FROM neural_networks";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeQuery();
        return null;
    }
}
