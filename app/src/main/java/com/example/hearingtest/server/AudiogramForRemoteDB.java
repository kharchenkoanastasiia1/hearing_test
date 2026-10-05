package com.example.hearingtest.server;

import android.annotation.SuppressLint;
import com.example.hearingtest.audiogram.Audiogram;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class AudiogramForRemoteDB {
    private int id;

    @JsonSetter("username")
    private String username;

    @JsonSetter("f125Left")
    private int f125Left;

    @JsonSetter("f250Left")
    private int f250Left;

    @JsonSetter("f500Left")
    private int f500Left;

    @JsonSetter("f1000Left")
    private int f1000Left;

    @JsonSetter("f2000Left")
    private int f2000Left;

    @JsonSetter("f3000Left")
    private int f3000Left;

    @JsonSetter("f4000Left")
    private int f4000Left;

    @JsonSetter("f8000Left")
    private int f8000Left;

    @JsonSetter("f125Right")
    private int f125Right;

    @JsonSetter("f250Right")
    private int f250Right;

    @JsonSetter("f500Right")
    private int f500Right;

    @JsonSetter("f1000Right")
    private int f1000Right;

    @JsonSetter("f2000Right")
    private int f2000Right;

    @JsonSetter("f3000Right")
    private int f3000Right;

    @JsonSetter("f4000Right")
    private int f4000Right;

    @JsonSetter("f8000Right")
    private int f8000Right;

    @JsonSetter("date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    public AudiogramForRemoteDB(Audiogram audiogram, String userName) throws ParseException {
        username = userName;
        f125Left = audiogram.getValueAmplitudeLeft()[0];
        f250Left = audiogram.getValueAmplitudeLeft()[1];
        f500Left = audiogram.getValueAmplitudeLeft()[2];
        f1000Left = audiogram.getValueAmplitudeLeft()[3];
        f2000Left = audiogram.getValueAmplitudeLeft()[4];
        f3000Left = audiogram.getValueAmplitudeLeft()[5];
        f4000Left = audiogram.getValueAmplitudeLeft()[6];
        f8000Left = audiogram.getValueAmplitudeLeft()[7];
        f125Right = audiogram.getValueAmplitudeRight()[0];
        f250Right = audiogram.getValueAmplitudeRight()[1];
        f500Right = audiogram.getValueAmplitudeRight()[2];
        f1000Right = audiogram.getValueAmplitudeRight()[3];
        f2000Right = audiogram.getValueAmplitudeRight()[4];
        f3000Right = audiogram.getValueAmplitudeRight()[5];
        f4000Right = audiogram.getValueAmplitudeRight()[6];
        f8000Right = audiogram.getValueAmplitudeRight()[7];
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = df.parse(audiogram.getDateRecord().toString());
    }
}
