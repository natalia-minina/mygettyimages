package com.example.mygettyimages;


import android.app.Application;

import com.example.mygettyimages.fragments.BaseMainScreenFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static App instance;

    private BaseMainScreenFragment currentBaseMainScreenFragment;

    public App() {
        instance = this;
    }

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        initImageLoader();
        currentBaseMainScreenFragment = null;
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public BaseMainScreenFragment getCurrentBaseMainScreenFragment() {
        if (currentBaseMainScreenFragment != null) {
            if (!currentBaseMainScreenFragment.isAdded()) {
                return null;
            }
        }
        return currentBaseMainScreenFragment;
    }

    public void setCurrentBaseMainScreenFragment(BaseMainScreenFragment currentBaseMainScreenFragment) {
        this.currentBaseMainScreenFragment = currentBaseMainScreenFragment;
    }

    private void initImageLoader() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder = builder
                .considerExifParams(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true);

        DisplayImageOptions options = builder.build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

}
