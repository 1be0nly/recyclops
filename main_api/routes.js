const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer();
const {verifyToken} = require('./middleware');
const {uploadImage, calculatePoint,getUser, getPoint} = require('./handler');

router.post('/upload', verifyToken, upload.single('image'), uploadImage);
router.post('/postpoint', verifyToken, calculatePoint);
router.post('/history', verifyToken, getUser);
router.post('/getpoint', verifyToken, getPoint);

module.exports = router;
