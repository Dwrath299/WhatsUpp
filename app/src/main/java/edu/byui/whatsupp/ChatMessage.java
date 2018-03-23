package edu.byui.whatsupp;

import java.util.Date;

/**
 * Created by Dallin's PC on 3/21/2018.
 */

/**
 * Contains all the information that is stored inside a chat message.
 */
public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;

    /**
     * Non-default Constructor.
     * @param messageText
     * @param messageUser
     */
    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    /**
     * Default Constructor.
     */
    public ChatMessage(){

    }

    /**
     * Getters and Setters.
     * @return
     */
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