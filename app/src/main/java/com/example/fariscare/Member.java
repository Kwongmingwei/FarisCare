package com.example.fariscare;
public class Member {
    private long UserID;
    private String Name;
    private String Email;
    private String password;
    private String Address;
    private String PhoneNo;
    private String EmergencyContact;
    private String RequestedItems;
    private String RequestHistory;



    public Member() {
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long id) { UserID=id;}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        password = Password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) { EmergencyContact = emergencyContact; }
    public String getRequestedItems() {
        return RequestedItems;
    }

    public void setRequestedItems(String requestedItems) { RequestedItems = requestedItems; }

    public String getRequestHistory() {
        return RequestHistory;
    }

    public void setRequestHistory(String requestHistory) {RequestHistory = requestHistory; }

}