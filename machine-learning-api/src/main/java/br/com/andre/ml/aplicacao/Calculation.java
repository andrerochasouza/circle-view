package br.com.andre.ml.aplicacao;

import br.com.andre.data.dominio.Pixel;

public class Calculation {

    public static double train(Pixel[] pixels, double[] targets, double[] biasOcult, double[] bias, boolean randomWeights,boolean saveWeights){
        double[] inputs = new double[pixels.length];
        for (int i = 0; i < pixels.length; i++){
            inputs[i] = pixels[i].getValue();
        }

        double[][] weightsOcult = null;
        double[][] weights = null;
        double[] outputsOcult = null;
        double[] outputs = null;
        double[] outputErrors = null;
        double[] hiddenErrors = null;
        double learningRate = 0.5;
        double costError = 0;

        if(randomWeights){
            weightsOcult = Functions.getRandomWeights(10, 2);
            outputsOcult = Functions.feedForward(weightsOcult, inputs, biasOcult);

            weights = Functions.getRandomWeights(2, 10);
            outputs = Functions.feedForward(weights, outputsOcult, bias);
        }

        System.out.println("---- Ocult ----");
        for(double output : outputsOcult){
            System.out.println(output);
        }

        System.out.println("---- Outputs ----");
        for(double output : outputs){
            System.out.println(output);
        }

        outputErrors = Functions.outputErrors(outputs, targets);
        System.out.println("---- Outputs errors ----");
        for(double outputError : outputErrors){
            System.out.println(outputError);
        }

        hiddenErrors = Functions.hiddenErros(weights, outputErrors);
        System.out.println("---- Hidden errors ----");
        for(double hiddenError : hiddenErrors){
            System.out.println(hiddenError);
        }

        costError = Functions.costFunction(outputs, targets);
        System.out.println("---- Cost error ----");
        System.out.println(costError);

        return 0;
    }


}
