const { Storage } = require('@google-cloud/storage');

// Configure Google Cloud Storage
const storage = new Storage({
  projectId: 'recyclops-prototype',
  keyFilename: 'image-test-api/service-image-api.json', // Path to your keyfile.json
});

// Upload image to Google Cloud Storage
const uploadImage = async (req, res) => {
  try {
    if (!req.file) {
      return res.status(400).json({ error: 'No file uploaded' });
    }

    const bucketName = 'image-test-api';
    const bucket = storage.bucket(bucketName);
    const { originalname, buffer } = req.file;

    const blob = bucket.file(originalname);
    const blobStream = blob.createWriteStream();

    blobStream.on('error', (err) => {
      console.error(err);
      res.status(500).json({ error: 'Failed to upload image' });
    });

    blobStream.on('finish', () => {
      res.status(200).json({ message: 'Image uploaded successfully' });
    });

    blobStream.end(buffer);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Internal server error' });
  }
};

module.exports = {
  uploadImage,
};
