package com.example.hearingtest.viewmodel;

import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.tone.GenerationTone;
import lombok.Getter;
import lombok.Setter;


public class ViewModelTest extends ViewModel {

    @Getter
    private final Integer minVolume = 0;
    @Getter
    private final Integer maxVolume = 100;
    private GenerationTone generationTone;
    private Audiogram audiogram;
    private AudioTrack track;

    private MutableLiveData<Integer> valueFrequencyLeft;
    private MutableLiveData<Integer> valueFrequencyRight;
    private MutableLiveData<Integer> valueSoundLevelLeft;
    private MutableLiveData<Integer> valueSoundLevelRight;

    private MutableLiveData<Boolean> status = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isEnd = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> emergencyExit = new MutableLiveData<>(false);

    public Audiogram execute(){
        if(Boolean.FALSE.equals(isStarted.getValue())){
            emergencyExit.postValue(false);
            isStarted.postValue(true);
            status.postValue(true);
            generationTone = new GenerationTone();
            audiogram = new Audiogram();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for(int j = 0; j < Audiogram.valueFrequency.length; j++){
                        generationTone.setBaseFrequency(Audiogram.valueFrequency[j]);

                        //Left:
                        generationTone.setStereoChannel(true);
                        valueFrequencyLeft.postValue(j);    //для отображения частот для UI
                        if (frequencyScan(valueSoundLevelLeft))
                            return;
                        audiogram.getValueAmplitudeLeft()[j] = valueSoundLevelLeft.getValue();  //фиксируем уровень амплитуды для частоты
                        valueSoundLevelLeft.postValue(minVolume); //возвращаем "уровень громкости" на минимальный

                        try {
                            Thread.sleep(generationTone.getCountMilliseconds());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        //Right:
                        generationTone.setStereoChannel(false);
                        valueFrequencyRight.postValue(j);   //для отображения частот для UI
                        if (frequencyScan(valueSoundLevelRight))
                            return;
                        audiogram.getValueAmplitudeRight()[j] = valueSoundLevelRight.getValue();
                        valueSoundLevelRight.postValue(minVolume);
                    }
                    isEnd.postValue(true);
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        } else{ //если процесс тестирования запущен,
            status.postValue(false);    //пользователь нажал кнопку "Слышу"
        }
        return audiogram;
    }


    private boolean frequencyScan(MutableLiveData<Integer> valueSoundLevel) {
        for (int i = minVolume; i <= maxVolume; i += 5) {
            try {
                generationTone.setVolume(i);
                valueSoundLevel.postValue(i);   // текущий уровень громкости для демонстрации UI

                // Генерируем и запускаем тон
                track = generationTone.generateTone();
                if (track != null && track.getState() == AudioTrack.STATE_INITIALIZED) {
                    track.play();
                }

                // делим время на мелкие отрезки по 50 мс
                long totalSleepTime = generationTone.getCountMilliseconds();
                long elapsed = 0;
                boolean clickedHear = false;

                while (elapsed < totalSleepTime) {
                    Thread.sleep(50);
                    elapsed += 50;

                    // Проверяем кнопку "Слышу" (статус стал false)
                    if (Boolean.FALSE.equals(status.getValue())) {
                        clickedHear = true;
                        break;
                    }

                    // Проверяем экстренный выход
                    if (Boolean.TRUE.equals(emergencyExit.getValue())) {
                        generationTone.clearMemory(track);
                        return true;
                    }
                }

                // Чистим память сразу после выхода из ожидания
                assert track != null;
                generationTone.clearMemory(track);

                if (clickedHear) {
                    status.postValue(true); // Возвращаем статус в исходное состояние для следующего шага
                    break; // переходим к следующей частоте/уху
                }

            } catch (InterruptedException e) {
                if (track != null) {
                    generationTone.clearMemory(track);
                }
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    //Возвращаем данные в исходное состояние
    public void setEmergencyExit(Boolean end) {
        isStarted.postValue(false);
        emergencyExit.postValue(end);
        valueFrequencyLeft.postValue(0);
        valueFrequencyRight.postValue(0);
        valueSoundLevelLeft.postValue(minVolume);
        valueSoundLevelRight.postValue(minVolume);
    }

    //Проверяем, происходит ли еще тестирование
    public LiveData<Boolean> getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean statusEnd) {
        isEnd.postValue(statusEnd);
    }

    //Геттеры для UI:
    public LiveData<Integer> getValueFrequencyLeft() {
        if (valueFrequencyLeft == null) {
            valueFrequencyLeft = new MutableLiveData<>(0);
        }
        return valueFrequencyLeft;
    }

    public LiveData<Integer> getValueFrequencyRight() {
        if (valueFrequencyRight == null) {
            valueFrequencyRight = new MutableLiveData<>(0);
        }
        return valueFrequencyRight;
    }

    public LiveData<Integer> getValueSoundLevelLeft() {
        if (valueSoundLevelLeft == null) {
            valueSoundLevelLeft = new MutableLiveData<>(minVolume);
        }
        return valueSoundLevelLeft;
    }

    public LiveData<Integer> getValueSoundLevelRight() {
        if (valueSoundLevelRight == null) {
            valueSoundLevelRight = new MutableLiveData<>(minVolume);
        }
        return valueSoundLevelRight;
    }
}