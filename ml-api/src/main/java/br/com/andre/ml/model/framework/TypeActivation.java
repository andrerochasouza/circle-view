package br.com.andre.ml.model.framework;

public enum TypeActivation {

    RELU,
    RELUDERIVATIVE,
    SIGMOID,
    SIGMOIDDERIVATIVE,
    SOFTMAX;

    public double activation(double value) {
        switch (this) {
            case RELU:
                return relu(value);
            case SIGMOID:
                return sigmoid(value);
            case SOFTMAX:
                return softmax(value);
            default:
                return 0;
        }
    }

    public double derivative(double value) {
        switch (this) {
            case RELUDERIVATIVE:
                return reluDerivative(value);
            case SIGMOIDDERIVATIVE:
                return sigmoidDerivative(value);
            default:
                return 0;
        }
    }

    private double relu(double value) {
        return Math.max(0, value);
    }

    private double reluDerivative(double value) {
        return value > 0 ? 1 : 0;
    }

    private double sigmoid(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    private double sigmoidDerivative(double value) {
        return value * (1 - value);
    }

    private double softmax(double value) {
        return Math.exp(value);
    }
}
