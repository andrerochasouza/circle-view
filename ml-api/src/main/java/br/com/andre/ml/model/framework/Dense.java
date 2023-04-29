package br.com.andre.ml.model.framework;

public class Dense extends Layer{

    private TypeActivation typeActivation;
    private double[][][] weights;
    private double[][] bias;
    private int nodes;
    private int shape;

    private Dense(TypeActivation typeActivation, double[][][] weights, double[][] bias, int nodes, int shape){
        super();
        this.typeActivation = typeActivation;
        this.weights = weights;
        this.bias = bias;
        this.nodes = nodes;
        this.shape = shape;
    }

    public static Dense dense(int nodes, TypeActivation typeActivation, int nodesPreviousLayer, int shape){
        return new Dense(typeActivation, new double[nodesPreviousLayer][nodes][shape], new double[nodes][shape], nodes, shape);
    }

    public static Dense dense(int nodes, TypeActivation typeActivation, int nodesPreviousLayer){
        return new Dense(typeActivation, new double[nodesPreviousLayer][nodes][1], new double[nodes][1], nodes, 1);
    }

    void feedfoward(){
        // TODO
    }

    void backpropagation(){
        // TODO
    }

    void updateWeights(){
        // TODO
    }

    @Override
    public int getParams(){
        return (nodes * weights.length) + bias.length;
    }

    @Override
    public int getOutputShape(){
        return this.nodes;
    }

    public TypeActivation getTypeActivation(){
        return this.typeActivation;
    }

    public double[][][] getWeights(){
        return this.weights;
    }

    public double[][] getBias(){
        return this.bias;
    }

    public int getNodes(){
        return this.nodes;
    }

    public int getShape(){
        return this.shape;
    }

    void setTypeActivation(TypeActivation typeActivation){
        this.typeActivation = typeActivation;
    }

    void setWeights(double[][][] weights){
        this.weights = weights;
    }

    void setBias(double[][] bias){
        this.bias = bias;
    }

    void setNodes(int nodes){
        this.nodes = nodes;
    }

    void setShape(int shape){
        this.shape = shape;
    }
}
