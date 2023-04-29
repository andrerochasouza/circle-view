package br.com.andre.ml.model.framework;

public class Flattern extends Layer{

    private double[][] inputs;
    private int shape;

    private Flattern(double[][] inputs, int shape){
        super();
        this.inputs = inputs;
        this.shape = shape;
    }

    public static Flattern flattern(int x, int y, int shape){
        return new Flattern(new double[x*y][shape], shape);
    }

    public static Flattern flattern(int x, int y){
        return new Flattern(new double[x*y][1], 1);
    }

    void inputByInputsAndShapes(double[][] inputs, int shape){
        if (this.shape != shape){
            throw new IllegalArgumentException("O Shape do input não é compatível com o shape da camada de entrada.");
        }
        this.inputs = inputs;
    }

    void input(double[] input){
        if (this.shape != 1){
            throw new IllegalArgumentException("O Shape do input não é compatível com o shape da camada de entrada.");
        }
        for (int i = 0; i < input.length; i++){
            this.inputs[i][0] = input[i];
        }
    }

    @Override
    public int getParams(){
        return 0;
    }

    @Override
    public int getOutputShape(){
        return this.inputs.length;
    }

    public double[][] getInputs(){
        return this.inputs;
    }

    public int getShape(){
        return this.shape;
    }

    void setInputs(double[][] inputs){
        this.inputs = inputs;
    }

    void setShape(int shape){
        this.shape = shape;
    }

}
