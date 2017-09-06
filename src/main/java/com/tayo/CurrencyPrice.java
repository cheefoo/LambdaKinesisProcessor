package com.tayo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created by temitayo on 8/30/17.
 */
@DynamoDBTable(tableName = "CurrencyRateTable")
public class CurrencyPrice
{
    private String currencySymbol;
    private double cuurencyBuyRate;
    private double currencySellRate;

    public CurrencyPrice(){}

    public CurrencyPrice(String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
    }

    @DynamoDBHashKey (attributeName = "currencySymbol")
    public String getCurrencySymbol()
    {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
    }

    @DynamoDBAttribute (attributeName = "cuurencyBuyRate")
    public double getCuurencyBuyRate()
    {
        return cuurencyBuyRate;
    }

    public void setCuurencyBuyRate(double cuurencyBuyRate)
    {
        this.cuurencyBuyRate = cuurencyBuyRate;
    }

    @DynamoDBAttribute   (attributeName = "currencySellRate")
    public double getCurrencySellRate()
    {
        return currencySellRate;
    }

    public void setCurrencySellRate(double currencySellRate)
    {
        this.currencySellRate = currencySellRate;
    }
}
