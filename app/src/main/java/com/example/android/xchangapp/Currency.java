package com.example.android.xchangapp;

/**
 * Created by Ogunbowale on 11/1/2017.
 */

public class Currency {

    private String currSym;
    private double btcValue;
    private double ethValue;

    public Currency(String currSym, double ethValue, double btcValue){
        this.currSym = currSym;
        this.ethValue = ethValue;
        this.btcValue = btcValue;
    }

    public String getCurrSym() {
        return currSym;
    }

    public double getBtcValue() {
        return btcValue;
    }

    public double getEthValue() {
        return ethValue;
    }
}
