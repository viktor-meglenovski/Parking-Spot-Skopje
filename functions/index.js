const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.sendNotification = functions.https.onRequest((request, response) => {
  // Retrieve necessary data from request, such as user ID or notification details
  const { userId, title, body } = request.body;

  // Retrieve device token(s) from Firebase Database using the user ID
  const db = admin.database();
  const deviceTokenRef = db.ref(`users/${userId}/deviceToken`);
  deviceTokenRef.once('value', (snapshot) => {
    const deviceToken = snapshot.val();

    // Craft the notification payload
    const notificationPayload = {
      notification: {
        title: title,
        body: body,
      },
    };

    // Send the notification using the device token(s) via the FCM API
    admin.messaging().sendToDevice(deviceToken, notificationPayload)
      .then((response) => {
        console.log('Notification sent successfully:', response);
        // Return a success response to the client
        response.status(200).send('Notification sent successfully');
      })
      .catch((error) => {
        console.error('Error sending notification:', error);
        // Return an error response to the client
        response.status(500).send('Error sending notification');
      });
  });
});