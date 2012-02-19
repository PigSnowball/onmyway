package com.porknbunny.onmyway;

import android.location.Location;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 18/02/2012
 * Time: 07:19
 * To change this template use File | Settings | File Templates.
 */
public class PlacesQuery {
    private static final String TAG = "PlacesQuery";
    private Location mLocation;
    private String mQuery;
    private String mApiKey;
    private String mUrl;
    private ArrayList<Place> mPlaceArrayList;

    public PlacesQuery(Location location, String query, String apiKey) {
        //do nothing for now
        mLocation = location;
        mQuery = query;
        mApiKey = apiKey;
    }


    public void execute() {
        ByteDownloader byteDownloader = new ByteDownloader();

        //construct the places api url
        mUrl = "https://maps.googleapis.com/maps/api/place/search/json?location=" +
                (float) mLocation.getLatitude() +
                "," +
                (float) mLocation.getLongitude() +
                "&radius=5000&keyword=" +
                mQuery + "" +
                "&sensor=true&key=" +
                mApiKey;

        Log.d(TAG, "url: " + mUrl);

        byteDownloader.setUrl(mUrl);

        BufferedInputStream inputStream = byteDownloader.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);

        if (inputStream != null) {
            byte[] tempBuff = new byte[16384];
            int readCount;
            try {
                while ((readCount = inputStream.read(tempBuff)) != -1) {
                    byteArrayOutputStream.write(tempBuff, 0, readCount);
                }
                byte[] newBuff = byteArrayOutputStream.toByteArray();
                inputStream.close();
                byteArrayOutputStream.close();
                String temp = new String(newBuff, "US_ASCII");
                Log.d(TAG, temp);


                //jsonparse results
                mPlaceArrayList = new ArrayList<Place>();

                try {
                    JSONObject jsonObject = new JSONObject(temp);
                    JSONArray jsonPlacesArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonPlacesArray.length(); i++) {
                        JSONObject jsonPlace = jsonPlacesArray.getJSONObject(i);
                        float latitude, longitude;
                        String icon, reference, name, vicinity;

                        JSONObject geometry = jsonPlace.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        latitude = (float) location.getDouble("lat");
                        longitude = (float) location.getDouble("lng");

                        icon = jsonPlace.getString("icon");
                        reference = jsonPlace.getString("id"); //fudge it. i need an id that's useful accross searches, no idea why google makes this different to reference
                        name = jsonPlace.getString("name");
                        vicinity = jsonPlace.getString("vicinity");

                        Place newPlace = new Place(latitude, longitude, icon, reference, name, vicinity);
                        mPlaceArrayList.add(newPlace);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } catch (IOException ioException) {
                Log.w(TAG, "Network connection went wrong");
            }
        }

    }

    public ArrayList<Place> getResult() {
        return mPlaceArrayList;
    }
}
