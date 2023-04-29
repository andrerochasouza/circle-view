package br.com.andre.ml.model.framework;

import br.com.andre.ml.model.framework.*;

public class ModelTesteMain {
    public static void main(String[] args) {

        Model model = Model.model(Optimizer.SGD, Loss.MSE);
        model.addLayer(Flattern.flattern(28, 28),
                Dense.dense(128, TypeActivation.RELU, 784),
                Dense.dense(64, TypeActivation.RELU, 128),
                Dense.dense(10, TypeActivation.SOFTMAX, 128));

        System.out.println(model.summary());
    }
}
