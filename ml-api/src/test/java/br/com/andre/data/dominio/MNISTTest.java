package br.com.andre.data.dominio;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MNISTTest {

    @Test
    public void testLoadTrainingData() throws IOException {
        MNIST mnist = new MNIST();
        mnist.loadTrainingData();

        // Verifica se o número de imagens e rótulos é o esperado
        assertEquals(60000, mnist.numImages());
        assertEquals(60000, mnist.getLabels().length);

        // Verifica se os valores das imagens estão no intervalo [0, 1]
        double[][] images = mnist.getImages();
        for (double[] image : images) {
            for (double pixel : image) {
                assertTrue(pixel >= 0.0 && pixel <= 1.0);
            }
        }

        // Verifica se os valores dos rótulos estão no intervalo [0, 1]
        double[][] labels = mnist.getLabels();
        for (double[] label : labels) {
            double sum = 0.0;
            for (double value : label) {
                assertTrue(value >= 0.0 && value <= 1.0);
                sum += value;
            }
            assertEquals(1.0, sum, 0.0);
        }
    }

}
