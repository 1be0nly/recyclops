const {Storage} = require('@google-cloud/storage');
const axios = require('axios');
const admin = require('firebase-admin');
require('dotenv').config();

const keyFilename = process.env.KEY_FILENAME;
const projectId = process.env.PROJECT_ID;

// Google Cloud Sotrage Service Account
const storage = new Storage({
    projectId,
    keyFilename
});

// Google Cloud Storage bucket name
const bucketName = process.env.BUCKET_NAME;

// Flask API URL
const flaskApiUrl = process.env.FLASK_API_URL;

// POST upload image to classify
const uploadImage = async (req, res) => {
  try {

    if (!req.file) {
      return res.status(400).json({error: 'Gambar tidak ditemukan!'});
    }
        const email = req.user.email;
        const userEmail = filterEmail(email);
        const wasteImage = req.file;
        const currentDate = new Date().toISOString().replace(/:/g, '-');
        const filename = `${userEmail}/${currentDate}.jpg`;

        await storage.bucket(bucketName).file(filename).save(wasteImage.buffer, {
            metadata: {
            contentType: 'image/jpeg',
        },
    });

        const imageUrl = `https://storage.googleapis.com/${bucketName}/${filename}`;
        const result = await axios.post(flaskApiUrl, {imageUrl});

    if (result.data.error) {
      return res.status(400).json({error: result.data.error});
    }

        const classificationResult = result.data.wasteType;
        const confidenceResult = result.data.confidence;

        res.status(200).json({imageUrl, wasteType: classificationResult, confidence: confidenceResult});
  } catch (error) {
        console.error(error);
        res.status(500).json({error: 'Internal server error upload'});
  }
};

// POST classified to database
const calculatePoint = async (req, res) => {
    try {
        const email = req.user.email;
        const userEmail = filterEmail(email);
        const wasteType = req.body.wasteType;
        const weight = req.body.weight;
        const imageUrl = req.body.imageUrl;
        const confidence = req.body.confidence

      let points = 0;
    
      function calculatePoints(wasteType) {
        switch (wasteType) {
          case 'Logam':
            return Math.floor(weight * 14);
          case 'Kaca':
            return Math.floor(weight * 13);
          case 'Kardus':
            return Math.floor(weight * 12);
          case 'Kertas':
            return Math.floor(weight * 11);
          case 'Plastik':
            return Math.floor(weight * 10);
          case 'Styrofoam':
            return Math.floor(weight * 9);
          default:
            return 0;
        }
      }
  
      const pointsForWasteType = calculatePoints(wasteType);
      points += pointsForWasteType;
  
        const dbRef = admin.database().ref('users/' + filterEmail(userEmail));
        await dbRef.push({userEmail, imageUrl, wasteType, confidence, weight, points});
        // await newData.set()
        
      res.status(200).json({points});
    } catch (error) {
      console.error(error);
      res.status(500).json({error: 'Internal server error point'});
    }
  };
  

// GET user data from database
const getUser = async (req, res) => {
    try {

      const userEmail = req.user.email;
      const dbRef = admin.database().ref('users').child(filterEmail(userEmail));
      const snapshot = await dbRef.once('value');
      const userData = snapshot.val();
  
      if (!userData) {
        return res.status(404).json({ error: 'User data not found' });
      }
  
      const userHistory = Object.values(userData);
  
      res.status(200).json({userHistory});
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: 'Internal server error' });
    }
  };

// GET user point from database
const getPoint = async (req, res) => {
    try {
        
      const userEmail = req.user.email;
      const dbRef = admin.database().ref('users/' + filterEmail(userEmail));
      const snapshot = await dbRef.once('value');
      const userData = snapshot.val();
  
      if (!userData) {
        return res.status(404).json({ error: 'User data not found' });
      }
  
      let totalPoints = 0;
      Object.values(userData).forEach((data) => {
        if (data.points) {
            totalPoints += data.points;
        }
      });

      res.status(200).json({totalPoints});
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: 'Internal server error' });
    }
  };
  
function filterEmail(email) {
    return email.replace(/[.#$\[\]]/g, '_');
  }

module.exports = {uploadImage, calculatePoint, getUser, getPoint};
