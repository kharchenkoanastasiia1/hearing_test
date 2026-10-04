package com.example.hearingtest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.fragment.DetailGraphicFragment;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetailAdapter extends FragmentStateAdapter {

    private Audiogram audiogram;
    private Audiogram median;
    private int countObject = 2;
    private Boolean typeNorm;

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
}
