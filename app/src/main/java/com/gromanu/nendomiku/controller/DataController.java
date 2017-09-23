package com.gromanu.nendomiku.controller;

import android.util.Log;

import com.google.gson.Gson;
import com.gromanu.nendomiku.model.MikuItem;
import com.gromanu.nendomiku.model.MikuList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataController {

    public interface DataControllerCallback {
        void onDataReceived(List<MikuItem> itemsList);

        void onDataError(Exception e);
    }

    private static final String DATA_PATH = "/miku/mikuList.json";

    private final String domainName;
    private final OkHttpClient client;

    public DataController(String domainName) {
        this.domainName = domainName;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.build();
    }

    public void fetchData(final DataControllerCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<MikuItem> dataList = getData(domainName+DATA_PATH);
                    callback.onDataReceived(dataList);
                } catch (Exception e){
                    Log.e(DataController.class.getSimpleName(),
                            "Error while fetching data",
                            e);
                    callback.onDataError(e);
                }
            }
        }).start();
    }

    private List<MikuItem> getData(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();

        if (response.code() == HttpURLConnection.HTTP_OK
                || response.code() == HttpURLConnection.HTTP_ACCEPTED) {

            String json = response.body().string();

            List<MikuItem> mikuList = new Gson().fromJson(json, MikuList.class);

            return mikuList;
        } else {
            throw new IOException("ERROR " + response.code() + ": "+response.body().string());
        }
    }
}
