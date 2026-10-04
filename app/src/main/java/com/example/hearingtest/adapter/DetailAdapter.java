package com.example.hearingtest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.fragment.DetailGraphicFragment;


public class DetailAdapter extends FragmentStateAdapter {

    public Audiogram audiogram;
    public Audiogram median;
    public int countObject = 2;
    public Boolean typeNorm = false;

    public DetailAdapter(FragmentActivity fragmentActivity, Audiogram audio, Audiogram med, Boolean type) {
        super(fragmentActivity);
        audiogram = audio;
        median = med;
        typeNorm = type;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(DetailGraphicFragment.newInstance(audiogram, median, position, typeNorm));
    }

    @Override
    public int getItemCount() {
        return countObject;
    }

    public void setItemCount(int count){
        this.countObject = count;
    }
}
