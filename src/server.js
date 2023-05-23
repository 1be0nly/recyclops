const express = require('express');
const routes = require('./routes');

const app = express();
const port = 3000;

// Parse JSON request bodies
app.use(express.json());

// Mount the routes
app.use('/', routes);

// Start the server
app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
 