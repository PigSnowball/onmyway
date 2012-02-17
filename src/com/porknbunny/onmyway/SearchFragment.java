package com.porknbunny.onmyway;

import android.app.Fragment;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        Button searchButton = (Button)getActivity().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButton(view);
            }
        });

        mSearchEditTextView = (EditText)getActivity().findViewById(R.id.searchText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.search_results_fragment, container, false);

        return view;
    }

    public void searchButton(View button){
        Log.d(TAG, "Do places search: " + mSearchEditTextView.getText());
    }

}
