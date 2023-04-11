package br.com.andre.ml.aplicacao;

public class Functions {

    /**
     *
     * Multiplicação de matrizes para calcular a saída de cada nó
     *
     * [w11 w12 w13]   [i1]   [b1]
     * [w21 w22 w23] * [i2] + [b2] = [o1 o2 o3]
     * [w31 w32 w33]   [i3]   [b3]
     *
     * @param weights Matriz de pesos
     * @param inputs Vetor de entradas
     * @param bias Vetor de bias
     * @return Array de saídas
     */
    public static double[] feedForward(double[][] weights, double[] inputs, double[] bias){

        double[] outputs = new double[weights.length];

        for (int i = 0; i < weights.length; i++){
            double sum = 0;
            for (int j = 0; j < weights[i].length; j++){
                sum += weights[i][j] * inputs[j] + bias[j];
            }
            outputs[i] = sigmoid(sum);
        }

        return outputs;

    }

    /**
     *
     * Função de pesos randomicos
     *
     * @param rows Quantidade de linhas
     *             [w11]
     *             [w21]
     *             [w31]
     * @param columns Quantidade de colunas
     *                [w11 w12 w13]
     * @return Matriz de pesos
     */
    public static double[][] getRandomWeights(int rows, int columns){
        double[][] weights = new double[rows][columns];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                weights[i][j] = Math.round(100 * Math.random()) / 100.0;
            }
        }
        return weights;
    }

    /**
     *
     * Função de ativação - Sigmoid
     *
     * @param value Valor a ser calculado
     *              1 / (1 + e^-value)
     * @return Valor calculado
     */
    public static double sigmoid(double value){
        return Math.round(100 * (1 / (1 + Math.exp(-value)))) / 100.0;
    }

    /**
     *
     * Calculo do erro de cada nó da camada de saída
     *
     * @param outputs Saídas da camada
     *                [o1 o2 o3]
     * @param targets Valores desejados
     *                [t1 t2 t3]
     * @return Array de erros
     *       [e1 e2 e3]
     */
    public static double[] outputErrors(double[] outputs, double[] targets){
        double[] errors = new double[outputs.length];
        for (int i = 0; i < outputs.length; i++){
            double error = targets[i] - outputs[i];
            errors[i] = Math.round(100 * error) / 100.0;
        }
        return errors;
    }

    /**
     *
     * Calculo do erro de cada nó da camada oculta
     *
     * @param weigths Matriz de pesos
     *                [w11 w12 w13]
     *                [w21 w22 w23]
     * @param outputErrors Array de erros da camada de saída
     *                     [e1 e2 e3]
     * @return Array de erros
     *        [he1 he2 he3]
     */
    public static double[] hiddenErros(double[][] weigths, double[] outputErrors){
        double[] hiddenErrors = new double[weigths.length];

        for (int i = 0; i < weigths.length; i++){
            double sum = 0;
            for (int j = 0; j < weigths[i].length; j++){
                sum += weigths[i][j] * outputErrors[j];
            }
            hiddenErrors[i] = Math.round(100 * sum) / 100.0;
        }

        return hiddenErrors;
    }

    /**
     *
     * Função de custo - MSE (Erro médio quadrático)
     *
     * @param outputs Saídas da rede
     *                [o1 o2 o3]
     * @param targets Valores desejados
     *                [t1 t2 t3]
     * @return Custo
     */
    public static double costFunction(double[] outputs, double[] targets){
        double sum = 0;
        for (int i = 0; i < outputs.length; i++){
            sum += Math.pow(targets[i] - outputs[i], 2);
        }
        return sum / outputs.length;
    }

}
