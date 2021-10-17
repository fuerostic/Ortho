package com.example.ortho;

public class WishInfo {

    private String wishName,wishType,wishDate,key;

    private int daysLeft;

    private double wishValue;

    private double valueGot;

    private int image;

    public WishInfo(String wishName, String wishType, String wishDate, double wishValue) {
        this.wishName = wishName;
        this.wishType = wishType;
        this.wishDate = wishDate;
        this.wishValue = wishValue;
    }

    public String getWishName() {
        return wishName;
    }

    public WishInfo() {
    }

    public void setWishName(String wishName) {
        this.wishName = wishName;
    }

    public String getWishType() {
        return wishType;
    }

    public void setWishType(String wishType) {
        this.wishType = wishType;
    }

    public String getWishDate() {
        return wishDate;
    }

    public void setWishDate(String wishDate) {
        this.wishDate = wishDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public double getWishValue() {
        return wishValue;
    }

    public void setWishValue(double wishValue) {
        this.wishValue = wishValue;
    }

    public double getValueGot() {
        return valueGot;
    }

    public void setValueGot(double valueGot) {
        this.valueGot = valueGot;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
