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

                console.log('FUCKING TOKEN', token);

                const payload = {
                  notification: {
                    title: `New tourist!`,
                    body: `You got a new tourist on your tour in ${data.place.name}`
                  }
                }

                admin.messaging().sendToDevice(token, payload).then(function(resp) {
                    console.log('Successfully sent message:', resp);
                  })
                  .catch(function(error) {
                    console.log('Error sending message:', error);
                  });
              }
          })
        })
        .catch(err => console.log(err));
        // return admin.firestore().collection('users').doc(data.guide.uid).get().then(response => {
        //   console.log('Query completed');
        //   console.log('Data:', response.data());
        //   console.log(JSON.parse(JSON.stringify(response.data())));
        //   console.log('REEEEEEEEEEEEEEEEE');
        // })
        // .catch(err => console.log(err));;
        // console.log(user[0].data().toJSON())

        // const userData = user.data();
        //
        // if (userData) {
        //   console.log('Name: ', userData.name);
        //   console.log('Token: ', userData.token);
        // }

        // admin.firestore().collection('users').doc(data.guide.uid).get().then( response => {
        //   const user = response.data();
        //   if (user) {
        //     const token = user.messageToken;
        //
        //     console.log('Name: ', user.name);
        //     console.log('Token: ', token);
        //   }
        //   //
        //     const payload = {
        //       notification: {
        //         title: `New tourist!`,
        //         body: `You got a new tourist on your tour in ${data.place.name}`
        //       }
        //     }
        //
        //     admin.messaging().sendToDevice(token, payload).then( resp => {
        //       resp.results.forEach((result, index) =>{
        //         const error = result.error;
        //         if (error) {
        //           console.log('Failed: ', error);
        //         }
        //       });
        //     })
        //     .catch(err => console.log(err));;
        //   }
        // })
        // .catch(err => console.log(err));

      } else {
        console.log("Token is null", data.guide.uuid);
      }
  }

  return null;
});
