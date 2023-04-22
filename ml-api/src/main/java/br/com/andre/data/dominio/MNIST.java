package br.com.andre.data.dominio;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MNIST {
    private static final String TRAIN_IMAGE_FILE = "train-images.idx3-ubyte";
    private static final String TRAIN_LABEL_FILE = "train-labels.idx1-ubyte";
    private static final int NUM_TRAIN_IMAGES = 60000;

    private double[][] images;
    private double[][] labels;

    public MNIST() {}

    public static void main(String[] args) {
        MNIST mnist = new MNIST();
        try {
            mnist.loadTrainingData();
            int random = (int) (Math.random() * 60000);
            mnist.printImagemPixels( random);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTrainingData() throws IOException {
        images = new double[NUM_TRAIN_IMAGES][784];
        labels = new double[NUM_TRAIN_IMAGES][10];

        InputStream imageStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(TRAIN_IMAGE_FILE));
        InputStream labelStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(TRAIN_LABEL_FILE));

        DataInputStream imageReader = new DataInputStream(imageStream);
        DataInputStream labelReader = new DataInputStream(labelStream);

        // Descartar o cabeçalho do arquivo de imagem
        imageReader.readInt();
        int numImages = imageReader.readInt();
        int numRows = imageReader.readInt();
        int numCols = imageReader.readInt();

        // Descartar o cabeçalho do arquivo de rótulo
        labelReader.readInt();
        int numLabels = labelReader.readInt();

        // Verificar que o número de imagens e rótulos é o mesmo
        if (numImages != numLabels) {
            throw new IOException("O número de imagens e rótulos não é o mesmo.");
        }

        // Ler os dados de imagem e rótulo
        for (int i = 0; i < NUM_TRAIN_IMAGES; i++) {
            for (int j = 0; j < 784; j++) {
                images[i][j] = imageReader.readUnsignedByte() / 255.0;
            }

            int label = labelReader.readUnsignedByte();
            labels[i][label] = 1.0;
        }

        imageReader.close();
        labelReader.close();
    }


    public void printImagemPixels(int indexImage) {
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                double pixelValue = images[indexImage][i*28+j];
                if (pixelValue > 0.9) {
                    System.out.print(" @ ");
                } else if (pixelValue > 0.1) {
                    System.out.print(" x ");
                } else {
                    System.out.print(" · ");
                }
            }
            if (i == 13) {
                System.out.print("|  Label: " + getLabelOneHotOneValue(labels[indexImage]));
            }
            System.out.println();
        }
    }

    public double[] getImage(int index) {
        return images[index];
    }

    public int numImages() {
        return NUM_TRAIN_IMAGES;
    }

    public double[] getOneHotLabel(int index) {
        return labels[index];
    }

    private int getLabelOneHotOneValue(double[] oneHotLabel) {
        for (int i = 0; i < oneHotLabel.length; i++) {
            if (oneHotLabel[i] == 1) {
                return i;
            }
        }
        return -1;
    }

    public double[][] getImages() {
        return images;
    }

    public void setImages(double[][] images) {
        this.images = images;
    }

    public double[][] getLabels() {
        return labels;
    }

    public void setLabels(double[][] labels) {
        this.labels = labels;
    }
}
