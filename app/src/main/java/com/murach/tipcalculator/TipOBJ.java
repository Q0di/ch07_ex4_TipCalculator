package com.murach.tipcalculator;

/**
 * Created by 660161986 on 2/29/2016.
 */
import android.annotation.SuppressLint;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TipOBJ {

    private long id;
    private long dateMillis;
    private float billAmount;
    private float tipPercent;

    public TipOBJ() {
        setId(0);
        setDateMillis(System.currentTimeMillis());
        setBillAmount(0);
        setTipPercent(.15f);
    }

    public TipOBJ(long id, long dateMillis, float billAmount, float tipPercent) {
        this.setId(id);
        this.setDateMillis(dateMillis);
        this.setBillAmount(billAmount);
        this.setTipPercent(tipPercent);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateMillis() {
        return dateMillis;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDateStringFormatted() {
        // set the date with formatting
        Date date = new Date(dateMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(float billAmount) {
        this.billAmount = billAmount;
    }

    public float getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(float tipPercent) {
        this.tipPercent = tipPercent;
    }
}