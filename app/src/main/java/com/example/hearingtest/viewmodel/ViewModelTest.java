package com.example.hearingtest.viewmodel;

import android.media.AudioTrack;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.tone.GenerationTone;
import lombok.Getter;

public class ViewModelTest extends ViewModel {

    private Integer minVolume = -10;
    @Getter
    private Integer maxVolume = 100;

    public GenerationTone generationTone;
    public Audiogram audiogram;
    public AudioTrack track;

    private MutableLiveData<Integer> valueFrequencyLeft;
    private MutableLiveData<Integer> valueFrequencyRight;
    private MutableLiveData<Integer> valueSoundLevelLeft;
    private MutableLiveData<Integer> valueSoundLevelRight;
    private MutableLiveData<Boolean> status = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);
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
        if(!isStarted.getValue()){
            isStarted.postValue(true);
            status.postValue(true);
            audiogram = new Audiogram();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for(int j = valueFrequencyLeft.getValue(); j < Audiogram.valueFrequency.length; j++){
                        generationTone.setStereoChannel(false);     //Left
                        generationTone.setBaseFrequency(Audiogram.valueFrequency[j]);
                        valueFrequencyLeft.postValue(j);

                        for(int i = minVolume; i <= maxVolume; i += 5){
                            status.getValue();
                            try {
                                generationTone.setVolume(i);
                                valueSoundLevelLeft.postValue(i);
                                track = generationTone.generateTone();
                                track.play();
                                if(Boolean.FALSE.equals(status.getValue())){
                                    generationTone.clearMemory(track);
                                    status.postValue(true);
                                    break;
                                }
                                if(Boolean.TRUE.equals(emergencyExit.getValue())){
                                    generationTone.clearMemory(track);
                                    return;
                                }
                                Thread.sleep(generationTone.getCountMilliseconds());
                                generationTone.clearMemory(track);
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }
                        }
                        audiogram.getValueAmplitudeLeft()[j] = valueSoundLevelLeft.getValue();
                        valueSoundLevelLeft.postValue(minVolume);

                        try {
                            Thread.sleep(generationTone.getCountMilliseconds());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        generationTone.setStereoChannel(true);      //Right
                        valueFrequencyRight.postValue(j);
                        for(int i = minVolume; i <= maxVolume; i += 5){
                            status.getValue();
                            try {
                                generationTone.setVolume(i);
                                valueSoundLevelRight.postValue(i);
                                track = generationTone.generateTone();
                                track.play();
                                if(Boolean.FALSE.equals(status.getValue())){
                                    generationTone.clearMemory(track);
                                    status.postValue(true);
                                    break;
                                }
                                if(Boolean.TRUE.equals(emergencyExit.getValue())){
                                    generationTone.clearMemory(track);
                                    return;
                                }
                                Thread.sleep(generationTone.getCountMilliseconds());
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
            status.postValue(false);
        }
        return audiogram;
    }
}
