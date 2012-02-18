package com.porknbunny.onmyway;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.porknbunny.onmyway.store.CredentialStore;
import com.porknbunny.onmyway.store.SharedPreferencesCredentialStore;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.latitude.Latitude;
import com.google.api.services.latitude.model.LatitudeCurrentlocationResourceJson;
import com.google.api.services.latitude.model.Location;
import com.google.api.services.latitude.model.LocationFeed;

public class LatitudeApiSample extends FragmentActivity {

    private static final String TAG = "LatSample";
    private SharedPreferences prefs;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ApiCall apiCall = new ApiCall();
        apiCall.execute();
    }

    /**
     * Performs an authorized API call.
     */

    private class ApiCall extends AsyncTask<Void, Void, Boolean> {
        ArrayList<android.location.Location> locationList;
        
        private Boolean performApiCall() {
            try {
                JsonFactory jsonFactory = new JacksonFactory();
                HttpTransport transport = new NetHttpTransport();

                CredentialStore credentialStore = new SharedPreferencesCredentialStore(prefs);
                AccessTokenResponse accessTokenResponse = credentialStore.read();

                GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(accessTokenResponse.accessToken,
                        transport,
                        jsonFactory,
                        OAuth2ClientCredentials.CLIENT_ID,
                        OAuth2ClientCredentials.CLIENT_SECRET,
                        accessTokenResponse.refreshToken);

                final Latitude latitude = new Latitude(transport, accessProtectedResource, jsonFactory);
                latitude.apiKey = OAuth2ClientCredentials.API_KEY;

                locationList = new ArrayList<android.location.Location>();

                LocationFeed locationFeed = latitude.location.list().execute();
                for (Location location : locationFeed.items) {
                    Log.d(TAG, location.toString());
                    double lat = Double.parseDouble(location.latitude.toString());
                    double longitude = Double.parseDouble(location.longitude.toString());
                    long secondsSinceEpoch = Long.parseLong(location.timestampMs.toString());
                    android.location.Location androidLocation = new android.location.Location("Latitude");
                    androidLocation.setLatitude(lat);
                    androidLocation.setLongitude(longitude);
                    androidLocation.setTime(secondsSinceEpoch);
                    Log.d(TAG, androidLocation.toString());

                    locationList.add(androidLocation);
                }

                return true;

            } catch (Exception ex) {
                ex.printStackTrace();
                textView.setText("Error occured : " + ex.getMessage());
            }
            return false;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return performApiCall();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            
            if(success){
                Intent intent = new Intent(LatitudeApiSample.this, SearchActivity.class);
                intent.putParcelableArrayListExtra("locations", locationList);
                startActivity(intent);
            }
            else{
                //auth failed, do it again
                //TODO - this really shouldn't be needing the application context.... i think...
                startActivity(new Intent().setClass(getApplicationContext(), OAuthAccessTokenActivity.class));
            }
            
            
        }
    }
}