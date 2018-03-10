package com.example.mygettyimages.models.apimodels;

import com.google.api.client.util.Key;

import java.io.Serializable;

public class ServerGettyImageDisplaySize implements Serializable {

    @Key
    private String uri;

    @Key
    private String name;


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
