package com.example.hearingtest.server;
import android.os.Build;

import androidx.annotation.RequiresApi;
import com.example.hearingtest.audiogram.Audiogram;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SyncOfflineAudiogramsUseCase {

    //Если отсутствует подключение, проверить коллекцию аудиограмм пользователя на наличие
    // новых записей после даты последней записи в БД
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Audiogram> selectAudiogramCollection(List<Audiogram> audiograms, Date lastDate){
        LocalDate searchDate = convertToLocalDateViaInstant(lastDate);
        return audiograms.stream()
                .filter(s->(s.getDateRecord().isAfter(searchDate) && !s.getDateRecord().isEqual(searchDate)))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Audiogram selectMedian(Audiogram audiogram, Date lastDate){
        LocalDate searchDate = convertToLocalDateViaInstant(lastDate);
        if(audiogram.getDateRecord().equals(searchDate)){
            return null;
        }
        return audiogram;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
