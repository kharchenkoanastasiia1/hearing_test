package com.example.hearingtest.audiogram;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Audiogram {
    private Integer idAudiogram;
    private LocalDate dateRecord;
    private Integer idUser;
    private int idCounter = 1;
    private Integer[] valueAmplitudeLeft;
    private Integer[] valueAmplitudeRight;
    public static final Integer[] valueFrequency = new Integer[]{125, 250, 500, 1000, 2000, 3000, 4000, 8000};

    public Audiogram() {
        valueAmplitudeLeft = new Integer[8];
        valueAmplitudeRight = new Integer[8];
    }

    public Audiogram(Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight) {
        valueAmplitudeLeft = copyChecked(amplitudeInDecibelsLeft);
        valueAmplitudeRight = copyChecked(amplitudeInDecibelsRight);
    }

    public Audiogram(Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight, Integer idU) {
        valueAmplitudeLeft = copyChecked(amplitudeInDecibelsLeft);
        valueAmplitudeRight = copyChecked(amplitudeInDecibelsRight);
        idUser = idU;
    }

    //Получение с БД:
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Audiogram(Integer idAudio, Integer[] amplitudeInDecibelsLeft, Integer[] amplitudeInDecibelsRight, LocalDate date, Integer idU) {
        valueAmplitudeLeft = copyChecked(amplitudeInDecibelsLeft);
        valueAmplitudeRight = copyChecked(amplitudeInDecibelsRight);
        idAudiogram = idAudio;
        idUser = idU;
        dateRecord = date;
    }

    private boolean validShape(Integer[] values) {
        return values != null && values.length == valueFrequency.length;
    }
    private Integer[] copyChecked(Integer[] values) {
        if (!validShape(values))
            throw new IllegalArgumentException("Invalid audiogram shape");
        return values.clone();
    }

    public void setIdCounter(Boolean status) {
        if(status){
            this.idCounter++;
        } else{
            this.idCounter = 1;
        }
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
