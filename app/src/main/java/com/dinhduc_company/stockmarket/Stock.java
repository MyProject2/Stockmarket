package com.dinhduc_company.stockmarket;

/**
 * Created by NguyenDinh on 4/12/2015.
 */
public class Stock {
    String lastTradePrice, priceChange, percentChange, date, previousClose, daysRange, open, yearRange;
    String bid, volume, avgVolume, ask, oneYearTarget, marketCap, pe, eps;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAvgVolume() {
        return avgVolume;
    }

    public void setAvgVolume(String avgVolume) {
        this.avgVolume = avgVolume;
    }

    public String getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(String lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }

    public String getDaysRange() {
        return daysRange;
    }

    public void setDaysRange(String daysRange) {
        this.daysRange = daysRange;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getYearRange() {
        return yearRange;
    }

    public void setYearRange(String yearRange) {
        this.yearRange = yearRange;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getOneYearTarget() {
        return oneYearTarget;
    }

    public void setOneYearTarget(String oneYearTarget) {
        this.oneYearTarget = oneYearTarget;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }


}
