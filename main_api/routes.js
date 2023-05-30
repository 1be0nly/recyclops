const express = require('express');
const {uploadImage, getImage} = require('./handler');
const {login} = require('./login');

const router = express.Router();

router.post('/login', login);
router.post('/upload', uploadImage);
router.get('/classified/:imageId', getImage);

module.exports = router;
