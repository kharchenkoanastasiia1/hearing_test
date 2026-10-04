package com.example.hearingtest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.fragment.GraphicFragment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GraphicAdapter extends FragmentStateAdapter {
    private final List<Audiogram> audiogram;
    private int countObject = 10;
    @Getter
    @Setter
    private int idUser = 0;

    public GraphicAdapter(FragmentActivity fragmentActivity, List<Audiogram> audio) {
        super(fragmentActivity);
        audiogram = audio;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(GraphicFragment.newInstance(audiogram, position, idUser));
    }

    @Override
    public int getItemCount() {
        return countObject;
    }

    public void setItemCount(int count){
        this.countObject = count;
    }
}
