from flask import Flask, request, jsonify
from PIL import Image
import numpy as np
import tensorflow as tf

app = Flask(__name__)

# Load the machine learning models (replace with your own models)
model = tf.keras.models.load_model('path/to/model.h5')
class_labels = ['recycle', 'non-recyle']

@app.route('/classify', methods=['POST'])
def classify_image():
    file = request.files['image']
    image = Image.open(file).convert('RGB')
    image = image.resize((224, 224))  # Resize the image if needed
    image_array = np.array(image) / 255.0  # Normalize the image array

    # Perform waste classification
    predictions = model.predict(np.expand_dims(image_array, axis=0))
    predicted_class_index = np.argmax(predictions[0])
    predicted_class = class_labels[predicted_class_index]

    return jsonify({'wasteType': predicted_class})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
