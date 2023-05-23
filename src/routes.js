const express = require('express');
const multer = require('multer');
const { uploadImage } = require('./handler');

// Configure multer for file upload
const upload = multer();

const router = express.Router();

// POST /upload route to handle image upload
router.post('/upload', upload.single('image'), uploadImage);

module.exports = router;
