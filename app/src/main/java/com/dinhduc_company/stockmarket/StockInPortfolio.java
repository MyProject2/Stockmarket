package com.dinhduc_company.stockmarket;

/**
 * Created by NguyenDinh on 4/4/2015.
 */
public class StockInPortfolio {
    int id;
    String symbol;
    double quantity;
    double averagePurchase;
    double cost;
    double interest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAveragePurchase() {
        return averagePurchase;
    }

    public void setAveragePurchase(double averagePurchase) {
        this.averagePurchase = averagePurchase;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
