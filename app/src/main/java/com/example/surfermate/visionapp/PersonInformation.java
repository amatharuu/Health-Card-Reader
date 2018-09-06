package com.example.surfermate.visionapp;

public class PersonInformation {
    private String fname, lname;
    private String address;

    public PersonInformation(String fname, String lname, String ddress) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
    }

    //getters and setters
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }

}