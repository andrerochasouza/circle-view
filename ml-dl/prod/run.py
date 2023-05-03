# Usar python 3.7
# Usar: python run.py <path_to_image>
import sys
import os

if sys.version_info == (3, 7):
    sys.exit('Este Script requer Python 3.7')


from flask import Flask, request, jsonify
import tensorflow as tf
from PIL import Image
import numpy as np

app = Flask(__name__)

dir_path = os.path.dirname(os.path.abspath(__file__))

model = tf.keras.models.load_model(dir_path, 'model_prod', 'model_prod.h5')

@app.route('/predict', methods=['POST'])
def predict():
    image_file = request.files['image']
    image = Image.open(image_file)
    image = image.resize((32, 32))
    image = np.array(image)

    predict_probabilities = model.predict(np.array([image]))
    predict_class = np.argmax(predict_probabilities)

    return jsonify({'class': predict_class})

if __name__ == '__main__':
    app.run(debug=True)