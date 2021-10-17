package com.example.ortho;

public class TourInfo {

    private String tourName,startDate,endDate,key,destination;

    private double budget,budgetSpend;

    private int number_of_people;

    public TourInfo(String tourName, String destination,String startDate, String endDate, double budget, int number_of_people) {
        this.tourName = tourName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.destination= destination;
        this.number_of_people = number_of_people;
    }

    public double getBudgetSpend() {
        return budgetSpend;
    }

    public void setBudgetSpend(double budgetSpend) {
        this.budgetSpend = budgetSpend;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public TourInfo() {
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }
}
