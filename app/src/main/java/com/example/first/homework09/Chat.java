package com.example.first.homework09;


public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public long timestamp;

    public Chat(){

    }

    public Chat(String sender, String senderUid, String receiverUid, String message, long timestamp){
        this.sender = sender;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;

    }

    @Override
    public String toString() {
        return "Chat{" +
                "sender='" + sender + '\'' +
                ", senderUid='" + senderUid + '\'' +
                ", receiverUid='" + receiverUid + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
