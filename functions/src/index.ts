import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin'

admin.initializeApp();

exports.onPostCreat = functions.firestore
.document('/posts/{postid}')
.onWrite((change, context) => {
  console.log(`New post: ${context.params.postid}`)

  if (!change.after.exists) {
    console.log("Data is being deleted");
    return null;
  }

  if (!change.before.exists) {
    console.log("Tour just created!");
    return null;
  }

  const data = change.after.data();
  if (data) {
      if (data.guide.uid !== null) {
        console.log('Uid: ', JSON.parse(JSON.stringify(data)));


        admin.firestore().collection('users').get().then(response => {
          response.forEach(function(value) {
              const user = value.data().user;
              if (user && user.uid == data.guide.uid) {
                console.log('Query completed');
                console.log('Data:', user);
                const token = user.messageToken;

                console.log('TOKEN', token);

                const payload = {
                  notification: {
                    title: `New tourist!`,
                    body: `You got a new tourist on your tour in ${data.place.name}`
                  },
                  token: token
                }

                admin.messaging().send(payload)
                  .then( resp => {
                    console.log('Successfully sent message:', resp);
                  })
                  .catch(function(error) {
                    console.log('Error sending message:', error);
                  });

                // admin.messaging().sendToDevice(token, payload).then(function(resp) {
                //     console.log('Successfully sent message:', resp);
                //   })
                //   .catch(function(error) {
                //     console.log('Error sending message:', error);
                //   });
              }
          })
        })
        .catch(err => console.log(err));

      } else {
        console.log("Token is null", data.guide.uuid);
      }
  }
  return null;
});
