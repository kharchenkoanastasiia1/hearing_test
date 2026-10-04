package com.example.hearingtest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelPager extends ViewModel {
    private MutableLiveData<Integer> numberPage = new MutableLiveData<>(0);

    public LiveData<Integer> getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(Integer page) {
        numberPage.postValue(page);
    }
}
