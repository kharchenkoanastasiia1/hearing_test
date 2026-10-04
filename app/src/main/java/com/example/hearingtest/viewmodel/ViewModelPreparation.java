package com.example.hearingtest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelPreparation extends ViewModel {
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> allowStartVolume = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> allowStartHeadphones = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsStarted() {
        return isStarted;
    }

    public LiveData<Boolean> getAllowStartVolume() {
        return allowStartVolume;
    }

    public LiveData<Boolean> getAllowStartHeadphones() {
        return allowStartHeadphones;
    }

    public void setIsStarted(Boolean end) {
        isStarted.postValue(end);
    }

    public void setAllowStartVolume(Boolean end) {
        allowStartVolume.postValue(end);
    }

    public void setAllowStartHeadphones(Boolean end) {
        allowStartHeadphones.postValue(end);
    }

    public void restart(){
        setIsStarted(false);
        setAllowStartVolume(false);
        setAllowStartHeadphones(false);
    }
}













