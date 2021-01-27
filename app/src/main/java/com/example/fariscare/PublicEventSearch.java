package com.example.fariscare;

public class PublicEventSearch {
    private String EventName;
    private String EventDesc;
    private String EventDate;
    private String EventType;
    public PublicEventSearch(){};
    public PublicEventSearch(String eventName, String eventDesc, String eventDate, String eventType) {
        EventName = eventName;
        EventDesc = eventDesc;
        EventDate = eventDate;
        EventType = eventType;
    }

    public String geteventName() {
        return EventName;
    }

    public void seteventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDesc() {
        return EventDesc;
    }

    public void setEventDesc(String eventDesc) {
        EventDesc = eventDesc;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String geteventType() {
        return EventType;
    }

    public void seteventType(String eventType) {
        EventType = eventType;
    }
}