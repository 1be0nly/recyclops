const {Storage} = require('@google-cloud/storage');
const {v4: uuidv4} = require('uuid');
const axios = require('axios');
const admin = require('firebase-admin');

const storage = new Storage({
    projectId: 'recyclops-prototype',
    keyFilename: 'main_api/recyclops-prototype.json',
});

// Google Cloud Storage bucket name
const bucketName = 'recyclops-bucket';

// URL for image classification
const flaskApiUrl = 'https://ml-api-3225qxd5uq-as.a.run.app/classify';


// POST upload image to classify
const uploadImage = async (req, res) => {
  try {

    if (!req.file) {
      return res.status(400).json({error: 'Gambar tidak ditemukan!'});
    }

        const wasteImage = req.file;
        const wasteWeight = 0;
        const points = 0;
        const filename = uuidv4() + '.jpg';

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

        const userId = req.user.uid;
        const dbRef = admin.database().ref('users');
        const userRef = dbRef.child(userId);

        userRef.push({imageUrl, weight: wasteWeight, points, wasteType: classificationResult, confidence: confidenceResult,
    });

        res.status(200).json({imageUrl, wasteType: classificationResult, confidence: confidenceResult});
  } catch (error) {
        console.error(error);
        res.status(500).json({error: 'Internal server error'});
  }
};

// POST classified to database
const calculatePoint = async (req, res) => {
    try {
      const userId = req.user.uid;
      const {wasteType, weight} = req.body;
  
      let points = 0;
  
      switch (wasteType) {
        case 'Logam':
          points = Math.floor(weight * 14);
          break;
        case 'Kaca':
          points = Math.floor(weight * 13);
          break;
        case 'Kardus':
          points = Math.floor(weight * 12);
          break;
        case 'Kertas':
          points = Math.floor(weight * 11);
          break;
        case 'Plastik':
          points = Math.floor(weight * 10);
          break;
        case 'Styrofoam':
          points = Math.floor(weight * 9);
          break;
        default:
          points = 0;
      }
  
      const dbRef = admin.database().ref('users').child(userId);
      await dbRef.update({weight, points});
  
      res.status(200).json({points});
    } catch (error) {
      console.error(error);
      res.status(500).json({error: 'Internal server error'});
    }
  };

// GET user data from database
const getUser = async (req, res) => {
    try {

        const userId = req.user.uid;
        const dbRef = admin.database().ref('users').child(userId);
  
        const snapshot = await dbRef.once('value');
        const userData = snapshot.val();
  
        res.status(200).json(userData);
    } catch (error) {
        console.error(error);
        res.status(500).json({error: 'Internal server error'});
    }
  };

// GET user point from database
const getPoint = async (req, res) => {
    try{

        const userId = req.user.uid;
        const dbRef = admin.database().ref('users').child(userId).child('points');
    
        const snapshot = await dbRef.once('value');
        const pointsSnapshot = snapshot.val();
    
        let totalPoints = 0;
        if (pointsSnapshot) {
          Object.values(pointsSnapshot).forEach((record) => {
            totalPoints += record.points;
          });
        }
    
        res.status(200).json({points: totalPoints});
    
    } catch (error) {
        console.error(error);
        res.status(500).json({error: 'Internal server error'});
    }
};

module.exports = {uploadImage, calculatePoint, getUser, getPoint};
