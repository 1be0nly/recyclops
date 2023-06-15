const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer();
const {verifyToken} = require('./middleware');
const {uploadImage, calculatePoint, getUser, getPoint} = require('./handler');
const bodyParser = require('body-parser');

router.post('/upload', verifyToken, upload.single('image'), uploadImage);
router.post('/postpoint', verifyToken, bodyParser.urlencoded(), calculatePoint);
router.get('/history', verifyToken, getUser);
router.get('/getpoint', verifyToken, getPoint);

module.exports = router;
