package com.example.hearingtest.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.adapter.DetailAdapter;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.constants.AgeNormConstants;
import com.example.hearingtest.norm.PopulationNorm;

import java.util.Objects;

public class DetailFragment extends Fragment {

    public Audiogram audiogram;
    public Audiogram median;
    public Button personNorm;
    public Button populNorm;
    public Audiogram populationNorm;

    public DetailFragment(Audiogram audiog, Audiogram med, Audiogram ageNorm){
        audiogram = audiog;
        median = med;
        populationNorm = ageNorm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        personNorm = view.findViewById(R.id.buttonPersonNorm);
        populNorm = view.findViewById(R.id.buttonPopulNorm);

        personNorm.setEnabled(false);

        choiseNorm(view, populationNorm, false);

        personNorm.setOnClickListener(v -> choiseNorm(view, median, true));
        populNorm.setOnClickListener(v -> choiseNorm(view, populationNorm, false));
    }

    public void choiseNorm(View view, Audiogram audio, Boolean type){
        if(audio != null){
            ViewPager2 pager = view.findViewById(R.id.fragmentDetail);
            DetailAdapter pageAdapter = new DetailAdapter(getActivity(), audiogram, audio, type);
            pager.setAdapter(pageAdapter);
            if(type){
                populNorm.setEnabled(true);
                populNorm.setBackgroundColor(Color.parseColor("#FF6200EE"));
                populNorm.setTextColor(Color.parseColor("#FFFFFFFF"));
                personNorm.setBackgroundColor(Color.parseColor("#21ff0d"));
                personNorm.setTextColor(Color.parseColor("#FFFFFFFF"));
                personNorm.setEnabled(false);
            } else{
                personNorm.setEnabled(true);
                personNorm.setBackgroundColor(Color.parseColor("#FF6200EE"));
                personNorm.setTextColor(Color.parseColor("#FFFFFFFF"));
                populNorm.setBackgroundColor(Color.parseColor("#21ff0d"));
                populNorm.setTextColor(Color.parseColor("#FFFFFFFF"));
                populNorm.setEnabled(false);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(2);
    }
}
