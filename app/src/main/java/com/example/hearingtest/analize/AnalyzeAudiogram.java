package com.example.hearingtest.analize;

import static com.example.hearingtest.constants.Constants.greatHear;
import static com.example.hearingtest.constants.Constants.normHear;
import static com.example.hearingtest.constants.Constants.notNormHear;
import static com.example.hearingtest.constants.Constants.notNormHearOneFrequency;
import static com.example.hearingtest.constants.Constants.personNorm;
import static com.example.hearingtest.constants.Constants.populNorm;
import static com.example.hearingtest.constants.Constants.soNormHear;
import static com.example.hearingtest.constants.Constants.soNotNormHear;

import com.example.hearingtest.audiogram.Audiogram;
import lombok.Getter;


public class AnalyzeAudiogram {

    private final Audiogram audiogram;
    private final Audiogram normal;
    private final int ear;
    private final Boolean isNorm;  //false - по популяционной норме
                                    //true - по персональной норме
    @Getter
    private int animation;
    private int betterNorm;
    private int norm;
    private int significant;
    private int insignificant;

    public AnalyzeAudiogram(Audiogram audio, Audiogram norm, Integer numberEar, Boolean status) {
        audiogram = audio;
        normal = norm;
        ear = numberEar;
        isNorm = status;
    }

    public StringBuilder methodAnalyzeOfNorm() {
        betterNorm = norm = significant = insignificant = 0;
        animation = 0;
        countState();

        StringBuilder str = new StringBuilder();
        if (isNorm) {     //Медиана
            str.append(personNorm);
            if (norm == 8 || (norm == 7 && betterNorm == 1)) {
                str.append(normHear);
                animation = 0;
            } else if (norm == 7 && insignificant == 1) {
                str.append(soNormHear);
                animation = 0;
            } else if (significant > 0) {
                str.append(notNormHear);
                animation = 2;
            } else if (insignificant > 1) {
                str.append(soNotNormHear);
                animation = 1;
            } else if (insignificant == 1) {
                str.append(soNormHear);
                animation = 0;
            } else if (betterNorm > 0) {
                str.append(greatHear);
                animation = 0;
            }
        } else {         //Популяционные нормы
            str.append(populNorm);
            if (significant > 0) {
                str.append(notNormHear);
                animation = 2;
            } else if (insignificant > 1) {
                str.append(soNotNormHear);
                animation = 1;
            } else if (insignificant == 1) {
                if (norm < 3) {
                    str.append(soNormHear);
                    animation = 0;
                } else {
                    str.append(notNormHearOneFrequency);
                    animation = 1;
                }
            } else if (betterNorm > 0) {
                str.append(greatHear);
                animation = 0;
            } else {
                str.append(normHear);
                animation = 0;
            }
        }
        return str;
    }

    private void countState(){
        for (int i = 0; i < audiogram.getValueAmplitudeLeft().length; i++) {
            int comparison;
            if (ear == 0) {
                comparison = audiogram.getValueAmplitudeLeft()[i] - normal.getValueAmplitudeLeft()[i];
            } else {
                comparison = audiogram.getValueAmplitudeRight()[i] - normal.getValueAmplitudeRight()[i];
            }
            if (comparison < 0) {
                betterNorm++;
            } else if (comparison < 5) {
                norm++;
            } else if (comparison < 15) {
                insignificant++;
            } else {
                significant++;
            }
        }
    }
}
