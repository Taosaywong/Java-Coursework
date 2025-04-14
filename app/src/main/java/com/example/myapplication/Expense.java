package com.example.myapplication;

public class Expense {
    private int id;
    private String title;

    private String destination;
    private double amount;
    private String date;
    private String description;
    private String riskAssessment;

    // ✅ Constructor that matches the DBHandler parameters
    public Expense(int id, String title, String destination, double amount, String date, String description, String riskAssessment) {
        this.id = id;
        this.title = title;
        this.destination = destination;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.riskAssessment = riskAssessment;
    }

    // ✅ Getters for retrieving data
    public int getId() {
        return id;
    }

    public String getExpenseTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    // ✅ Optional: Setters if you need to modify fields
    public void setId(int id) {
        this.id = id;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.title = expenseTitle;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }
}
