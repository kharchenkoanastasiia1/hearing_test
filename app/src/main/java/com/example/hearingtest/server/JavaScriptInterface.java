package com.example.hearingtest.server;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JavaScriptInterface {
    Context mContext;
    Date lastDateAudiogram;
    Date lastDateMedian;
    String username;

    public JavaScriptInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void getLastDateExamination(String lastDateAud, String lastDateMed, String name) throws JsonProcessingException, ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        lastDateAudiogram = df.parse(lastDateAud);
        lastDateMedian = df.parse(lastDateMed);
        username = name;
    }

    public Date getLastDateAudiogram() {
        return lastDateAudiogram;
    }

    public void setLastDateAudiogram(Date lastDateAudiogram) {
        this.lastDateAudiogram = lastDateAudiogram;
    }

    public Date getLastDateMedian() {
        return lastDateMedian;
    }

    public void setLastDateMedian(Date lastDateMedian) {
        this.lastDateMedian = lastDateMedian;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
