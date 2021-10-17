package com.example.ortho;

public class BankAccountInfo {

    private String bankName;
    private String accountType,accountNumber;
    private String loanDueDate;

    public String getBankName() {
        return bankName;
    }

    public BankAccountInfo() {
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getLoanDueDate() {
        return loanDueDate;
    }

    public void setLoanDueDate(String loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(double bankBalance) {
        this.bankBalance = bankBalance;
    }

    public double getLoan() {
        return loan;
    }

    public void setLoan(double loan) {
        this.loan = loan;
    }

    private String key;

    private double bankBalance,loan;

    public BankAccountInfo(String bankName, String accountType,String accountNumber, String loanDueDate, double bankBalance, double loan) {
        this.bankName = bankName;
        this.accountType = accountType;
        this.loanDueDate = loanDueDate;
        this.bankBalance = bankBalance;
        this.accountNumber=accountNumber;
        this.loan = loan;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
