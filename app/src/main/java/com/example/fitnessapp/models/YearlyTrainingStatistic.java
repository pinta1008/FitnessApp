package com.example.fitnessapp.models;

public class YearlyTrainingStatistic {


    private int year;
    private int count;

    public YearlyTrainingStatistic(int year, int count) {
        this.year = year;
        this.count = count;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
