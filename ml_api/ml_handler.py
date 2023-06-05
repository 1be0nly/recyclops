from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from PIL import Image
import numpy as np

app = Flask(__name__)

# Load the pre-trained model
model = load_model('recyclemodel.h5')

# Define the class labels
class_labels = ['sampah bisa didaur ulang', 'sampah residu']

@app.route('/classify', methods=['POST'])
def classify_image():
    try:
        # Check if the image URL is present in the request
        if 'imageUrl' not in request.json:
            return jsonify({'error': 'No image URL provided'}), 400
        
        image_url = request.json['imageUrl']
        
        # Download the image from the provided URL
        image = Image.open(request.get(image_url, stream=True).raw)
        
        # Resize the image to the required input size
        image = image.resize((224, 224)) 
        
        # Preprocess the image
        image_array = np.array(image) / 255.0
        image_array = np.expand_dims(image_array, axis=0)
        
        # Make predictions
        predictions = model.predict(image_array)
        predicted_class = class_labels[np.argmax(predictions)]
        
        result = {'wasteType': predicted_class}
        
        return jsonify(result), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=8080)
