package com.example.hearingtest.viewmodel;

import android.media.AudioTrack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.tone.GenerationTone;
import lombok.Getter;
import lombok.Setter;

public class ViewModelTestAdvanced extends ViewModel {
    private final Integer minVolume = 0;
    private final Integer maxVolume = 100;
    private GenerationTone generationTone;
    private Audiogram audiogram;
    private AudioTrack track;

    private MutableLiveData<Integer> valueFrequencyLeft;
    private MutableLiveData<Integer> valueFrequencyRight;
    private MutableLiveData<Integer> valueSoundLevelLeft;
    private MutableLiveData<Integer> valueSoundLevelRight;

    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> statusHear = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> statusNotHear = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> isEnd = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> emergencyExit = new MutableLiveData<>(false);

    public Audiogram execute(){
        if(Boolean.FALSE.equals(isStarted.getValue())){
            emergencyExit.postValue(false);
            isStarted.postValue(true);
            statusHear.postValue(true);
            generationTone = new GenerationTone();
            audiogram = new Audiogram();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for(int j = 0; j < Audiogram.valueFrequency.length; j++){
                        generationTone.setBaseFrequency(Audiogram.valueFrequency[j]);
                        generationTone.setCountMilliseconds(300000);

                        // Left:
                        generationTone.setStereoChannel(true);
                        valueFrequencyLeft.postValue(j);
                        if (frequencyScan(valueSoundLevelLeft))
                            return;
                        audiogram.getValueAmplitudeLeft()[j] = valueSoundLevelLeft.getValue();
                        valueSoundLevelLeft.postValue(minVolume);

                        // Задержка 1000 мс между ушами с проверкой аварийного выхода каждые 50 мс
                        long pauseElapsed = 0;
                        while (pauseElapsed < 1000) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            pauseElapsed += 50;
                            if (Boolean.TRUE.equals(emergencyExit.getValue())) {
                                return; // Выход во время паузы
                            }
                        }

                        // Right:
                        generationTone.setStereoChannel(false);
                        valueFrequencyRight.postValue(j);
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
        } else {    //если процесс тестирования запущен,
            statusHear.postValue(false);    //пользователь нажал кнопку "Слышу"
        }
        return audiogram;
    }

    private boolean frequencyScan(MutableLiveData<Integer> valueSoundLevel) {
        for (int i = minVolume; i <= maxVolume; ) {
            try {
                generationTone.setVolume(i);
                valueSoundLevel.postValue(i);

                // Генерируем и запускаем тон
                track = generationTone.generateTone();
                if (track != null && track.getState() == AudioTrack.STATE_INITIALIZED) {
                    track.play();
                }

                long totalSleepTime = 1000; // Играем тон максимум 1 секунду
                long elapsed = 0;

                boolean clickedNotHear = false;
                boolean clickedHear = false;

                // Цикл быстрого опроса кнопок (каждые 50 мс)
                while (elapsed < totalSleepTime) {
                    Thread.sleep(50);
                    elapsed += 50;

                    // Проверяем экстренный выход
                    if (Boolean.TRUE.equals(emergencyExit.getValue())) {
                        generationTone.clearMemory(track);
                        return true;
                    }

                    // Проверяем кнопку "Не слышу"
                    if (Boolean.FALSE.equals(statusNotHear.getValue())) {
                        clickedNotHear = true;
                        break;
                    }

                    // Проверяем кнопку "Слышу"
                    if (Boolean.FALSE.equals(statusHear.getValue())) {
                        clickedHear = true;
                        break;
                    }
                }

                // Звук отыграл (или прерван кнопкой) — очищаем
                generationTone.clearMemory(track);

                // Обработка логики после остановки звука
                if (clickedNotHear) {
                    statusNotHear.postValue(true); // Возвращаем флаг в исходное состояние
                    i += 1;                       // Увеличиваем громкость
                    continue;                     // Переходим к следующей итерации цикла
                }

                if (clickedHear) {
                    statusHear.postValue(true);   // Возвращаем флаг в исходное состояние
                    break;                        // Выходим из цикла и фиксируем результат
                }

                // Если за 1 секунду никто ничего не нажал, просто идем на следующую итерацию
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    //Проверяем, происходит ли еще тестирование
    public LiveData<Boolean> getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean statusEnd) {
        isEnd.postValue(statusEnd);
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

    public void setStatusNotHear(Boolean status) {
        statusNotHear.postValue(status);
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
