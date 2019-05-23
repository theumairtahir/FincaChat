package com.example.fincachat.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {
    long Id;
    String message, userName;
    String dateTime;

    public Message(String message, String userName, String dateTime) {
        this.message = message;
        this.userName = userName;
        this.dateTime = dateTime;
    }

    public long getmId() {
        return Id;
    }

    public void setmId(long mId) {
        this.Id =Id;
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(long id, String message, String dateTime) {
        Id = id;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Message(long id, String message, String dateTime, String user) {
        Id = id;
        this.message = message;
        this.dateTime = dateTime;
        userName = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date= format.parse(dateTime);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            return df.format(date);
        } catch (ParseException e) {
            return dateTime;
        }
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser() {
        return userName;
    }

    public void setUser(String user) {
        userName = user;
    }

}
