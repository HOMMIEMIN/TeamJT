package com.example.homin.test1;

import java.io.Serializable;

/**
 * Created by stu on 2018-03-26.
 */

public class Chat implements Serializable{

    private String name;
    private String id;
    private String chat;
    private String time;

    public Chat(){}

    public Chat(String name, String id, String chat, String time) {
        this.name = name;
        this.id = id;
        this.chat = chat;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
