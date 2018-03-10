package com.example.mygettyimages.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mygettyimages.activities.BaseActivity;

public abstract class BaseFragment extends Fragment {

    private BaseActivity ctx;

    public BaseActivity getBaseActivity() {
        return ctx;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ctx = (BaseActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must extend BaseActivity");
        }
    }

    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(isRetainInstance());
        setViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getViewId(), null);
        return view;
    }

    protected abstract int getViewId();

    protected abstract void setViews();

    public String getStringIfAddedOrNull(int id) {
        if (isAdded()) {
            return getString(id);
        }
        return null;
    }

}
