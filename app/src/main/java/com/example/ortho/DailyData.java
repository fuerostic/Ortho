package com.example.ortho;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyData {


    private String day,month,year;

    private Date date;

    public DailyData(Date date) {

        this.date = date;

        day = new SimpleDateFormat("dd").format(date);

        month = new SimpleDateFormat("MM").format(date);

        year = new SimpleDateFormat("yyyy").format(date);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
