package com.example.ortho;

public class AccountInfo {

    String email,gender;

    public AccountInfo(String email) {
        this.email = email;
    }

    public AccountInfo() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
