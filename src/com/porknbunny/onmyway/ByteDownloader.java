package com.porknbunny.onmyway;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ByteDownloader {
    private static final String TAG = "com.porknbunny.onmyway.ByteDownloader";
    private static final int TEMP_BUFF_SIZE = 131072;

    public ByteDownloader() {

    }

    public BufferedInputStream get() {
        for (int i = 0; i < 1; i++) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(mUrl).openConnection();
                connection.setReadTimeout(5000);
                connection.addRequestProperty("User-agent", "User-Agent=Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13");
                InputStream inputStream = (InputStream) connection.getContent();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, TEMP_BUFF_SIZE);

                    return bufferedInputStream;
                }
                inputStream.close();
                Log.w(TAG, "Incorrect response for " + mUrl + " : " + connection.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
                Log.w(TAG, "Could not get " + mUrl);
            }
        }
        return null;
    }

    public ByteDownloader(String url) {
        setUrl(url);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    private String mUrl;
}
