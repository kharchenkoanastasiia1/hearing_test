package com.example.hearingtest.norm;

import com.example.hearingtest.audiogram.Audiogram;

import static com.example.hearingtest.constants.AgeNormConstants.*;

public class PopulationNorm {
    public static Audiogram determinePopulationNorm(Boolean sex, Integer age){
        Audiogram audiogram;
        if(sex){
            if(age <= 29){
                audiogram = new Audiogram(Female_20_29, Female_20_29);
            } else if(age <= 39){
                audiogram = new Audiogram(Female_30_39, Female_30_39);
            } else if(age <= 49){
                audiogram = new Audiogram(Female_40_49, Female_40_49);
            } else{
                audiogram = new Audiogram(Female_50_59, Female_50_59);
            }
        } else{
            if(age <= 29){
                audiogram = new Audiogram(Male_20_29, Male_20_29);
            } else if(age <= 39){
                audiogram = new Audiogram(Male_30_39, Male_30_39);
            } else if(age <= 49){
                audiogram = new Audiogram(Male_40_49, Male_40_49);
            } else{
                audiogram = new Audiogram(Male_50_59, Male_50_59);
            }
        }
        return audiogram;
    }
}
