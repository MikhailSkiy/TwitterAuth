package com.example.mikhail.twitterauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import twitter4j.auth.RequestToken;

public class MainActivity extends Activity {

    private Button buttonLogin;
    private Button buttonSentTwit;
    private static EditText text;
    private boolean isUseStoredTokenKey = false;
    private boolean isUseWebViewForAuthentication = false;
    public static Context contextOfApplication;
    public static String status;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextOfApplication = getApplicationContext();

        buttonSentTwit = (Button)findViewById(R.id.buttonUpdateStatus);
        buttonSentTwit.setOnClickListener(buttonSentTwitListener);

        text = (EditText)findViewById(R.id.editTextStatus);


    }

    public static String getStatus(){
        String st = text.getText().toString();
        return st;
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    private View.OnClickListener buttonSentTwitListener = new View.OnClickListener(){
        @Override
        public void onClick (View v){
            logIn();
        }
    };


    @Override
    public void onResume(){
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(ConstantValues.TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(ConstantValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            new TwitterGetAccessTokenTask().execute(verifier);
        }
//        else {
//            new TwitterGetAccessTokenTask().execute("");
//        }

    }

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            new TwitterAuthenticateTask().execute();
        }
        else
        {
            String status = MainActivity.getStatus();
            new TwitterUpdateStatusTask().execute(status);
//            Intent intent = new Intent(this, TwitterActivity.class);
//            startActivity(intent);
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                startActivity(intent);
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }




    }


}
