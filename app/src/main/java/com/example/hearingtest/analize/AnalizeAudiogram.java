package com.example.hearingtest.analize;

import static com.example.hearingtest.referencebook.Constants.greatHear;
import static com.example.hearingtest.referencebook.Constants.normHear;
import static com.example.hearingtest.referencebook.Constants.notNormHear;
import static com.example.hearingtest.referencebook.Constants.notNormHearOneFrequency;
import static com.example.hearingtest.referencebook.Constants.personNorm;
import static com.example.hearingtest.referencebook.Constants.populNorm;
import static com.example.hearingtest.referencebook.Constants.soNormHear;
import static com.example.hearingtest.referencebook.Constants.soNotNormHear;

import com.example.hearingtest.audiogram.Audiogram;


public class AnalizeAudiogram {

    private Audiogram audiogram;
    private Audiogram normal;
    private int ear = 0;
    private int animation = 0;
    private Boolean isNorm = false;  //false - по популяционнной нормой
                                    //true - по персональной нормой
    private int betterNorm = 0;
    private int norm = 0;
    private int significant = 0;
    private int insignificant = 0;

    public AnalizeAudiogram(Audiogram audio, Audiogram norm, Integer numberEar, Boolean status){
        audiogram = audio;
        normal = norm;
        ear = numberEar;
        isNorm = status;
    }

    public StringBuilder methodAnalizeOfNorm(){
        for(int i = 0; i < audiogram.valueAmplitudeLeft.length; i++){
            int comparison;
            if(ear == 0){
                comparison = audiogram.valueAmplitudeLeft[i] - normal.valueAmplitudeLeft[i];
            } else{
                comparison = audiogram.valueAmplitudeRight[i] - normal.valueAmplitudeRight[i];
            }
            if(comparison < 0){
                betterNorm++;
            } else if(comparison < 5){
                norm++;
            } else if(comparison < 15){
                insignificant++;
            } else{
                significant++;
            }
        }

        StringBuilder str = new StringBuilder();
        if(isNorm){     //Медиана
            str.append(personNorm);
            if(norm == 8 || (norm == 7 && betterNorm == 1)){
                str.append(normHear);
                animation = 0;
            } else if(norm == 7 && insignificant == 1){
                str.append(soNormHear);
                animation = 0;
            } else if(significant > 0){
                str.append(notNormHear);
                animation = 2;
            } else if(insignificant > 1){
                str.append(soNotNormHear);
                animation = 1;
            } else if(insignificant == 1){
                str.append(soNormHear);
                animation = 0;
            } else if(betterNorm > 0){
                str.append(greatHear);
                animation = 0;
            }
        } else{         //Популяционные нормы
            str.append(populNorm);
            if(significant > 0){
                str.append(notNormHear);
                animation = 2;
            } else if(insignificant > 1){
                str.append(soNotNormHear);
                animation = 1;
            } else if(insignificant == 1){
                if(norm < 3){
                    str.append(soNormHear);
                    animation = 0;
                } else{
                    str.append(notNormHearOneFrequency);
                    animation = 1;
                }
            } else if(betterNorm > 0){
                str.append(greatHear);
                animation = 0;
            } else{
                str.append(normHear);
                animation = 0;
            }
        }
        return str;
    }

    public int getAnimation(){ return animation; }

}
