package com.example.ortho;

public class DebtsInfo {

    private String date,dateDue,personInteracted,key,debtType;

    private double amount;

    public double getDebtGot() {
        return debtGot;
    }

    public void setDebtGot(double debtGot) {
        this.debtGot = debtGot;
    }

    private double debtGot;

    public DebtsInfo(String date, String dateDue, String personInteracted, String debtType, double amount) {
        this.date = date;
        this.dateDue = dateDue;
        this.personInteracted = personInteracted;
        this.debtType = debtType;
        this.amount = amount;
    }

    public DebtsInfo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateDue() {
        return dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
    }

    public String getPersonInteracted() {
        return personInteracted;
    }

    public void setPersonInteracted(String personInteracted) {
        this.personInteracted = personInteracted;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDebtType() {
        return debtType;
    }

    public void setDebtType(String debtType) {
        this.debtType = debtType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
