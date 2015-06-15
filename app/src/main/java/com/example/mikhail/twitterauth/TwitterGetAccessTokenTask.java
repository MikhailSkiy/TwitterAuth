package com.example.mikhail.twitterauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by Mikhail on 15.06.2015.
 */
class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

    @Override
    protected void onPostExecute(String userName) {
        String status = MainActivity.getStatus();
        new TwitterUpdateStatusTask().execute(status);
    }

    @Override
    protected String doInBackground(String... params) {

        Context context = MainActivity.getContextOfApplication();

        Twitter twitter = TwitterUtil.getInstance().getTwitter();
        RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
        if ((params[0]!=null)) {
            try {

                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
                editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
                editor.commit();

                return twitter.showUser(accessToken.getUserId()).getName();
            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
            String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
            AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
            try {
                TwitterUtil.getInstance().setTwitterFactory(accessToken);
                return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}