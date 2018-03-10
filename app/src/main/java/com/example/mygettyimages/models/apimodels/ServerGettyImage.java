package com.example.mygettyimages.models.apimodels;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

public class ServerGettyImage implements Serializable {

    @Key("display_sizes")
    private List<ServerGettyImageDisplaySize> displaySizes;

    @Key("max_dimensions")
    private ServerGettyImageMaxDimensions maxDimensions;

    @Key
    private String title;

    @Key
    private String id;

    public List<ServerGettyImageDisplaySize> getDisplaySizes() {
        return displaySizes;
    }

    public void setDisplaySizes(List<ServerGettyImageDisplaySize> displaySizes) {
        this.displaySizes = displaySizes;
    }

    public ServerGettyImageMaxDimensions getMaxDimensions() {
        return maxDimensions;
    }

    public void setMaxDimensions(ServerGettyImageMaxDimensions maxDimensions) {
        this.maxDimensions = maxDimensions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
