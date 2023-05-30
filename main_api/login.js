const {auth} = require('./firebase');

const login = async (req, res) => {
  try {
    const token = req.body.token;
    const uid = await auth(token);
    
    res.json({success: true, uid});
  } catch (error) {
    console.error(error);
    return res.status(401).json({ error: 'Unauthorized' });
  }
};

module.exports = {login};
