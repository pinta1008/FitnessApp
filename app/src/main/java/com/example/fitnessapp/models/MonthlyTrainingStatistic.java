package com.example.fitnessapp.models;

public class MonthlyTrainingStatistic
{
    public int year;
    public int month;
    public int count;

    public MonthlyTrainingStatistic(int year, int month, int count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
