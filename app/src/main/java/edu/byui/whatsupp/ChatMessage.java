package edu.byui.whatsupp;

import java.util.Date;

/**
 * Created by Dallin's PC on 3/21/2018.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String group;

    public ChatMessage(String messageText, String messageUser, String group) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.group = group;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
}