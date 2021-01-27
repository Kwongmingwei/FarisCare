package com.example.fariscare;

class PrivateEvent {
    private String Title;
    private String Date;
    private String Time;
    private String Notes;
    private String Participants;
    private String EventType;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) { Title=title;}

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String geteventType() {
        return EventType;
    }

    public void seteventType(String eventType) {
        EventType = eventType;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getParticipants() {
        return Participants;
    }

    public void setParticipants(String participants) {
        Participants = participants;
    }



}
