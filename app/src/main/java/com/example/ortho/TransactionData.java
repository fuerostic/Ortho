package com.example.ortho;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionData implements Comparable{

    private String date,category,type;

    private double amount;

    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionData(String date, String category, double amount, String type) {
        this.date = date;
        this.category = category;
        this.type = type;
        this.amount = amount;
    }

    public TransactionData() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(Object o) {

        TransactionData transactionData = (TransactionData) o;

        String dateTemp = transactionData.getDate();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = format.parse(date);
            Date date2 = format.parse(dateTemp);


            if(date1.compareTo(date2)>0){
                return -1;
            }else if(date1.compareTo(date2)<0){

                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
