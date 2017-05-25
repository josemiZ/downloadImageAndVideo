package com.cerezaconsulting.downloadmultipleimagedemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private String video = "https://youtu.be/NNOLsSxNvAQ";
    private String uri_1="https://s-media-cache-ak0.pinimg.com/736x/a6/86/08/a68608c271149e2eb03116911e7b5493.jpg";
    private String uri_2="https://images-na.ssl-images-amazon.com/images/M/MV5BMjE5Mzg0NzU3OF5BMl5BanBnXkFtZTgwNDg1ODg2MTI@._V1_UX182_CR0,0,182,268_AL_.jpg";
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageViewID);
        list.add(uri_1);
        list.add(uri_2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Picasso.with(getApplicationContext()).load(uri_1).into(mImageView);

        for (int i=0; i<list.size(); i++) {
            Picasso.with(getApplicationContext()).load(list.get(i)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File folder = new File(sd, "/Picasso/");
                    if (!folder.exists()) {
                        if (!folder.mkdir()) {
                            Log.e("ERROR", "Cannot create a directory!");
                        } else {
                            folder.mkdir();
                        }
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateAndTime = sdf.format(new Date());

                    File fileName = new File(folder,currentDateAndTime+".jpg");

                    try {
                        FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        ProgressBack PB = new ProgressBack();
        PB.execute("");



    }

    /*private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                    File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File folder = new File(sd, "/Picasso/");
                    if (!folder.exists()) {
                        if (!folder.mkdir()) {
                            Log.e("ERROR", "Cannot create a directory!");
                        } else {
                            folder.mkdir();
                        }
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateAndTime = sdf.format(new Date());

                    File fileName = new File(folder,currentDateAndTime+".jpg");

                            try {
                                FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }





        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };*/

    private class ProgressBack extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile(video,"Sample.mp4");
            return "";
        }
        protected void onPostExecute(Boolean result) {


        }

    }



    private void downloadFile(String fileURL, String fileName) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }
    }

}
