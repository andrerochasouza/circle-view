package br.com.andre.ml.model.framework;

public class NeuralNetworkCompile {

    private final Model model;

    private NeuralNetworkCompile(Model model){
        this.model = model;
    }

    static NeuralNetworkCompile compile(Model model){
        return new NeuralNetworkCompile(model);
    }

    void fit(double[][] imagesTrain, double[][] labelsTrain, int epochs){

    }

    void evaluate(double[][] imagesTest, double[][] labelsTest){
    }

}
