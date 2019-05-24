package com.example.pathfinderapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, View.OnClickListener {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String DUMMY_PASSWORD = "ThEpASs";
    private static final String NO_PSSWRD = "no_password";
    private static final String NO_EMAIL = "no_email";
    private static final int QUANTITY_FLAGS_FIREBASE_STORAGE = 5;
    int indexOfImage;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String NAME = "name";
    // private static final String URL ="https://graph.facebook.com/";
    private static final String PICTURE_REFERENCE = "/picture?type=large";

    private static final String PACKAGE_NAME = "com.example.pathfinderapp";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    //private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences prefs;
    private ProgressBar rotateLoading;

    //Firebase authentication
    private FirebaseAuth mAuth;
    private String mCustomToken;

    private FirebaseFirestore db;
   /* Button mEmailSignInButton;
    Button emailCreateAccountButton;
    TextView verifyEmailButton;*/
    //private TokenBroadcastReceiver mTokenReceiver;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();


        prefs = this.getSharedPreferences(
                PACKAGE_NAME, MODE_PRIVATE);
        rotateLoading = findViewById(R.id.rotate_loading);
        mLoginFormView = findViewById(R.id.login_form);
        mPasswordView = findViewById(R.id.password);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        /*mEmailSignInButton = (Button)*/
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        /*emailCreateAccountButton = (Button)*/
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        /*verifyEmailButton = (TextView) */
        findViewById(R.id.email_password_validation).setOnClickListener(this);


        //Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Add login

        if (currentUser != null) {

            // if(aux){
            //intent = new Intent(this, LangugesSelectionActivity.class);
            //} else {
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            //}
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void createAccount(String email, String password) {
        Log.d("CREATEACCOUNT", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgress(true);

        // [START create_user   _with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this,
                                    "Account creation success",
                                    Toast.LENGTH_SHORT).show();

                            sendEmailVerification();

                            /*FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CREATEACCOUNT", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        showProgress(false);
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private void signIn(String email, String password) {
        Log.d("SIGNIN", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgress(true);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNIN", "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this,
                                    "Authentication success",
                                    Toast.LENGTH_SHORT).show();

                            String userUid = mAuth.getCurrentUser().getUid();

                            String token = prefs.getString(getResources().getString(R.string.message_token), null);
                            if (token != null) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(userUid).push().setValue(token);
                                User user = DefValues.getUserInContext();
                                user.setMessageToken(token);
                                DefValues.getUserInContextDocument().update("user", user.addToHashMap());
                            }

                            //const usersRef = db.collection('users').whereEqualTo("user.uid", userUid )
                            //create iff

                            db.collection("users").whereEqualTo("user.uid", userUid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                if (task.getResult().size() > 0) {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                   // storeFlagImageFromFirebaseStorage();
                                                    Intent intent = new Intent(LoginActivity.this, LangugesSelectionActivity.class);
                                                    startActivity(intent);

                                                    //Intent intent = new Intent(LoginActivity.this, LangugesSelectionActivity.class);
                                                    //startActivity(intent);
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this,
                                                        "Server not found (Error: 400)",
                                                        Toast.LENGTH_SHORT).show();
                                                Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                            }
                                            finish();
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Server not found (Error: 400)",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        /*if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }*/
                        showProgress(false);
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

   /* private void storeFlagImageFromFirebaseStorage() {

        for (indexOfImage = 0; indexOfImage < QUANTITY_FLAGS_FIREBASE_STORAGE; indexOfImage++) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child("100" + indexOfImage + ".png");

            try {
                final File localFile = File.createTempFile("images_flag", "png");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        DefValues.addFlagFromFirebaseStorage(bitmap);

                        if (indexOfImage == QUANTITY_FLAGS_FIREBASE_STORAGE - 1){

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.w("TEST10", "Error getting flag image ."+ exception.getMessage());

                    }
                });
            } catch (IOException e) {
                //System.out.println("Vergassso");
            }
        }


    }*/

    private void sendEmailVerification() {
        // Disable button
        // findViewById(R.id.email_password_validation).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.email_password_validation).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("VERIFICATIONEMAIL", "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
    //////////////////////////////////

    /*private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Required.");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Required.");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }*/

    ///////////////////////////////////

    ////////////////////////////////////
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());

        } else if (i == R.id.email_password_validation) {
            //sendEmailVerification();
            restorePassword();
        }
    }

    public void restorePassword() {

        findViewById(R.id.email_password_validation).setEnabled(false);
        if (!validateForm()) {
            return;
        }

        showProgress(true);

        FirebaseAuth.getInstance().sendPasswordResetEmail(mEmailView.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "Email sent.");


                            Toast.makeText(LoginActivity.this,
                                    "Please check the instructions sent in your email to restore the password.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "E-mail inserted not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                        findViewById(R.id.email_password_validation).setEnabled(true);

                    }
                });
    }


    ////////////////////////////////////////
    private boolean hasbeenLoggedInBefore() {
        String userEmail = prefs.getString(getResources().getString(R.string.email), NO_EMAIL);
        String password = prefs.getString(getResources().getString(R.string.password), NO_PSSWRD);

        if (userEmail == null || password == null)
            return false;

        return !userEmail.equals(NO_EMAIL) && !password.equals(NO_PSSWRD);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean validateForm() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

        } else {
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        if (show) {
            rotateLoading.setVisibility(View.VISIBLE);
        } else {
            rotateLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

