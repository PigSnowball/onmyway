package com.porknbunny.onmyway;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.SearchView;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 17/02/2012
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.search_activity);
    }

}