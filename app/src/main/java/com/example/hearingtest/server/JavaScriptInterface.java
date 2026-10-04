package com.example.hearingtest.server;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Getter
@Setter
public class JavaScriptInterface {
    private Context mContext;
    private Date lastDateAudiogram;
    private Date lastDateMedian;
    private String username;

    public JavaScriptInterface(Context c) {
        mContext = c;
    }
}
