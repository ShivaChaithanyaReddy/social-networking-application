package com.example.first.homework09;


import android.graphics.Bitmap;

import java.util.List;

public class FirebaseUserEntity {

    private String userID;
    private String email;
    private String fname;
    private String country;
    private String gender;
    private String lname;
    private String hobby;
    private String myPendingRequest;
    private  String myFriends;
    private String image;
    private Bitmap bimage;
    private String ReceivedFriendRequests;

    List<String> AllMyFriends;




    public FirebaseUserEntity() {
    }

    @Override
    public String toString() {
        return "FirebaseUserEntity{" +
                "userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", country='" + country + '\'' +
                ", image='" + image + '\'' +
                ", gender='" + gender + '\'' +
                ", lname='" + lname + '\'' +
                ", allFrnds='" + AllMyFriends + '\'' +
                ", hobby='" + hobby + '\'' +
                ", myPendingRequest='" + myPendingRequest + '\'' +
                ", myFriends='" + myFriends + '\'' +
                ", ReceivedFriendRequests='" + ReceivedFriendRequests + '\'' +
                '}';
    }

    public FirebaseUserEntity(String hobby, String gender, String userID, String lname, String email, String fname, String country, String myPendingRequest, String myFriends, String ReceivedFriendRequests, String image) {
        this.userID = userID;
        this.email = email;
        this.fname = fname;
        this.country = country;
        this.gender = gender;
        this.lname = lname;
        this.hobby = hobby;
        this.myPendingRequest = myPendingRequest;
        this.myFriends = myFriends;
        this.ReceivedFriendRequests = ReceivedFriendRequests;
        this.image = image;
    }


    public List<String> getAllMyFriends() {
        return AllMyFriends;
    }

    public void setAllMyFriends(List<String> allMyFriends) {
        AllMyFriends = allMyFriends;
    }

    public String getReceivedFriendRequests() {
        return ReceivedFriendRequests;
    }

    public void setReceivedFriendRequests(String receivedFriendRequests) {
        ReceivedFriendRequests = receivedFriendRequests;
    }

    public String getMyFriends() {
        return myFriends;
    }

    public void setMyFriends(String myFriends) {
        this.myFriends = myFriends;
    }

    public String getMyPendingRequest() {
        return myPendingRequest;
    }

    public void setMyPendingRequest(String myPendingRequest) {
        this.myPendingRequest = myPendingRequest;
    }


    public String getuserID() {
        return userID;
    }

    public void setuserID(String uId) {
        this.userID = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBimage() {
        return bimage;
    }

    public void setBimage(Bitmap bimage) {
        this.bimage = bimage;
    }
}