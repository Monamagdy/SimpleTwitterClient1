package com.example.monamagdy.simpletwitterclient1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.MotionEvent;
import com.twitter.sdk.android.Twitter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.List;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.TextView;

import io.fabric.sdk.android.Fabric;

/**
 * Created by monamagdy on 1/11/17.
 */
public class FollowerInformation extends Activity {
    ImageView backgroundpic;
     FixedTweetTimeline timeline=null;
    List<Tweet>tweets=null;
    ListView k;
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("Current Activity", "FollowerInformation");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followerinfo);
        Bundle bundle = getIntent().getExtras();
        ImageView profilepic = (ImageView)findViewById(R.id.imageView);
        TextView name_user = (TextView)findViewById(R.id.textView);
        backgroundpic = (ImageView)findViewById(R.id.background);
        Bitmap ProfilePicture =  bundle.getParcelable("ProfilePic");
        String name = getIntent().getStringExtra("Name") ;
        name_user.setText(name);
        onViewCreated(name);
        profilepic.setImageBitmap(ProfilePicture);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_SCROLL: k.smoothScrollBy(0, 0); // to stop the scrolling.
                break;
        }
        return super.onTouchEvent(ev); }
    public void onViewCreated(final String user_required)
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (!Fabric.isInitialized()) {
                TwitterAuthConfig authConfig = new TwitterAuthConfig(MainActivity.TWITTER_KEY,MainActivity.TWITTER_SECRET);
                Fabric.with(this.getApplicationContext(), new Twitter(authConfig));
            }
           final TwitterSession session = Twitter.getSessionManager().getActiveSession();
           long userID = Twitter.getSessionManager().getActiveSession().getUserId();
           TwitterApiClient aa = new TwitterApiClient(session);
            User s ;
           // int limit=  u.followersCount;
            new ApiClient(session).getCustomService().show(session.getId(), null, true, true,100).enqueue(new com.twitter.sdk.android.core.Callback<Followers>() {
                User user;

                @Override
                public void success(Result<Followers> result) {
                    user = null;
                    //TODO dont load activity unless all pictures are parsed
                    for (int i = 0; i < result.data.users.size(); i++) {
                        user = result.data.users.get(i);
                        if (user.name.contains(user_required)) {
                            try {
                                String ko = user.profileBannerUrl;
                                if (ko == null)
                                    backgroundpic.setBackgroundResource(R.drawable.default_background);
                                else {
                                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.profileBannerUrl).getContent());
                                    backgroundpic.setImageBitmap(bitmap);
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final UserTimeline userTimeline = new UserTimeline.Builder().screenName(user.screenName).maxItemsPerRequest(10).build();
                            k = (ListView) findViewById(R.id.listView3);
                            final Callback<TimelineResult<Tweet>> callback = new Callback<TimelineResult<Tweet>>() {
                                @Override
                                public void success(Result<TimelineResult<Tweet>> result) {
                                    Log.d("tweets size", String.valueOf(result.data.items.size()));
                                    if (result.data.items.size() == 0) {
                                        tweets = null;
                                        Log.d("tweets size", String.valueOf(result.data.items.size()));
                                    } else if (result.data.items.size() > 10)
                                        tweets = result.data.items.subList(0, 9);
                                    else
                                        tweets = result.data.items.subList(0, result.data.items.size() - 1);
                                    timeline = new FixedTweetTimeline.Builder().setTweets(tweets).build();
                                    TweetTimelineListAdapter adapter1 = new TweetTimelineListAdapter.Builder(FollowerInformation.this).setTimeline(timeline).build();
                                    k.setAdapter(adapter1);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    Log.d("TAG", "Failure");
                                }
                            };
                            userTimeline.next(null, callback);
                        }
                    }
                }

                @Override
                public void failure(TwitterException e) {
                    Log.i("Get fail", "" + e.getMessage());
                }
            });
        }
    }
}
