# Usar python 3.7
# Usar: python run.py
import sys
import os
os.environ['TF_ENABLE_ONEDNN_OPTS'] = '1'

print('Python %s on %s' % (sys.version, sys.platform))

from fbs.Image import Image as image_fbs
import fbs.Predict  as predict_fbs
from flask import Flask, request
import tensorflow as tf
from PIL import Image
import numpy as np
import matplotlib.pyplot as plt
from io import BytesIO
import flatbuffers

app = Flask(__name__)

dir_path = os.path.dirname(os.path.abspath(__file__))
dir_path = os.path.join(dir_path, 'model_prod', 'model_prod.h5')

model = tf.keras.models.load_model(dir_path)

@app.route('/predict', methods=['POST'])
def predict():
    
    # Rebendo a request e convertendo para imagem pelo flatbuffers
    image_byte = request.data
    image = image_fbs.GetRootAs(image_byte, 0)
    image = image.DataAsNumpy()
    
    # Obtendo o flatbuffers e convetendo para imagem pelo PIL
    image = Image.open(BytesIO(image))

    image = image.resize((32, 32))
    image = np.array(image)

    # Predizendo a classe da imagem
    predict_probabilities = model.predict(np.array([image]))
    predict_class = np.argmax(predict_probabilities)
    predict_class = str(predict_class)

    # Serializando o resultado da predição
    builder = flatbuffers.Builder(0)
    predict_fbs.PredictStart(builder)
    predict_fbs.PredictAddPredict(builder, int(predict_class))
    predict = predict_fbs.PredictEnd(builder)
    builder.Finish(predict)

    # Retornando o resultado da predição
    return bytes(builder.Output())

if __name__ == '__main__':
    app.run(debug=True)