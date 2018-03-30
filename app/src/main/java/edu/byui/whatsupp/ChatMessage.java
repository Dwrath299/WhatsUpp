package edu.byui.whatsupp;

import java.util.Date;

/**
 * <h1>Chat Messages</h1>
 *  This is to store the information of a group chat

 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String group;

	/**
     * Constructor asks for everything needed to store in the database
     * @param messageText The actual text of a message
	 * @param messageUser The user that sent the message
	 * @param group The group that has the message
     */
    public ChatMessage(String messageText, String messageUser, String group) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.group = group;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

	/**
     * Default Constructor
	 * Not used
     */
    public ChatMessage(){

    }

	/**
     * Get Group
	 * To know which group to have the message in
	 * @return group The group title
     */
    public String getGroup() {
        return group;
    }

	/**
     * Set Group
	 * To know which group to have the message in
	 * @param group The group title
     */
    public void setGroup(String group) {
        this.group = group;
    }

	/**
     * Get Message Text
	 * To get that actual text content
	 * @return messageText
     */
    public String getMessageText() {
        return messageText;
    }
	
	/**
     * Set Message Text
	 * To set that actual text content
	 * @param messageText
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

	/**
     * Get Message User
	 * The user that sent the message
	 * @return messageText
     */
    public String getMessageUser() {
        return messageUser;
    }

	/**
     * Set Message User
	 * The user that sent the message
	 * @param messageText
     */
    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

	/**
     * Get Message Time
	 * The time that the message was sent
	 * @return messageTime
     */
    public long getMessageTime() {
        return messageTime;
    }

	/**
     * Set Message Time
	 * The time that the message was sent
	 * @param messageTime
     */
    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}