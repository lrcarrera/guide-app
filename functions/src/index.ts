import * as functions from 'firebase-functions';

exports.onPostCreat = functions.firestore
.document('/posts/{postid}')
.onWrite((change, context) => {
  console.log(`New post: ${context.params.postid}`)

  const data = change.after.data();
  if (data) {
      console.log(`New post data: ${data.tourists[0].name}`);

    console.log(`New post data: ${data.endHour}`);
  }
});
