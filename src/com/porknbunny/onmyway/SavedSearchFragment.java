package com.porknbunny.onmyway;

import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 17/02/2012
 * Time: 22:49
 * To change this template use File | Settings | File Templates.
 */
public class SavedSearchFragment extends Fragment{
    private static final String TAG = "SavedSearchFragment";

     
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.saved_searches_fragment, container, false);

        return view;
    }
}
