package com.example.chazhampton.hospicerouter;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Patient {
    public String patientName;
    public String patientAddress;
    public String patientComments;

    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Patient(String username, String address, String comments) {
        this.patientName = username;
        this.patientAddress = address;
        this.patientComments = comments;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public String getPatientComments() {
        return patientComments;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String name) {  this.patientName = name; }

    public void setPatientAddress(String address) { this.patientAddress = address; }

    public void setPatientComments(String comments) { this.patientComments = comments; }

}
