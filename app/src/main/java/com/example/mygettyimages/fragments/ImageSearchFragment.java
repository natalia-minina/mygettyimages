package com.example.mygettyimages.fragments;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;

import com.example.mygettyimages.Constants;
import com.example.mygettyimages.R;
import com.example.mygettyimages.adapters.ImagesAdapter;
import com.example.mygettyimages.interfaces.SearchInterface;
import com.example.mygettyimages.models.GettyImage;
import com.example.mygettyimages.utils.AppItemDecoration;
import com.example.mygettyimages.utils.Utils;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class ImageSearchFragment extends BaseMainScreenFragment {

    private ImagesAdapter imagesAdapter = null;
    private SearchInterface searchInterface = null;
    private RecyclerView searchResultListView;
    private StaggeredGridLayoutManager layoutManager = null;
    private OrderedRealmCollectionChangeListener<RealmResults<GettyImage>> callback =
            new OrderedRealmCollectionChangeListener<RealmResults<GettyImage>>() {
                @Override
                public void onChange(RealmResults<GettyImage> results, OrderedCollectionChangeSet changeSet) {
                    if (isAdded() && imagesAdapter != null && layoutManager != null) {
                        if (changeSet != null) {
                            layoutManager.scrollToPosition(0);
                        }
                    } else {
                        imagesAdapter = null;
                    }
                }
            };

    @Override
    protected int getViewId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void setViews() {
        searchResultListView = getView().findViewById(R.id.searchResultListView);

        if (searchResultListView != null) {

            int columnCount = 2;
            if (Utils.isTablet(getBaseActivity())) {
                columnCount = 3;
            }
            int columnSpace = getResources().getDimensionPixelSize(R.dimen.small_offset);
            layoutManager =
                    new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
            searchResultListView.addItemDecoration(new AppItemDecoration(columnSpace));

            searchResultListView.setLayoutManager(layoutManager);
            if (imagesAdapter == null) {
                int columnWidth = (getBaseActivity().getScreenWidth() - (columnCount + 1) * columnSpace) / columnCount;

                RealmResults<GettyImage> results = getBaseActivity().getAppDBManager().getAllImagesInBackgroundThread();
                imagesAdapter = new ImagesAdapter(getBaseActivity(), columnWidth, results);
                searchInterface = imagesAdapter;
                if (results != null) {
                    results.addChangeListener(callback);
                }
            }
            searchResultListView.setAdapter(imagesAdapter);
        }
    }

    @Override
    public String getMainScreenFragmentTag() {
        return Constants.MainScreenFragmentsTags.NO_TAG;
    }

    @Override
    public int getActionBarType() {
        return Constants.UI.ActionBarType.SEARCH_ACTION_BAR;
    }

    @Override
    public String searchHint() {
        return getStringIfAddedOrNull(R.string.search_getty_images);
    }

    @Override
    public String searchQuery() {
        if (!isAdded() || searchInterface == null) {
            return null;
        }
        return searchInterface.getLastQuery();
    }

    @Override
    public synchronized void onSearchQueryTextSubmitFromActionBar(String query) {
        if (!isAdded() || searchInterface == null) {
            return;
        }
        if (TextUtils.isEmpty(query) || query.length() < Constants.Data.MIN_SEARCH_QUERY_LENGTH) {
            getBaseActivity().showMessage(R.string.search_query_is_too_short);
            return;
        }
        searchInterface.query(query);
    }

}
