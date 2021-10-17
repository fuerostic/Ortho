package com.example.ortho;

public class ExpenseListChildData {

    private String date,category;

    private double amount;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ExpenseListChildData(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public ExpenseListChildData() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
