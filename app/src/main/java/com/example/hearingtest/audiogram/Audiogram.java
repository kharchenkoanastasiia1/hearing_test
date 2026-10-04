package com.example.hearingtest.audiogram;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Audiogram {
    private Integer idAudiogram;
    private LocalDate dateRecord;
    private Integer idUser;
    private int idCounter = 1;
    public Integer[] valueAmplitudeLeft;
    public Integer[] valueAmplitudeRight;
    public static Integer[] valueFrequency = new Integer[]{125, 250, 500, 1000, 2000, 3000, 4000, 8000};

    public Audiogram() {
        valueAmplitudeLeft = new Integer[8];
        valueAmplitudeRight = new Integer[8];
    }

    public Audiogram(Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight) {
        valueAmplitudeLeft = amplitudeInDecibelsLeft;
        valueAmplitudeRight = amplitudeInDecibelsRight;
    }

    public Audiogram(Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight, Integer id) {
        valueAmplitudeLeft = amplitudeInDecibelsLeft;
        valueAmplitudeRight = amplitudeInDecibelsRight;
        idUser = id;
    }

    public Audiogram(Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight, Boolean status, Integer idUser) {
        if(amplitudeInDecibelsLeft.length == 8 || amplitudeInDecibelsRight.length == 8){
            valueAmplitudeLeft = amplitudeInDecibelsLeft;
            valueAmplitudeRight = amplitudeInDecibelsRight;
            this.setIdAudiogram(idCounter);
            this.setIdCounter(status);
            this.setIdUser(idUser);
        } else{
            valueAmplitudeLeft = new Integer[8];
            valueAmplitudeRight = new Integer[8];
        }
    }

    //Получение с БД:
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Audiogram(Integer idAudio, Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight, LocalDate date, Integer idUser) {
        if(amplitudeInDecibelsLeft.length == 8 || amplitudeInDecibelsRight.length == 8){
            valueAmplitudeLeft = amplitudeInDecibelsLeft;
            valueAmplitudeRight = amplitudeInDecibelsRight;
            this.setIdAudiogram(idAudio);
            this.setIdUser(idUser);
            this.setDateCounter(date);
        }
    }

    //-----Getters-----
    public Integer getIdAudiogram(){
        return idAudiogram;
    }

    public Integer getIdUser(){
        return idUser;
    }

    public LocalDate getDateRecorder(){
        return dateRecord;
    }

    public String getDateRecorderToString(){
        return dateRecord.toString();
    }

    public int getIdCounter() {
        return idCounter;
    }

    public Integer[] getValueAmplitudeLeft() {
        return valueAmplitudeLeft;
    }

    public Integer[] getValueAmplitudeRight() {
        return valueAmplitudeRight;
    }

    //-----Setters-----
    public void setIdAudiogram(Integer id){
        this.idAudiogram = id;
    }

    public void setIdUser(Integer id){
        this.idUser = id;
    }

    public void setIdCounter(Boolean status) {
        if(status){
            this.idCounter++;
        } else{
            this.idCounter = 1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateCounter() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String str = sdf.format(new Date());
        this.dateRecord = LocalDate.parse(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateCounter(LocalDate date) {
        this.dateRecord = date;
    }

    public void setDateRecord(LocalDate dateRecord) {
        this.dateRecord = dateRecord;
    }

    public void setValueAmplitudeLeft(Integer[] valueAmplitudeLeft) {
        this.valueAmplitudeLeft = valueAmplitudeLeft;
    }

    public void setValueAmplitudeRight(Integer[] valueAmplitudeRight) {
        this.valueAmplitudeRight = valueAmplitudeRight;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID=").append(idAudiogram).append("\nAmplitude in decibels:\t");
        for(int i : valueAmplitudeLeft){
            str.append(i).append("\t");
        }
        str.append("\n");
        for(int i : valueAmplitudeRight){
            str.append(i).append("\t");
        }
        str.append("\nidUser= ").append(idUser);
        return str.toString();
    }
}
