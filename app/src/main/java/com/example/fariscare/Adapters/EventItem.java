package com.example.fariscare.Adapters;

import android.widget.Button;

import java.time.LocalDateTime;

public class EventItem {
    private String mEventName;
    private String mEventDate;
    private String mEventType;
    private String mEventDesc;
    private Button mEventJoin;

    public EventItem(String eventName, String eventDate, String eventType, String eventDesc, Button eventJoin) {
        mEventName = eventName;
        mEventDate = eventDate;
        mEventType = eventType;
        mEventDesc = eventDesc;
        mEventJoin = eventJoin;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getEventDate() {
        return mEventDate;
    }

    public String getEventType() {
        return mEventType;
    }

    public String getEventDesc() {
        return mEventName;
    }

    public Button getEventJoin() {
        return mEventJoin;
    }
}
