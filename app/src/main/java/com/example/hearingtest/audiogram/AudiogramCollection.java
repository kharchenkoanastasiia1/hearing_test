package com.example.hearingtest.audiogram;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AudiogramCollection {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Audiogram> selectAudiogramCollection(List<Audiogram> audiograms, Date lastDate){
        LocalDate searchDate = convertToLocalDateViaInstant(lastDate);
        return audiograms.stream()
                .filter(s->(s.getDateRecorder().isAfter(searchDate) && !s.getDateRecorder().isEqual(searchDate)))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Audiogram selectMedian(Audiogram audiogram, Date lastDate){
        LocalDate searchDate = convertToLocalDateViaInstant(lastDate);
        if(audiogram.getDateRecorder().equals(searchDate)){
            return null;
        }
        return audiogram;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
