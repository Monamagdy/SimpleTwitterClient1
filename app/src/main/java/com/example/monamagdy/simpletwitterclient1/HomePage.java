package com.example.monamagdy.simpletwitterclient1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.os.StrictMode;
import android.widget.ListView;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.widget.ArrayAdapter;
import android.app.ProgressDialog;
import android.net.NetworkInfo;
import android.content.Context;
import com.twitter.sdk.android.Twitter;

import android.os.Environment;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import java.util.List;
import java.io.File;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;
import java.net.MalformedURLException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by monamagdy on 1/9/17.
 */
public class HomePage extends Activity {
    ProgressDialog dialog1;
    int g=0;
    FileOutputStream out;
    boolean Network_state = true;
    ArrayList<Bitmap> pics_list = new ArrayList<Bitmap>();
    ArrayList<String> bio_list = new ArrayList<String>();
    ArrayList<String> name_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        final ListView listview = (ListView) findViewById(R.id.list1);
        String []options = {"Followers"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, options);
        listview.setAdapter(adapter);
        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(MainActivity.TWITTER_KEY,MainActivity.TWITTER_SECRET);
            Fabric.with(this.getApplicationContext(), new Twitter(authConfig));
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //dialog1 = ProgressDialog.show(getApplicationContext(), "", "Saving...");
                Network_state= isNetworkAvailable();
                if(Network_state)
                { dialog1 = new ProgressDialog(HomePage.this); // this = YourActivity
                    dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog1.setMessage("Loading followers..");
                    dialog1.setIndeterminate(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(true);
                    dialog1.show();
                    onViewCreated();
                }
                else
                {
                    try{
                        pics_list.clear();
                        name_list=    read("/data/data/com.example.monamagdy.simpletwitterclient1/cache/name_list.txt");
                        bio_list=     read("/data/data/com.example.monamagdy.simpletwitterclient1/cache/bio_list.txt");
                         for(int i=0;i<5;i++)
                         readImage("/data/data/com.example.monamagdy.simpletwitterclient1/cache/pics_list.png");
                        Intent intent = new Intent(HomePage.this, Home.class);
                        intent.putExtra("NAME", name_list);
                        intent.putExtra("BIOS", bio_list);
                        intent.putExtra("PICS", pics_list);
                        startActivity(intent);
                    }catch (FileNotFoundException t)
                    {

                    }
                }


            }
        });
    }
    public void onViewCreated()
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
         /*
            if (!Fabric.isInitialized()) {
                TwitterAuthConfig authConfig = new TwitterAuthConfig(MainActivity.TWITTER_KEY,MainActivity.TWITTER_SECRET);
                Fabric.with(this.getApplicationContext(), new Twitter(authConfig));
            }
            */
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            //TwitterAuthToken authToken = session.getAuthToken();

            long userID = Twitter.getSessionManager().getActiveSession().getUserId();

            new ApiClient(session).getCustomService().show(session.getId(), null, true, true, 100).enqueue(new com.twitter.sdk.android.core.Callback<Followers>() {
                User user;

                @Override
                public void success(Result<Followers> result) {
                    user = null;
                    try {
                        out = new FileOutputStream("/data/data/com.example.monamagdy.simpletwitterclient1/cache/pics_list.png");


                    }catch (FileNotFoundException g)
                    {

                    }
                    //TODO dont load activity unless all pictures are parsed
                    //   Log.i(" success", "" + result.data.users.toString());
                    Log.i("Get success", "" + result.data.users.size());
                    for (int i = 0; i < 5; i++) {
                        //  for (int i = 0; i < result.data.users.size(); i++) {
                        user = result.data.users.get(i);
                        name_list.add(user.name);
                        bio_list.add(user.description);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.profileImageUrl).getContent());
                            pics_list.add(bitmap);
                            Log.d("write image", String.valueOf(bitmap));
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //  if (i == result.data.users.size() - 1)
                        {
                            Intent intent = new Intent(HomePage.this, Home.class);
                            if (isExternalStorageWritable()) {
                                writeToFile(name_list, "name_list.txt");
                                writeToFile(bio_list, "bio_list.txt");
                            }
                            intent.putExtra("NAME", name_list);
                            intent.putExtra("PICS", pics_list);
                            intent.putExtra("BIOS", bio_list);
                            startActivity(intent);
                            dialog1.dismiss();
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
    }
    public void writeToFile( List<?>  data1,String filename) {

        File data = new File(this.getCacheDir(), filename);
        Log.d("path", data.getPath());
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(data));
            objectOutputStream.writeObject(data1);
            objectOutputStream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    public ArrayList<String> read(String path) throws FileNotFoundException {
        ArrayList<String>  list = null;
        File data = new File(path);
        try {
            if(data.exists()) {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(data));
                list = (ArrayList<String> ) objectInputStream.readObject();
                objectInputStream.close();
                Log.d("files", "read");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void readImage(String path) throws FileNotFoundException {
        Bitmap bitmap=null;
        File f= new File(path);
        FileInputStream gigi = new FileInputStream(f);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            BufferedInputStream buffer=new BufferedInputStream(gigi);
            BitmapFactory.decodeStream(buffer, null, options);
            bitmap = BitmapFactory.decodeStream(buffer,null,options);
            Log.d("bi",String.valueOf(bitmap));
           // buffer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(bitmap!=null) {
            pics_list.add(bitmap);
        }

    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("Writing state",state);
            return true;
        }
        return false;
    }
}