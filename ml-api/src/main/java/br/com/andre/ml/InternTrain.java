package br.com.andre.ml;

import br.com.andre.data.aplicacao.NeuralNetworkData;
import br.com.andre.data.dominio.MNIST;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.UUID;

public class InternTrain {

    private MNIST mnist = new MNIST();
    private NeuralNetwork nn = new NeuralNetwork(784, new int[]{30, 30}, 10, 0.1);
    private final static Logger log = Logger.getLogger(InternTrain.class);

    public static void main(String[] args) {
        InternTrain internTrain = new InternTrain();

        int rand = (int) (Math.random() * 60000);
        internTrain.mnist.printImagemPixels(rand);
        double[] output = internTrain.nn.feedforward(internTrain.mnist.getImage(rand));
        for (int i = 0; i < 10; i++) {
            System.out.println("Output " + i + ": " + output[i]);
        }

        System.out.println("--------------------------------");

        internTrain.trainInternByMNIST();

        internTrain.mnist.printImagemPixels(rand);
        double[] output1 = internTrain.nn.feedforward(internTrain.mnist.getImage(rand));
        for (int i = 0; i < 10; i++) {
            System.out.println("Output1 " + i + ": " + output1[i]);
        }
    }

    public InternTrain() {
        try {
            mnist.loadTrainingData();
        } catch (IOException e) {
            log.info("Erro ao carregar MNIST na memória");
            e.printStackTrace();
        }
    }

    public UUID trainInternByMNISTAndSaveNN() {
        InternTrain internTrain = new InternTrain();
        log.info("Treinando Neural Network - UUID: " + internTrain.nn.getUuid());
        internTrain.trainInternByMNIST();

        NeuralNetworkData neuralNetworkData = new NeuralNetworkData();
        neuralNetworkData.saveNeuralNetwork(internTrain.nn);
        UUID uuid = internTrain.nn.getUuid();
        log.info("Neural Network salva com sucesso - UUID: " + uuid);
        return uuid;
    }

    public UUID retrainInternByMNISTAndSaveNN(UUID uuid) {
        InternTrain internTrain = new InternTrain();
        log.info("Retreinando Neural Network - UUID: " + uuid);
        internTrain.trainInternByMNIST(uuid);

        NeuralNetworkData neuralNetworkData = new NeuralNetworkData();
        neuralNetworkData.updateNeuralNetwork(internTrain.nn);
        log.info("Neural Network atualizada com sucesso - UUID: " + uuid);
        return uuid;
    }

    private void trainInternByMNIST() {
        double[][] images = mnist.getImages();
        double[][] labels = mnist.getLabels();
        int numEpochs = 150;
        int numImages = 60000;
        int totalProgress = numEpochs * numImages;
        int currentProgress = 0;

        for (int i = 0; i < numEpochs; i++) {
            for (int j = 0; j < numImages; j++) {
                nn.train(images[j], labels[j]);
                currentProgress++;
                int percentComplete = (int) ((double) currentProgress / totalProgress * 100);
                String progressBar = getProgressBar(percentComplete);
                System.out.print("\rTraining: [" + progressBar + "] " + percentComplete + "%");
            }
        }
        System.out.println();
    }

    private void trainInternByMNIST(UUID uuid){
        NeuralNetworkData neuralNetworkData = new NeuralNetworkData();
        nn = neuralNetworkData.getNeuralNetwork(uuid);
        this.trainInternByMNIST();
    }

    private String getProgressBar(int percentComplete) {
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if (i < percentComplete / 5) {
                progressBar.append("\\\\");
            } else {
                progressBar.append(" ");
            }
        }
        return progressBar.toString();
    }

}
