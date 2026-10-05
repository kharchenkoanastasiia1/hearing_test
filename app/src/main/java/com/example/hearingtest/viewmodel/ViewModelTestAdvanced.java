package com.example.hearingtest.viewmodel;

import android.media.AudioTrack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.tone.GenerationTone;

public class ViewModelTestAdvanced extends ViewModel {
    private Integer minVolume = 0;
    private Integer maxVolume = 100;

    private GenerationTone generationTone;
    private Audiogram audiogram;
    private AudioTrack track;

    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> statusHear = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> statusNotHear = new MutableLiveData<>(false);
    private MutableLiveData<Integer> valueFrequencyLeft;
    private MutableLiveData<Integer> valueFrequencyRight;
    private MutableLiveData<Integer> valueSoundLevelLeft;
    private MutableLiveData<Integer> valueSoundLevelRight;
    private MutableLiveData<Boolean> isEnd = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> emergencyExit = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean statusEnd) {
        isEnd.postValue(statusEnd);
    }

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

    public LiveData<Integer> getValueFrequencyLeft() {
        if (valueFrequencyLeft == null) {
            valueFrequencyLeft = new MutableLiveData<>(0);
            generationTone = new GenerationTone();
        }
        return valueFrequencyLeft;
    }

    public LiveData<Integer> getValueFrequencyRight() {
        if (valueFrequencyRight == null) {
            valueFrequencyRight = new MutableLiveData<>(0);
            generationTone = new GenerationTone();
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

    public Audiogram execute(){
        if(Boolean.FALSE.equals(isStarted.getValue())){
            isStarted.postValue(true);
            statusHear.postValue(true);
            audiogram = new Audiogram();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for(int j = valueFrequencyLeft.getValue(); j < Audiogram.valueFrequency.length; j++){
                        generationTone.setStereoChannel(false);
                        generationTone.setBaseFrequency(Audiogram.valueFrequency[j]);
                        generationTone.setCountMilliseconds(300000);
                        valueFrequencyLeft.postValue(j);

                        //Left:
                        for(int i = minVolume; i <= maxVolume; ){
                            statusHear.getValue();
                            try {
                                generationTone.setVolume(i);
                                valueSoundLevelLeft.postValue(i);
                                track = generationTone.generateTone();
                                track.play();

                                if(Boolean.FALSE.equals(statusNotHear.getValue())){
                                    generationTone.clearMemory(track);
                                    statusNotHear.postValue(true);
                                    i += 1;
                                    continue;
                                }
                                if(Boolean.FALSE.equals(statusHear.getValue())){
                                    generationTone.clearMemory(track);
                                    statusHear.postValue(true);
                                    break;
                                }
                                if(Boolean.TRUE.equals(emergencyExit.getValue())){
                                    generationTone.clearMemory(track);
                                    return;
                                }
                                Thread.sleep(1000);
                                generationTone.clearMemory(track);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        audiogram.getValueAmplitudeLeft()[j] = valueSoundLevelLeft.getValue();
                        valueSoundLevelLeft.postValue(minVolume);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //Right:
                        generationTone.setStereoChannel(true);
                        valueFrequencyRight.postValue(j);
                        for(int i = minVolume; i <= maxVolume; ){
                            statusHear.getValue();
                            try {
                                generationTone.setVolume(i);
                                valueSoundLevelRight.postValue(i);
                                track = generationTone.generateTone();
                                track.play();

                                if(Boolean.FALSE.equals(statusNotHear.getValue())){
                                    generationTone.clearMemory(track);
                                    statusNotHear.postValue(true);
                                    i += 1;
                                    continue;
                                }
                                if(Boolean.FALSE.equals(statusHear.getValue())){
                                    generationTone.clearMemory(track);
                                    statusHear.postValue(true);
                                    break;
                                }
                                if(Boolean.TRUE.equals(emergencyExit.getValue())){
                                    generationTone.clearMemory(track);
                                    return;
                                }
                                Thread.sleep(1000);
                                generationTone.clearMemory(track);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        audiogram.getValueAmplitudeRight()[j] = valueSoundLevelRight.getValue();
                        valueSoundLevelRight.postValue(minVolume);
                    }
                    isEnd.postValue(true);
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        } else{
            statusHear.postValue(false);
        }
        return audiogram;
    }
}
