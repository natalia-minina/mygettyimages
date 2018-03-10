package com.example.mygettyimages.models.apimodels;

import com.google.api.client.util.Key;

public class ServerErrorResponse {

    @Key
    private String status;

    @Key
    private String messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}