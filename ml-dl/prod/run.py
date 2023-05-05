# Usar python 3.7
# Usar: python run.py
import sys
import os
os.environ['TF_ENABLE_ONEDNN_OPTS'] = '1'

print('Python %s on %s' % (sys.version, sys.platform))

from flask import Flask, request, jsonify
import tensorflow as tf
from PIL import Image
import numpy as np

app = Flask(__name__)

dir_path = os.path.dirname(os.path.abspath(__file__))
dir_path = os.path.join(dir_path, 'model_prod', 'model_prod.h5')

model = tf.keras.models.load_model(dir_path)

@app.route('/predict', methods=['POST'])
def predict():
    image_file = request.files['image']
    image = Image.open(image_file)
    image = image.resize((32, 32))
    image = np.array(image)

    predict_probabilities = model.predict(np.array([image]))
    predict_class = np.argmax(predict_probabilities)
    predict_class = str(predict_class)

    return jsonify({'value': predict_class})

if __name__ == '__main__':
    app.run(debug=True)