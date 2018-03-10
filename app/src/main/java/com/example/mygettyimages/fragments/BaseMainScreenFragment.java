package com.example.mygettyimages.fragments;

public abstract class BaseMainScreenFragment extends BaseFragment {

    public abstract int getActionBarType();

    public abstract String getMainScreenFragmentTag();

    public String searchHint() {
        return null;
    }

    public String searchQuery() {
        return null;
    }

    public void onSearchQueryTextSubmitFromActionBar(String query) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getBaseActivity().getApp().setCurrentBaseMainScreenFragment(this);
        getBaseActivity().updateActionBar();
    }
}
