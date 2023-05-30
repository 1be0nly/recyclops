const express = require('express');
const multer = require('multer');
const routes = require('./routes');

const app = express();
const port = process.env.PORT || 8080;

const storage = multer.memoryStorage();
const upload = multer({storage});

app.use(express.json());
app.use(upload.single('image'));
app.use('/api', routes);

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
