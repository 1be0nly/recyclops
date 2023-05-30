const {Storage} = require('@google-cloud/storage');
const axios = require('axios');
const {auth} = require('./firebase');

const cloudStorage = new Storage({
  projectId: 'recyclops-prototype',
  keyFilename: 'main_api/service-image-api.json',
});

const bucketStorage = 'image-test-api';

const uploadImage = async (req, res) => {
  try {
    const token = req.headers.authorization;
    const uid = await auth(token);

    if (!req.file) {
      return res.status(400).json({error: 'Image tidak ditemukan'});
    }

    const file = req.file;
    const {originalname, buffer} = file;

    const fileName = `${uid}/${originalname}`;
    const blob = cloudStorage.bucket(bucketStorage).file(fileName);

    const blobStream = blob.createWriteStream({
      metadata: {
        contentType: file.mimetype,
      },
    });

    blobStream.on('error', (error) => {
      console.error(error);
      return res.status(500).json({error: 'Gagal upload image'});
    });

    blobStream.on('finish', () => {
      const pubUrl = `https://storage.googleapis.com/${bucketStorage}/${blob.name}`;

      // Trigger request to Flask API for classification
      axios.post('################', {imageUrl: pubUrl})
        .then((response) => {
          const classifiedImage = {
            imageUrl: pubUrl,
            classification: response.data.wasteType,
          };

          res.json(classifiedImage);
        })
        .catch((error) => {
          console.error(error);
          return res.status(500).json({error: 'Gagal klasifikasi image'});
        });
    });

    blobStream.end(buffer);
  } catch (error) {
    console.error(error);
    return res.status(401).json({error: 'Unauthorized'});
  }
};

const getImage = async (req, res) => {
  try {
    const token = req.headers.authorization;
    const uid = await auth(token);

    const {imageId} = req.params;

    const file = cloudStorage.bucket(bucketStorage).file(imageId);

    file.getSignedUrl({
      action: 'read',
      expires: Date.now() + 15 * 60 * 1000,
    }, (err, signedUrl) => {
      if (err) {
        console.error(err);
        return res.status(500).json({error: 'Gagal mengambil image'});
      }

      const classifiedImage = {
        imageUrl: signedUrl,
        classification: req.query.classification,
      };

      res.json(classifiedImage);
    });
  } catch (error) {
    console.error(error);
    return res.status(401).json({error: 'Unauthorized'});
  }
};

module.exports = {uploadImage, getImage};
