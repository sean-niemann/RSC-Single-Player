package org.nemotech.rsc.model;

public class ChatMessage {
    
    private Mob sender;
    
    private String message;
    
    private Mob recipient = null;
    
    public ChatMessage(Mob sender, String message, Mob recipient) {
        this.sender = sender;
        this.message = message;
        this.recipient = recipient;
    }
    
    public Mob getRecipient() {
        return recipient;
    }

    public Mob getSender() {
        return sender;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getLength() {
        return message.length();
    }

}
