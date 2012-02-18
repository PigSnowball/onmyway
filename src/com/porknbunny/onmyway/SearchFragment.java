package com.porknbunny.onmyway;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
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
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private EditText mSearchEditTextView;
    private LocationManager mLocationManager;
    private Location mLocation;
    private Criteria mCriteria;
    private ListView mSearchListView;
    private BaseAdapter mPlacesAdapter;
    private ArrayList<Place> mPlacesList;
    private ArrayList<Location> mLocationsList;
    private int searchCount = 0;
    private ProgressBar mProgressBar;
    private int searchValue = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*
        The system calls this when creating the fragment. Within your implementation,
        you should initialize essential components of the fragment that you want to
        retain when the fragment is paused or stopped, then resumed.
         */

        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mCriteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);

        List<String> providers = mLocationManager.getAllProviders();
        long latestTime = 0;
        if (providers.size() == 0) {
            Log.w(TAG, "No providers available");
        }

        for (String provider : providers) {
            Location tempLoc = mLocationManager.getLastKnownLocation(provider);
            if (tempLoc != null && (tempLoc.getTime() > latestTime)) {
                mLocation = tempLoc;
                latestTime = tempLoc.getTime();
            }
        }

        mLocationsList = new ArrayList<Location>();
        if(mLocation != null){
            mLocationsList.add(mLocation);
        }

        //sort latitude data
        ArrayList<Location> tempLocList = getActivity().getIntent().getExtras().getParcelableArrayList("locations");

        if(tempLocList != null && tempLocList.size() >0){
            mLocationsList.addAll(tempLocList);
        }

        if(mLocationsList.size() == 0){
            Log.w(TAG,"User has neither latitude locations or hardware location");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("We couldn't locate you");
            builder.setMessage("Check your GPS settings and if you have Latitude enabled.");
            builder.setNeutralButton("OK", null);
            AlertDialog alert = builder.create();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        //do data processing here...
        mPlacesList = new ArrayList<Place>();

        //set up results table
        mPlacesAdapter = new PlaceListAdapter();
        mSearchListView = (ListView)getActivity().findViewById(R.id.searchResultsList);
        mSearchListView.setAdapter(mPlacesAdapter);

        //EditTextView
        mSearchEditTextView = (EditText)getActivity().findViewById(R.id.searchText);
        mSearchEditTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    mPlacesList.clear();
                    mPlacesAdapter.notifyDataSetChanged();
                    searchButton();
                    return true;
                }
                return false;
            }
        });
        
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.search_results_fragment, container, false);
        return view;
    }
    
    public void addPlace(Place newPlace){
        //check for dupes
        if(isDuplicate(newPlace)){
            return;
        }

        //ok lets, find the nearest lat point
        Location newPlaceLoc = newPlace.getLocation();
        float minDistance = 9999999;
        Location minLocation = null;
        for (Location testLocation : mLocationsList) {
            if (testLocation.distanceTo(newPlaceLoc) < minDistance) {
                minLocation = testLocation;
                minDistance = testLocation.distanceTo(newPlaceLoc);
            }
        }
        
        newPlace.setmNearestLatitude(minLocation);
        newPlace.setmDistanceToLatPoint(minDistance);
        
        //find position to enter in array
        for(Place cmpPlace: mPlacesList){
            if(cmpPlace.getmDistanceToLatPoint() > newPlace.getmDistanceToLatPoint()){
                int insertInFrontOf = mPlacesList.indexOf(cmpPlace);
                mPlacesList.add(insertInFrontOf, newPlace);
                return;
            }
        }
        mPlacesList.add(newPlace);
        
    }

    public void addPlaces(ArrayList<Place> newPlaces){
        for(Place newPlace : newPlaces){
            addPlace(newPlace);
        }
        mPlacesAdapter.notifyDataSetChanged();
    }

    private boolean isDuplicate(Place newPlace){
        for(Place existingPlace : mPlacesList){
            if(existingPlace.getmReference().equals(newPlace.getmReference())){

                return true;
            }
        }
        return false;
    }

    private class PlaceListAdapter extends BaseAdapter{
        LayoutInflater inflateService;

        public PlaceListAdapter(){
            super();
            inflateService = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mPlacesList.size();
        }

        @Override
        public Object getItem(int i) {
            return mPlacesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){
            if (convertView == null) {
                convertView = inflateService.inflate(R.layout.place_list_item, parent, false);
                int resourceList[] = {R.id.place_name,
                        R.id.place_icon,
                        R.id.place_distance_to_lat,
                        R.id.place_distance_to_user,
                        R.id.place_lat,
                        R.id.place_vicinity
                };
                for (int res : resourceList) {
                    convertView.setTag(res, convertView.findViewById(res));
                }
            }

            Place place = mPlacesList.get(position);

            if (place != null) {
                ((TextView) convertView.getTag(R.id.place_name)).setText(place.getmName());
                ((TextView) convertView.getTag(R.id.place_vicinity)).setText(place.getVicinity());
                ((TextView) convertView.getTag(R.id.place_lat)).setText("");
                ((TextView) convertView.getTag(R.id.place_distance_to_user)).setText(""+(int)place.getLocation().distanceTo(mLocation)+"m");
                ((TextView) convertView.getTag(R.id.place_distance_to_lat)).setText(""+(int)place.getmDistanceToLatPoint()+"m");
                //((ImageView) convertView.getTag(R.id.place_icon))
            }
            return convertView;
        }
    }
    
    public void searchButton(){
        String query = mSearchEditTextView.getText().toString();
        Log.d(TAG, "Do places search: " + mSearchEditTextView.getText());

        searchCount = 0;
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        //only keeping latitude data seperated by 500m
        Location prevLocation = null;
        for(Location location : mLocationsList){
            if(prevLocation == null || prevLocation.distanceTo(location) > 1500){
                PlacesQuery placesQuery = new PlacesQuery(location, query, "AIzaSyAC-8tZVVPKXxAnBMhK3jUBFuRNXtAsOjk");
                AsyncPlacesQuery asyncPlacesQuery = new AsyncPlacesQuery();
                asyncPlacesQuery.execute(placesQuery);
            }
            prevLocation = location;
        }
        mProgressBar.setProgress(5);
        searchValue = 95 / searchCount;
        
    }

    private void checkProgress(){
        searchCount--;
        mProgressBar.incrementProgressBy(searchValue);
        if(searchCount <= 0){
            mProgressBar.incrementProgressBy(searchValue);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    
    private class AsyncPlacesQuery extends AsyncTask<PlacesQuery, Void, ArrayList<Place>>{
        public AsyncPlacesQuery(){
             super();
            searchCount++;
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
            addPlaces(placeList);
            checkProgress();
        }
    }

}
