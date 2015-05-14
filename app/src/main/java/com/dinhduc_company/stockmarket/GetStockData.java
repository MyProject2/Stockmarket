package com.dinhduc_company.stockmarket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by NguyenDinh on 4/4/2015.
 */
public class GetStockData {
    public InputStream openHttpConnection(String urlString) {
        int response = -1;
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (!(urlConnection instanceof HttpURLConnection)) {
                throw new IOException("not HTTP connection");
            } else {
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                response = httpURLConnection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK)
                    in = httpURLConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public Stock getStockData(String symbol) {
        Stock stock = new Stock();
        String urlString = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22" + symbol.toUpperCase() + "%22)&env=store://datatables.org/alltableswithkeys";
        InputStream in = openHttpConnection(urlString);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(in);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NodeList quotes = doc.getElementsByTagName("quote");
        Element quote = (Element) quotes.item(0);

        stock.setLastTradePrice(quote.getElementsByTagName("LastTradePriceOnly").item(0).getTextContent());
        stock.setPriceChange(quote.getElementsByTagName("Change").item(0).getTextContent());
        stock.setPercentChange(quote.getElementsByTagName("ChangeinPercent").item(0).getTextContent());
        Calendar calendar = Calendar.getInstance();
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        stock.setDate(simpleDateFormat.format(calendar.getTime()));
        stock.setPreviousClose(quote.getElementsByTagName("PreviousClose").item(0).getTextContent());
        stock.setDaysRange(quote.getElementsByTagName("DaysRange").item(0).getTextContent());
        stock.setOpen(quote.getElementsByTagName("Open").item(0).getTextContent());
        stock.setYearRange(quote.getElementsByTagName("YearRange").item(0).getTextContent());
        stock.setBid(quote.getElementsByTagName("Bid").item(0).getTextContent());
        stock.setVolume(quote.getElementsByTagName("Volume").item(0).getTextContent());
        stock.setAvgVolume(quote.getElementsByTagName("AverageDailyVolume").item(0).getTextContent());
        stock.setAsk(quote.getElementsByTagName("Ask").item(0).getTextContent());
        stock.setOneYearTarget(quote.getElementsByTagName("OneyrTargetPrice").item(0).getTextContent());
        stock.setMarketCap(quote.getElementsByTagName("MarketCapitalization").item(0).getTextContent());
        stock.setPe(quote.getElementsByTagName("PERatio").item(0).getTextContent());
        stock.setEps(quote.getElementsByTagName("EarningsShare").item(0).getTextContent());

        return stock;
    }
}
