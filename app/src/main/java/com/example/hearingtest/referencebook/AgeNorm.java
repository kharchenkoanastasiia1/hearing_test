package com.example.hearingtest.referencebook;

import com.example.hearingtest.audiogram.Audiogram;

public class AgeNorm {
    public static Integer[] Male_20_29 = new Integer[]{5, 5, 5, 5, 10, 10, 10, 8};
    public static Integer[] Male_30_39 = new Integer[]{5, 5, 5, 7, 7, 13, 15, 17};
    public static Integer[] Male_40_49 = new Integer[]{10, 10, 10, 9, 14, 31, 28, 33};
    public static Integer[] Male_50_59 = new Integer[]{10, 10, 15, 16, 27, 41, 42, 45};
    public static Integer[] Female_20_29 = new Integer[]{5, 5, 5, 5, 5, 5, 6, 5};
    public static Integer[] Female_30_39 = new Integer[]{5, 5, 5, 8, 9, 13, 13, 15};
    public static Integer[] Female_40_49 = new Integer[]{5, 10, 5, 10, 11, 25, 25, 23};
    public static Integer[] Female_50_59 = new Integer[]{10, 10, 15, 18, 20, 30, 31, 37};
    public Audiogram audiogram;

    public AgeNorm(Boolean sex, Integer age){
        if(sex){
            if(age <= 29){
                audiogram = new Audiogram(Female_20_29, Female_20_29);
            } else if(age > 29 && age <= 39){
                audiogram = new Audiogram(Female_30_39, Female_30_39);
            } else if(age > 39 && age <= 49){
                audiogram = new Audiogram(Female_40_49, Female_40_49);
            } else{
                audiogram = new Audiogram(Female_50_59, Female_50_59);
            }
        } else{
            if(age <= 29){
                audiogram = new Audiogram(Male_20_29, Male_20_29);
            } else if(age > 29 && age <= 39){
                audiogram = new Audiogram(Male_30_39, Male_30_39);
            } else if(age > 39 && age <= 49){
                audiogram = new Audiogram(Male_40_49, Male_40_49);
            } else{
                audiogram = new Audiogram(Male_50_59, Male_50_59);
            }
        }
    }
}
