package com.dinhduc_company.stockmarket;

/**
 * Created by duy on 4/17/2015.
 */
public class HistoryItem {

    private String symbol;
    private String type;
    private String portfolioName;
    private int quantity;
    private double cost;
    private String time;

    public HistoryItem(){}

    public HistoryItem(String symbol, String type, String portfolioName, int quantity, double cost, String time){
        this.symbol = symbol;
        this.type = type;
        this.portfolioName = portfolioName;
        this.quantity = quantity;
        this.cost = cost;
        this.time = time;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setPortfolioName(String portfolioName){
        this.portfolioName = portfolioName;
    }

    public String getPortfolioName(){
        return portfolioName;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public double getCost(){
        return cost;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }
}
