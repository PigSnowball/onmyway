package com.porknbunny.onmyway;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";
    private EditText mSearchEditTextView;
    private LocationManager mLocationManager;
    private Location mLocation;
    private Criteria mCriteria;
    private ListView mSearchListView;
    private BaseAdapter mPlacesAdapter;
    private ArrayList<Place> mPlacesList;
    
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
                    searchButton();
                    return true;
                }
                return false;
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

    public void addPlaces(ArrayList<Place> newPlaces){
        for(Place newPlace : newPlaces){
            if(!isDuplicate(newPlace)){
                mPlacesList.add(newPlace);
            }
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
                int resourceList[] = {R.id.place_name
                };
                for (int res : resourceList) {
                    convertView.setTag(res, convertView.findViewById(res));
                }
            }

            Place place = mPlacesList.get(position);

            if (place != null) {
                ((TextView) convertView.getTag(R.id.place_name)).setText(place.getmName());
            }
            return convertView;
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
             super();
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
        }
    }

}
