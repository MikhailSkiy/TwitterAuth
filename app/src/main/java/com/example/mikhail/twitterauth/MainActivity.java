package com.example.mikhail.twitterauth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import twitter4j.auth.RequestToken;

public class MainActivity extends Activity {

    private Button buttonLogin;
    private boolean isUseStoredTokenKey = false;
    private boolean isUseWebViewForAuthentication = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initializeComponent();
        if (isUseStoredTokenKey)
            logIn();
    }

    private void initializeComponent() {
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(buttonLoginOnClickListener);
    }

    private View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            logIn();

        }
    };

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            new TwitterAuthenticateTask().execute();
        }
        else
        {
            Intent intent = new Intent(this, TwitterActivity.class);
            startActivity(intent);
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {
                if (!isUseWebViewForAuthentication)
                {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);
                }
//                else
//                {
//                    Intent intent = new Intent(getApplicationContext(), OAuthActivity.class);
//                    intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL,requestToken.getAuthenticationURL());
//                    startActivity(intent);
//                }
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }
}
