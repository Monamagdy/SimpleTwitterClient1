package com.example.monamagdy.simpletwitterclient1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "Prefs";
   // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    public static final String TWITTER_KEY = "JobH3X5M6JnMekFqHzE1fEIP1";
    public static final String TWITTER_SECRET = "i6WUK6H8qZEgSDIIMOsKxaVMPoA1DBiHmVnnyAp4aFtsZpqM6N";
     TwitterLoginButton Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET);
            Fabric.with(this.getApplicationContext(), new Twitter(authConfig));
        }
        TwitterSession session = Twitter.getSessionManager().getActiveSession();

        if(session!=null)
        {
            Log.d("null", session.toString());
            Intent intent = new Intent(MainActivity.this,HomePage.class);
            startActivity(intent);
             finish();
        }
        setContentView(R.layout.activity_main);
           Login = (TwitterLoginButton) findViewById(R.id.login);
           final SharedPreferences.Editor editor;
           editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                Login.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        // Do something with result, which provides a TwitterSession for making API calls
                        TwitterSession session = Twitter.getSessionManager()
                                .getActiveSession();
                        TwitterAuthToken authToken = session.getAuthToken();
                        String token = authToken.token;
                        String secret = authToken.secret;

                        //Here we save all the details of user's twitter account in the device settings
                        System.out.println(result.data.getUserName()+ result.data.getUserId());
                        editor.putString("tweetToken", token).commit();
                        editor.putString("tweetSecret", secret).commit();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Login.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this,HomePage.class);
        startActivity(intent);

    }

}
