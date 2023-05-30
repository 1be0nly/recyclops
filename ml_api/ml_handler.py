from flask import Flask, request, jsonify
import tensorflow as tf
from PIL import Image
import numpy as np

app = Flask(__name__)

# Load model
model = tf.keras.models.load_model('ml_api/recyclemodel.h5')

# Class labels
class_labels = ['recycle', 'non-recycle']

@app.route('/classify', methods=['POST'])
def classify_image():
    try:
        # Check image URL request
        if 'imageUrl' not in request.json:
            return jsonify({'error': 'ImageURL Kosong'}), 400
        
        image_url = request.json['imageUrl']
        
        # Download  image from imageURL
        image = Image.open(requests.get(image_url, stream=True).raw)
 
        #Resize image
        image = image.resize((225, 0)) 
        
        # Preprocess image
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
    app.run(debug=True)
