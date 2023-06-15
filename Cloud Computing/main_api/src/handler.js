const {Storage} = require('@google-cloud/storage');
const {v4: uuidv4} = require('uuid');
const axios = require('axios');
const admin = require('firebase-admin');

const storage = new Storage({
    projectId: 'recyclops-prototype',
    keyFilename: 'src/recyclops-prototype.json',
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
        const uniqueId = uuidv4();
        const filename = `${uniqueId}.jpg`;

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
// const calculatePoint = async (req, res) => {
//     try {

//       const userEmail = req.user.email;
//       const wasteType = req.body.wasteType;
//       const weight = req.body.weight
        
//       let points = 0;
  
//       switch (wasteType) {
//         case 'Logam':
//           points = Math.floor(weight * 14);
//           break;
//         case 'Kaca':
//           points = Math.floor(weight * 13);
//           break;
//         case 'Kardus':
//           points = Math.floor(weight * 12);
//           break;
//         case 'Kertas':
//           points = Math.floor(weight * 11);
//           break;
//         case 'Plastik':
//           points = Math.floor(weight * 10);
//           break;
//         case 'Styrofoam':
//           points = Math.floor(weight * 9);
//           break;
//         default:
//           points = 0;
//       }
  
//     const dbRef = admin.database().ref('users').child(filterEmail(userEmail));
//     const snapshot = await dbRef.once('value');
//     const userData = snapshot.val();

//     let updateData = {
//         weight,
//         points,
//         wasteType,
//       };
  
//       if (userData && userData.imageUrl) {
//         updateData.imageUrl = userData.imageUrl;
//       }
  
//       if (userData && userData.confidence) {
//         updateData.confidence = userData.confidence;
//       }

//       await dbRef.update(updateData);
  
//       res.status(200).json({points});
//     } catch (error) {
//       console.error(error);
//       res.status(500).json({error: 'Internal server error point'});
//     }
//   };
const calculatePoint = async (req, res) => {
    try {
        const uniqueId = uuidv4();
        const email = req.user.email;
        const userEmail = filterEmail(email);
        const uid = req.user.uid;
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
    return email.replace('.', '_').replace('@', '_');
  }

module.exports = {uploadImage, calculatePoint, getUser, getPoint};
