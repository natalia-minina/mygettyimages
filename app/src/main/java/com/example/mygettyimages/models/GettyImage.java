package com.example.mygettyimages.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GettyImage extends RealmObject {

    public static final String DATE_FIELD_NAME = "searchDate";

    private String url;

    private String name;

    private int maxDimensionsHeight;

    private int maxDimensionsWidth;

    private String id;

    @PrimaryKey
    private long searchDate;

    public GettyImage(String name, String url, String id) {
        this.name = name;
        this.url = url;
        this.id = id;
    }

    public GettyImage() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxDimensionsHeight() {
        return maxDimensionsHeight;
    }

    public void setMaxDimensionsHeight(int maxDimensionsHeight) {
        this.maxDimensionsHeight = maxDimensionsHeight;
    }

    public int getMaxDimensionsWidth() {
        return maxDimensionsWidth;
    }

    public void setMaxDimensionsWidth(int maxDimensionsWidth) {
        this.maxDimensionsWidth = maxDimensionsWidth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(long searchDate) {
        this.searchDate = searchDate;
    }
}
