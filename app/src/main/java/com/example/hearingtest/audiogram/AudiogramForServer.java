package com.example.hearingtest.audiogram;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudiogramForServer {
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

    public AudiogramForServer() {
    }

    public AudiogramForServer(Audiogram audiogram, String username) throws ParseException {
        this.username = username;
        this.f125Left = audiogram.getValueAmplitudeLeft()[0];
        this.f250Left = audiogram.getValueAmplitudeLeft()[1];
        this.f500Left = audiogram.getValueAmplitudeLeft()[2];
        this.f1000Left = audiogram.getValueAmplitudeLeft()[3];
        this.f2000Left = audiogram.getValueAmplitudeLeft()[4];
        this.f3000Left = audiogram.getValueAmplitudeLeft()[5];
        this.f4000Left = audiogram.getValueAmplitudeLeft()[6];
        this.f8000Left = audiogram.getValueAmplitudeLeft()[7];
        this.f125Right = audiogram.getValueAmplitudeRight()[0];
        this.f250Right = audiogram.getValueAmplitudeRight()[1];
        this.f500Right = audiogram.getValueAmplitudeRight()[2];
        this.f1000Right = audiogram.getValueAmplitudeRight()[3];
        this.f2000Right = audiogram.getValueAmplitudeRight()[4];
        this.f3000Right = audiogram.getValueAmplitudeRight()[5];
        this.f4000Right = audiogram.getValueAmplitudeRight()[6];
        this.f8000Right = audiogram.getValueAmplitudeRight()[7];
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.date = df.parse(audiogram.getDateRecorderToString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getF125Left() {
        return f125Left;
    }

    public void setF125Left(int f125Left) {
        this.f125Left = f125Left;
    }

    public int getF250Left() {
        return f250Left;
    }

    public void setF250Left(int f250Left) {
        this.f250Left = f250Left;
    }

    public int getF500Left() {
        return f500Left;
    }

    public void setF500Left(int f500Left) {
        this.f500Left = f500Left;
    }

    public int getF1000Left() {
        return f1000Left;
    }

    public void setF1000Left(int f1000Left) {
        this.f1000Left = f1000Left;
    }

    public int getF2000Left() {
        return f2000Left;
    }

    public void setF2000Left(int f2000Left) {
        this.f2000Left = f2000Left;
    }

    public int getF3000Left() {
        return f3000Left;
    }

    public void setF3000Left(int f3000Left) {
        this.f3000Left = f3000Left;
    }

    public int getF4000Left() {
        return f4000Left;
    }

    public void setF4000Left(int f4000Left) {
        this.f4000Left = f4000Left;
    }

    public int getF8000Left() {
        return f8000Left;
    }

    public void setF8000Left(int f8000Left) {
        this.f8000Left = f8000Left;
    }

    public int getF125Right() {
        return f125Right;
    }

    public void setF125Right(int f125Right) {
        this.f125Right = f125Right;
    }

    public int getF250Right() {
        return f250Right;
    }

    public void setF250Right(int f250Right) {
        this.f250Right = f250Right;
    }

    public int getF500Right() {
        return f500Right;
    }

    public void setF500Right(int f500Right) {
        this.f500Right = f500Right;
    }

    public int getF1000Right() {
        return f1000Right;
    }

    public void setF1000Right(int f1000Right) {
        this.f1000Right = f1000Right;
    }

    public int getF2000Right() {
        return f2000Right;
    }

    public void setF2000Right(int f2000Right) {
        this.f2000Right = f2000Right;
    }

    public int getF3000Right() {
        return f3000Right;
    }

    public void setF3000Right(int f3000Right) {
        this.f3000Right = f3000Right;
    }

    public int getF4000Right() {
        return f4000Right;
    }

    public void setF4000Right(int f4000Right) {
        this.f4000Right = f4000Right;
    }

    public int getF8000Right() {
        return f8000Right;
    }

    public void setF8000Right(int f8000Right) {
        this.f8000Right = f8000Right;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
