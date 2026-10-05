package com.example.hearingtest.fragment;
import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.constants.Constants;
import com.example.hearingtest.viewmodel.ViewModelTest;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.util.Objects;

public class TestFragment extends Fragment {
    private Audiogram audiogram;
    private ViewModelTest model;
    private Boolean endTest = false;
    private Boolean start = false;
    private Button btnFetch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ViewModelTest.class);

        ProgressBar indicatorBarLeftFrequency = (ProgressBar) view.findViewById(R.id.progressBar);
        indicatorBarLeftFrequency.setMax(Audiogram.valueFrequency.length);

        ProgressBar indicatorBarRightFrequency = (ProgressBar) view.findViewById(R.id.progressBar4);
        indicatorBarRightFrequency.setMax(Audiogram.valueFrequency.length);

        TextView statusView = (TextView) view.findViewById(R.id.textView);

        btnFetch = (Button)view.findViewById(R.id.button6);

        ProgressBar indicatorBarLeft = (ProgressBar) view.findViewById(R.id.progressBar3);
        indicatorBarLeft.setMin(model.getMinVolume() + 10);
        indicatorBarLeft.setMax(model.getMaxVolume() + 20);

        ProgressBar indicatorBarRight = (ProgressBar) view.findViewById(R.id.progressBar5);
        indicatorBarRight.setMin(model.getMinVolume() + 10);
        indicatorBarRight.setMax(model.getMaxVolume() + 20);

        model.getValueFrequencyLeft().observe(getViewLifecycleOwner(), valueFrequency -> {
            if(start){
                indicatorBarLeftFrequency.setProgress(valueFrequency + 1);
                statusView.setText(Constants.soundInLeftEar  + Audiogram.valueFrequency[valueFrequency] + Constants.hz);
            }
        });
        model.getValueFrequencyRight().observe(getViewLifecycleOwner(), valueFrequency -> {
            if(start){
                indicatorBarRightFrequency.setProgress(valueFrequency + 1);
                statusView.setText(Constants.soundInRightEar + Audiogram.valueFrequency[valueFrequency] + Constants.hz);
            }
        });
        //indicatorBarLeft.setProgress(valueVolume + 20);
        model.getValueSoundLevelLeft().observe(getViewLifecycleOwner(), indicatorBarLeft::setProgress);
        //            indicatorBarRight.setProgress(valueVolume + 20);
        model.getValueSoundLevelRight().observe(getViewLifecycleOwner(), indicatorBarRight::setProgress);

        model.getIsEnd().observe(getViewLifecycleOwner(), end -> {
            if(end){
                endTest = true;
                model.setEmergencyExit(false);
                model.setIsEnd(false);
                try {
                    ((MainActivity) Objects.requireNonNull(getActivity())).returnResultTest(audiogram);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnFetch.setOnClickListener(v -> { beginTest(); audiogram = model.execute(); } );
    }

    private void beginTest(){
        if(!start){
            btnFetch.setText(R.string.hear);
            start = true;
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if(!endTest){
//            model.setEmergencyExit(true);
//            model.setIsEnd(false);
//            ((MainActivity) Objects.requireNonNull(getActivity())).startMenu();
//        }
//    }
}
