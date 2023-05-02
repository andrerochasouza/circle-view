# Usar python 3.7
# Usar: python run.py <path_to_image>

import sys

if sys.version_info < (3, 7):
    sys.exit("Este script requer Python 3.7 ou superior")

import tensorflow as tf
from PIL import Image
import numpy as np

model = tf.keras.models.load_model('C:/Users/1202000046/Desktop/java_projects/circle-view/ml-dl/model/best_model.h5')

path_image = sys.argv[1]

test_image = Image.open(path_image)
test_image = test_image.resize((32, 32))

test_image = np.array(test_image)

predicted_probabilities = model.predict(np.array([test_image]))
predicted_class = np.argmax(predicted_probabilities)

print(predicted_class)

with open("C:/Users/1202000046/Desktop/java_projects/circle-view/ml-dl/output.txt", "w") as f:
    print(predicted_class, file=f)