package com.example.mygettyimages.models.apimodels;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

public class ServerGettyImageList implements Serializable {

    @Key
    private List<ServerGettyImage> images;

    public List<ServerGettyImage> getImages() {
        return images;
    }

    public void setImages(List<ServerGettyImage> images) {
        this.images = images;
    }
}
