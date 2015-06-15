package com.example.mikhail.twitterauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * Created by Mikhail on 15.06.2015.
 */
class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

    @Override
    protected void onPostExecute(Boolean result) {
        Context context = MainActivity.getContextOfApplication();
        if (result)
            Toast.makeText(context, "Tweet successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Tweet failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(String... params) {

        Context context = MainActivity.getContextOfApplication();
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
            String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");

            if ((accessTokenString!=null) && accessTokenSecret!=null) {
                AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                twitter4j.Status status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
                return true;
            }

        } catch (TwitterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.

    }
}
