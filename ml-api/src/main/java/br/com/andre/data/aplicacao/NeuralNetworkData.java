package br.com.andre.data.aplicacao;

import br.com.andre.ml.model.NeuralNetwork;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static br.com.andre.util.GsonUtil.fromJson;
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
                + "	outputNodes INTEGER NOT NULL,\n"
                + "	layers REAL NOT NULL,\n"
                + "	learningRate REAL NOT NULL,\n"
                + "	weights TEXT NOT NULL,\n"
                + " biases TEXT NOT NULL,\n"
                + " outputs TEXT NOT NULL\n"
                + ");";

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log.info("Erro ao criar tabela neural_networks");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void saveNeuralNetwork(NeuralNetwork network) {
        String sql = "INSERT INTO neural_networks (uuid, outputNodes, layers, learningRate, weights, biases, outputs) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);

            if(network.getUuid().toString() == null){
                network.setUuid(UUID.randomUUID());
            }

            stmt.setString(1, network.getUuid().toString());
            stmt.setInt(2, network.getOutputNodes());
            stmt.setInt(3, network.getLayers());
            stmt.setDouble(4, network.getLearningRate());
            stmt.setString(5, toJson(network.getWeights()));
            stmt.setString(6, toJson(network.getBiases()));
            stmt.setString(7, toJson(network.getOutputs()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("Erro ao salvar rede neural");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateNeuralNetwork(NeuralNetwork network) {
        String sql = "UPDATE neural_networks SET outputNodes = ?, layers = ?, learningRate = ?, weights = ?, biases = ?, outputs = ? WHERE uuid = ?";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, network.getOutputNodes());
            stmt.setInt(2, network.getLayers());
            stmt.setDouble(3, network.getLearningRate());
            stmt.setString(4, toJson(network.getWeights()));
            stmt.setString(5, toJson(network.getBiases()));
            stmt.setString(6, toJson(network.getOutputs()));
            stmt.setString(7, network.getUuid().toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.info("Erro ao atualizar rede neural");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteNeuralNetwork(UUID uuid) {
        String sql = "DELETE FROM neural_networks WHERE uuid = ?";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.info("Erro ao deletar rede neural");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteAllNeuralNetworks(){
        String sql = "DELETE FROM neural_networks";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.info("Erro ao deletar todas as redes neurais");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public NeuralNetwork getNeuralNetwork(UUID uuid) {
        String sql = "SELECT * FROM neural_networks WHERE uuid = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            NeuralNetwork neuralNetwork = null;
            if (rs.next()) {
                neuralNetwork = new NeuralNetwork();
                neuralNetwork.setUuid(UUID.fromString(rs.getString("uuid")));
                neuralNetwork.setOutputNodes(rs.getInt("outputNodes"));
                neuralNetwork.setLayers(rs.getInt("layers"));
                neuralNetwork.setLearningRate(rs.getDouble("learningRate"));
                neuralNetwork.setWeights(fromJson(rs.getString("weights"), double[][][].class));
                neuralNetwork.setBiases(fromJson(rs.getString("biases"), double[][].class));
                neuralNetwork.setOutputs(fromJson(rs.getString("outputs"), double[].class));
            }
            rs.close();
            stmt.close();
            return neuralNetwork;
        } catch (SQLException e) {
            log.info("Erro ao buscar rede neural");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ArrayList<NeuralNetwork> getAllNeuralNetworks() {
        String sql = "SELECT * FROM neural_networks";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<NeuralNetwork> neuralNetworks = new ArrayList<>();
            while (rs.next()) {
                NeuralNetwork nn = new NeuralNetwork();
                nn.setUuid(UUID.fromString(rs.getString("uuid")));
                nn.setOutputNodes(rs.getInt("outputNodes"));
                nn.setLayers(rs.getInt("layers"));
                nn.setLearningRate(rs.getDouble("learningRate"));
                nn.setWeights(fromJson(rs.getString("weights"), double[][][].class));
                nn.setBiases(fromJson(rs.getString("biases"), double[][].class));
                nn.setOutputs(fromJson(rs.getString("outputs"), double[].class));
                neuralNetworks.add(nn);
            }
            rs.close();
            stmt.close();
            return neuralNetworks;
        } catch (SQLException e) {
            log.info("Erro ao buscar todas as redes neurais");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ArrayList<UUID> getAllUUIDs(){
        String sql = "SELECT uuid FROM neural_networks";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<UUID> uuids = new ArrayList<>();
            while (rs.next()) {
                uuids.add(UUID.fromString(rs.getString("uuid")));
            }
            rs.close();
            stmt.close();
            return uuids;
        } catch (SQLException e) {
            log.info("Erro ao buscar todas as UUIDs disponiveis");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
