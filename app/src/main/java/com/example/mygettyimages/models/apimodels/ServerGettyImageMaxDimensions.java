package com.example.mygettyimages.models.apimodels;

import com.google.api.client.util.Key;

import java.io.Serializable;

public class ServerGettyImageMaxDimensions implements Serializable {

    @Key
    private int height;

    @Key
    private int width;


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
