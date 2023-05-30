const admin = require('firebase-admin');

const serviceAccount = require('main_api/google-services.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const auth = async (token) => {
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    return decodedToken.uid;
  } catch (error) {
    throw new Error('Unauthorized');
  }
};

module.exports = {auth};
