package com.example.ortho;

import java.util.ArrayList;

public class DateInfo {

    private double expense,deposit,wishSave,bankDeposit,bankWithdrawn,debtPay,debtReceived;

    private ArrayList<TransactionData>transactionDataArrayList;

    private ArrayList<BankAccountInfo>bankAccountInfoArrayList;

    private ArrayList<WishInfo>wishInfoArrayList;


    public DateInfo() {

        this.expense=0;
        this.deposit=0;

        this.wishSave=0;

        this.bankDeposit=0;
        this.bankWithdrawn=0;

        this.debtPay=0;
        this.debtReceived=0;

        transactionDataArrayList = new ArrayList<TransactionData>();

        bankAccountInfoArrayList = new ArrayList<BankAccountInfo>();

        wishInfoArrayList = new ArrayList<WishInfo>();

    }

    public void wishinfoAdd(WishInfo wishInfo){

        wishInfoArrayList.add(wishInfo);
    }

    public void bankinfoAdd(BankAccountInfo bankAccountInfo){


        bankAccountInfoArrayList.add(bankAccountInfo);
    }

    public ArrayList<TransactionData> getTransactionDataArrayList() {
        return transactionDataArrayList;
    }

    public ArrayList<BankAccountInfo> getBankAccountInfoArrayList() {
        return bankAccountInfoArrayList;
    }

    public ArrayList<WishInfo> getWishInfoArrayList() {
        return wishInfoArrayList;
    }

    public void transactionAdd(TransactionData transactionData){

        transactionDataArrayList.add(transactionData);
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getWishSave() {
        return wishSave;
    }

    public void setWishSave(double wishSave) {
        this.wishSave = wishSave;
    }

    public double getBankDeposit() {
        return bankDeposit;
    }

    public void setBankDeposit(double bankDeposit) {
        this.bankDeposit = bankDeposit;
    }

    public double getBankWithdrawn() {
        return bankWithdrawn;
    }

    public void setBankWithdrawn(double bankWithdrawn) {
        this.bankWithdrawn = bankWithdrawn;
    }

    public double getDebtPay() {
        return debtPay;
    }

    public void setDebtPay(double debtPay) {
        this.debtPay = debtPay;
    }

    public double getDebtReceived() {
        return debtReceived;
    }

    public void setDebtReceived(double debtReceived) {
        this.debtReceived = debtReceived;
    }
}
