package com.gromanu.nendomiku.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageController {

    public interface ImageControllerCallback {

        void onImageReceived(Bitmap image);

        void onImageError(Exception e);
    }

    private static final String IMAGE_PATH = "/miku/images/";


    private final String domainName;
    private final OkHttpClient client;

    public ImageController(String domainName) {
        this.domainName = domainName;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.build();
    }

    public void fetchImage(final String filename, final ImageControllerCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap image = getImage(domainName + IMAGE_PATH + filename);
                    callback.onImageReceived(image);
                } catch (Exception e){
                    Log.e(DataController.class.getSimpleName(),
                            "Error while fetching image "+filename,
                            e);
                    callback.onImageError(e);
                }
            }
        }).start();
    }

    private Bitmap getImage(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();

        if (response.code() == HttpURLConnection.HTTP_OK
                || response.code() == HttpURLConnection.HTTP_ACCEPTED) {

            byte[] body = response.body().bytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        } else {
            throw new IOException("ERROR " + response.code() + ": "+response.body().string());
        }
    }
}
