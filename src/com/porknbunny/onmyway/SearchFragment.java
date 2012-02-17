package com.porknbunny.onmyway;

import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 17/02/2012
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";
    private EditText mSearchEditTextView;
    private LocationManager mLocationManager;
    private Location mLocation;
    private Criteria mCriteria;
    private ListView mSearchListView;
    private PlaceListAdapter mPlacesListAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*
        The system calls this when creating the fragment. Within your implementation,
        you should initialize essential components of the fragment that you want to
        retain when the fragment is paused or stopped, then resumed.
         */
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        //do data processing here...

        //set up results table
        mPlacesListAdapter = new PlaceListAdapter();
        mSearchListView = (ListView)getActivity().findViewById(R.id.searchResultsList);
        mSearchListView.setAdapter(mPlacesListAdapter);

        //EditTextView
        mSearchEditTextView = (EditText)getActivity().findViewById(R.id.searchText);
        mSearchEditTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    searchButton();
                }
            }
        });


        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mCriteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);

        List<String> providers = mLocationManager.getAllProviders();
        double accuracy = -1;
        if (providers.size() == 0) {
            Log.w(TAG, "No providers available");
        }

        for (String provider : providers) {
            Location tempLoc = mLocationManager.getLastKnownLocation(provider);
            if (tempLoc != null && (accuracy < 0 || tempLoc.getAccuracy() < accuracy)) {
                mLocation = tempLoc;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.search_results_fragment, container, false);

        return view;
    }

    private class PlaceListAdapter extends ArrayAdapter<Place>{
        
        public PlaceListAdapter(){
            super();
        }
        
        private boolean isDuplicate(Place newPlace){
            for(int i = 0; i < getCount(); i++){
                Place existingPlace = getItem(i);
                if(existingPlace.getmReference().equals(newPlace.getmReference())){
                    return true;
                }
            }
            return false;
        }
        
        public void addPlace(ArrayList<Place> newPlaces){
            for(Place newPlace : newPlaces){
                if(!isDuplicate(newPlace)){
                    add(newPlace);
                }
            }
        }
        
    }
    
    public void searchButton(){
        String query = mSearchEditTextView.getText().toString();
        Log.d(TAG, "Do places search: " + mSearchEditTextView.getText());

        PlacesQuery placesQuery = new PlacesQuery(mLocation, query, "AIzaSyAC-8tZVVPKXxAnBMhK3jUBFuRNXtAsOjk");
        AsyncPlacesQuery asyncPlacesQuery = new AsyncPlacesQuery();
        asyncPlacesQuery.execute(placesQuery);
    }

    private class AsyncPlacesQuery extends AsyncTask<PlacesQuery, Void, ArrayList<Place>>{
        public AsyncPlacesQuery(){

        }

        @Override
        protected ArrayList<Place> doInBackground(PlacesQuery... placesQueryList) {
            PlacesQuery placesQuery = placesQueryList[0];
            placesQuery.execute();
            return placesQuery.getResult();
        }

        protected void onPostExecute(ArrayList<Place> placeList) {
            for(Place place : placeList){
                Log.d(TAG, "JSON RESULTS: " + place);
            }
        }


    }

}
