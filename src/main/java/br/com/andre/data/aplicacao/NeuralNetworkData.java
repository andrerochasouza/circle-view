package br.com.andre.data.aplicacao;

import br.com.andre.ml.NeuralNetwork;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
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
                + "	hidden_weights TEXT NOT NULL,\n"
                + "	output_weights TEXT NOT NULL,\n"
                + "	bias1 TEXT NOT NULL,\n"
                + "	bias2 TEXT NOT NULL,\n"
                + "	outputs TEXT \n"
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
        String sql = "INSERT INTO neural_networks (uuid, hidden_weights, output_weights, bias1, bias2, outputs) VALUES (?, ?, ?, ?, ?, ?)";

        try{
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
        } catch (SQLException e) {
            log.info("Erro ao salvar rede neural");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateNeuralNetwork(NeuralNetwork network) {
        String sql = "UPDATE neural_networks SET hidden_weights = ?, output_weights = ?, bias1 = ?, bias2 = ?, outputs = ? WHERE uuid = ?";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, toJson(network.getHiddenWeights()));
            stmt.setString(2, toJson(network.getOutputWeights()));
            stmt.setString(3, toJson(network.getBias1()));
            stmt.setString(4, toJson(network.getBias2()));
            stmt.setString(5, toJson(network.getOutputs()));
            stmt.setString(6, network.getUuid().toString());
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
                neuralNetwork.setHiddenWeights(fromJson(rs.getString("hidden_weights"), double[][].class));
                neuralNetwork.setOutputWeights(fromJson(rs.getString("output_weights"), double[][].class));
                neuralNetwork.setBias1(fromJson(rs.getString("bias1"), double[].class));
                neuralNetwork.setBias2(fromJson(rs.getString("bias2"), double[].class));
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
                nn.setHiddenWeights(fromJson(rs.getString("hidden_weights"), double[][].class));
                nn.setOutputWeights(fromJson(rs.getString("output_weights"), double[][].class));
                nn.setBias1(fromJson(rs.getString("bias1"), double[].class));
                nn.setBias2(fromJson(rs.getString("bias2"), double[].class));
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
