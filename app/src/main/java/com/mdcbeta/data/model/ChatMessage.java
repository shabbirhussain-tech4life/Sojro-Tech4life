package com.mdcbeta.data.model;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageUserName;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser, String userName) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageUserName = userName;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageUserName() {
        return messageUserName;
    }

    public void setMessageUserName(String messageUserName) {
        this.messageUserName = messageUserName;
    }
}