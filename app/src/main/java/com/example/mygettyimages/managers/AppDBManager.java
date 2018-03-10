package com.example.mygettyimages.managers;

import android.util.Log;

import com.example.mygettyimages.Constants;
import com.example.mygettyimages.R;
import com.example.mygettyimages.activities.BaseActivity;
import com.example.mygettyimages.models.GettyImage;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AppDBManager {
    private BaseActivity ctx;
    private Realm realm;
    private boolean isOpened;

    public AppDBManager(BaseActivity ctx) {
        this.ctx = ctx;
        realm = Realm.getDefaultInstance();
        isOpened = true;
    }

    public RealmResults<GettyImage> getAllImagesInBackgroundThread() {
        if (!isOpened) {
            return null;
        }
        RealmResults<GettyImage> results = realm.where(GettyImage.class).
                sort(GettyImage.DATE_FIELD_NAME, Sort.DESCENDING).findAllAsync();
        return results;
    }

    public void addGettyImageInBackgroundThread(final GettyImage gettyImage) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealm(gettyImage);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(Constants.Debug.ERROR_TAG, "AppDBManager addGettyImageFromBackgroundThread", error);
                ctx.showMessage(R.string.has_error_occurred);
            }
        });
    }

    public void close() {
        isOpened = false;
        realm.close();
    }
}
