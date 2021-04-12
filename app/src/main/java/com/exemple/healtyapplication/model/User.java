package com.exemple.healtyapplication.model;

import com.google.firebase.firestore.SnapshotMetadata;

import java.io.Serializable;

public class User implements Serializable {

    private String email;
    private String name;
    private String surname;
    private String prefix;
    private String uid;
    private String phone;
    private String pwd;

    public User(String email, String pwd, String name, String surname, String prefix, String phone) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.prefix = prefix;
        this.phone = phone;
        this.pwd = pwd;
    }

    public User(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }
}
