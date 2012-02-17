package com.porknbunny.onmyway;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 17/02/2012
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.search_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Configure the search info and add any event listeners

        return super.onCreateOptionsMenu(menu);
    }
}