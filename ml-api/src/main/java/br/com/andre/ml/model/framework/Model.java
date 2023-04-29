package br.com.andre.ml.model.framework;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Model {

    private ArrayList<Object> layers = new ArrayList<>();
    private final double learningRate;
    private final Optimizer optimizer;
    private final Loss loss;
    private NeuralNetworkCompile neuralNetworkCompile;

    private Model(ArrayList<Object> layers, double learningRate, Optimizer optimizer, Loss loss){
        this.layers = layers;
        this.learningRate = learningRate;
        this.optimizer = optimizer;
        this.loss = loss;
    }

    private Model(double learningRate, Optimizer optimizer, Loss loss){
        this.learningRate = learningRate;
        this.optimizer = optimizer;
        this.loss = loss;
    }

    public static Model model(Optimizer optimizer, Loss loss, Layer... layers){
        if(Objects.nonNull(layers) && layers.length > 0){
            return new Model(new ArrayList<>(Arrays.asList(layers)), 0.01, optimizer, loss);
        }

        return new Model(0.01, optimizer, loss);
    }

    public void addLayer(Layer... layer){
        if(Objects.nonNull(layer) && layer.length > 0){
            this.layers.addAll(Arrays.asList(layer));
        } else {
            throw new RuntimeException("Layer está nula ou vazia");
        }
    }

    public String summary(){

        int[] params = new int[this.layers.size()];
        ArrayList<String> layersName = new ArrayList<>();
        int totalParams = 0;

        for (int i = 0; i < this.layers.size(); i++) {
            params[i] = ((Layer) this.layers.get(i)).getParams();
            totalParams += params[i];

            if(this.layers.get(i) instanceof Dense){
                layersName.add("dense_" + i);
            } else if(this.layers.get(i) instanceof Flattern){
                layersName.add("flattern_" + i);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Layer (type)                 Output Shape              Param #\n");
        sb.append("================================================================\n");
        for (int i = 0; i < this.layers.size(); i++) {
            sb.append(String.format("%-30s", layersName.get(i)));
            sb.append(String.format("%-26s", ((Layer) this.layers.get(i)).getOutputShape()));
            sb.append(String.format("%-13s", params[i]));
            sb.append("\n");
            sb.append("________________________________________________________________\n");
        }
        sb.append(String.format("Total params: %d\n", totalParams));

        return sb.toString();
    }

    public void compile(Model model){
        this.neuralNetworkCompile = NeuralNetworkCompile.compile(model);
    }

    public void fit(double[][] images, double[][] labels, int epochs){
        this.neuralNetworkCompile.fit(images, labels, epochs);
    }
}

