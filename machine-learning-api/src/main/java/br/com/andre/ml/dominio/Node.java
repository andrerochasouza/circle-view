package br.com.andre.ml.dominio;

public class Node {

    private double[] inputs;
    private double[] weights;
    private double bias;

    public Node(double[] inputs, double[] weights, double bias) {
        if(inputs.length != weights.length){
            throw new IllegalArgumentException("Pesos e entradas devem ter o mesmo tamanho");
        }
        this.inputs = inputs;
        this.weights = weights;
        this.bias = bias;
    }

    public double[] getInputs() {
        return inputs;
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
