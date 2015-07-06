package com.example.ettie.networkingdemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ImageView imageView;
    private static final String IMAGE_URL = "http://www.google.com/images/srpr/logo11w.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView1);
    }

    public void startDownload(View view) {
        if (isNetworkAvailable()) {
            new DownloadTask().execute(IMAGE_URL);
        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            available = true;
        }
        return available;
    }

    private Bitmap downloadImage(String urlStr) throws IOException {
        Bitmap bitmap = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.d("NetworkingDemo", e.toString());
        } finally {
            inputStream.close();
        }
        return bitmap;
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {

        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... params) {
            try{
                bitmap = downloadImage(params[0]);
            } catch (Exception e) {
                Log.d("NetworkingDemo", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
