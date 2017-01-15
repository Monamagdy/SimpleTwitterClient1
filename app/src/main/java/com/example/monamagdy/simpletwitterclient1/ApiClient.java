package com.example.monamagdy.simpletwitterclient1;

/**
 * Created by monamagdy on 1/7/17.
 */

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
        import com.twitter.sdk.android.core.TwitterSession;
import okhttp3.ResponseBody;
import com.twitter.sdk.android.core.models.User;

import com.squareup.picasso.Downloader.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
        import retrofit2.http.Query;


public class ApiClient extends TwitterApiClient {
    public ApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public CustomService getCustomService() {
        return getService(CustomService.class);
    }

}
/*
// example users/show service endpoint
 interface CustomService {
    @GET("/1.1/followers/ids.json")
   //Call<ResponseBody> list(@Query("user_id") long userId,Callback<Response> cb);
    Call <String> list(@Query("user_id") long id, Callback<Response> cb);
}
*/

interface CustomService {
    @GET("/1.1/followers/list.json")
    Call<Followers> show(@Query("user_id") Long userId, @Query("screen_name") String
            var, @Query("skip_status") Boolean var1, @Query("include_user_entities") Boolean var2, @Query("count") Integer var3);
}