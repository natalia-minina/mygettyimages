package com.example.mygettyimages.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygettyimages.App;
import com.example.mygettyimages.Constants;
import com.example.mygettyimages.R;
import com.example.mygettyimages.fragments.BaseMainScreenFragment;
import com.example.mygettyimages.managers.AppDBManager;
import com.example.mygettyimages.managers.AppNetworkManager;

public class BaseActivity extends AppCompatActivity {

    private App app;

    private AppNetworkManager appNetworkManager;

    private AppDBManager appDBManager;

    private SearchView searchView = null;

    public AppNetworkManager getAppNetworkManager() {
        return appNetworkManager;
    }

    public AppDBManager getAppDBManager() {
        return appDBManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        app = (App) getApplication();
        appNetworkManager = new AppNetworkManager(this);
        appDBManager = new AppDBManager(this);

        setActionBar();
    }

    public void openMainScreenFragment(BaseMainScreenFragment fragment, boolean isAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fTrans = fm.beginTransaction();
        fTrans.replace(R.id.container, fragment, null);
        if (isAddToBackStack) {
            fTrans.addToBackStack(fragment.getMainScreenFragmentTag());
        }
        fTrans.commit();
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        updateActionBar();
    }

    public void updateActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        int actionBarType = Constants.UI.ActionBarType.EMPTY_ACTION_BAR;
        BaseMainScreenFragment f = getApp().getCurrentBaseMainScreenFragment();
        if (f != null) {
            actionBarType = f.getActionBarType();
        }
        if (actionBarType == Constants.UI.ActionBarType.SEARCH_ACTION_BAR) {
            actionBar.setHomeAsUpIndicator(R.drawable.arrowleft);
            actionBar.setDisplayHomeAsUpEnabled(true);

            searchView = new SearchView(this);
            searchView.setMaxWidth(Integer.MAX_VALUE);
            try {
                int id = android.support.v7.appcompat.R.id.search_src_text;//getResources().getIdentifier("android:id/search_src_text", null, null);
                TextView textView = (TextView) searchView.findViewById(id);
                textView.setHintTextColor(ContextCompat.getColor(this, R.color.light_light_grey_color));
                textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.Data.MAX_SEARCH_QUERY_LENGTH)});
            } catch (Exception e) {
                Log.e(Constants.Debug.ERROR_TAG, "updateActionBar searchView setHintTextColor", e);
            }
            searchView.setBackgroundResource(R.drawable.white_bg_with_rounded_corners_without_left_padding);
            searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);

            String queryHint = null;
            String query = null;
            if (f != null) {
                queryHint = f.searchHint();
                query = f.searchQuery();
            }
            searchView.setQueryHint(queryHint);
            searchView.setQuery(query, false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    onSearchQueryTextSubmitFromActionBar(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
            actionBar.setCustomView(searchView);
            searchView.requestFocus();
            actionBar.setDisplayShowCustomEnabled(true);

        } else {
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public void clearFocusInSearchViewFromActionBar() {
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onOptionsItemSelected(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onOptionsItemSelected(int menuItemId) {
        switch (menuItemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        return screenWidth;
    }

    public int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.heightPixels;
        return screenWidth;
    }

    public synchronized void onSearchQueryTextSubmitFromActionBar(String query) {
        BaseMainScreenFragment f = getApp().getCurrentBaseMainScreenFragment();
        if (f == null) {
            return;
        }
        f.onSearchQueryTextSubmitFromActionBar(query);
    }

    @Override
    public void onDestroy() {
        appDBManager.close();
        super.onDestroy();
    }

    public App getApp() {
        return app;
    }

    public void showMessage(int textId) {
        showMessage(getString(textId));
    }

    public void showMessage(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

}
