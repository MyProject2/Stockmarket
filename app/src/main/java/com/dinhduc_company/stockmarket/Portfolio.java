package com.dinhduc_company.stockmarket;

/**
 * Created by NguyenDinh on 4/7/2015.
 */
public class Portfolio {
    int id;
    String portfolioName;
    double interest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
