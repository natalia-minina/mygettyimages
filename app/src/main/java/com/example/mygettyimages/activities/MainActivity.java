package com.example.mygettyimages.activities;

import android.os.Bundle;

import com.example.mygettyimages.fragments.ImageSearchFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageSearchFragment fragment = new ImageSearchFragment();
        openMainScreenFragment(fragment, false);
    }
}
