package com.example.hearingtest.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.constants.Constants;
import com.example.hearingtest.viewmodel.ViewModelTestAdvanced;
import java.text.ParseException;
import java.util.Objects;

public class AdvancedTestFragment extends Fragment {
    private Audiogram audiogram;
    private ViewModelTestAdvanced model;
    private Boolean start = false;
    private Button btnHear;
    private Button btnNotHear;
    private Button btnStart;
    private ImageView leftImage;
    private ImageView rightImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_advanced_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        @SuppressLint("ResourceType")
        Animation animation = AnimationUtils.loadAnimation((MainActivity) Objects.requireNonNull(getActivity()), R.xml.common_animation);

        model = new ViewModelProvider(requireActivity()).get(ViewModelTestAdvanced.class);

        TextView statusView = (TextView) view.findViewById(R.id.textViewInstruction);
        btnHear = (Button)view.findViewById(R.id.buttonAdvanceHear);
        btnNotHear = (Button)view.findViewById(R.id.buttonNoHear);
        btnStart = (Button)view.findViewById(R.id.buttonStart);
        leftImage = view.findViewById(R.id.imageLeftEar);
        rightImage = view.findViewById(R.id.imageRigtEar);

        btnHear.setVisibility(View.INVISIBLE);
        btnNotHear.setVisibility(View.INVISIBLE);
        leftImage.setVisibility(View.INVISIBLE);
        rightImage.setVisibility(View.INVISIBLE);
        statusView.setText("Для початку тестування натисніть на кнопку.");

        model.getValueFrequencyLeft().observe(getViewLifecycleOwner(), valueFrequency -> {
            if(start){
                statusView.setText(Constants.soundInLeftEar  + Audiogram.valueFrequency[valueFrequency] + Constants.hz + Constants.hearLevel
                        + model.getValueSoundLevelLeft().getValue() + Constants.dB);
                leftImage.startAnimation(animation);
                rightImage.clearAnimation();
            }
        });
        model.getValueFrequencyRight().observe(getViewLifecycleOwner(), valueFrequency -> {
            if(start){
                statusView.setText(Constants.soundInRightEar + Audiogram.valueFrequency[valueFrequency] + Constants.hz + Constants.hearLevel
                        + model.getValueSoundLevelRight().getValue() + Constants.dB);
                rightImage.startAnimation(animation);
                leftImage.clearAnimation();
            }
        });
        model.getValueSoundLevelLeft().observe(getViewLifecycleOwner(), valueVolume -> {
            if(start){
                statusView.setText(Constants.soundInLeftEar  + Audiogram.valueFrequency[model.getValueFrequencyLeft().getValue()] + Constants.hz + Constants.hearLevel
                        + valueVolume + Constants.dB);
            }
        });
        model.getValueSoundLevelRight().observe(getViewLifecycleOwner(), valueVolume -> {
            if(start){
                statusView.setText(Constants.soundInRightEar + Audiogram.valueFrequency[model.getValueFrequencyRight().getValue()] + Constants.hz + Constants.hearLevel
                        + valueVolume + Constants.dB);
            }
        });

        model.getIsEnd().observe(getViewLifecycleOwner(), end -> {
            if(end){
                model.setEmergencyExit(false);
                model.setIsEnd(false);
                try {
                    ((MainActivity) Objects.requireNonNull(getActivity())).returnResultTestAdvanced(audiogram);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnStart.setOnClickListener(v -> { beginTest(); audiogram = model.execute(); } );
        btnHear.setOnClickListener(v -> audiogram = model.execute());
        btnNotHear.setOnClickListener(v -> model.setStatusNotHear(false));
    }

    private void beginTest(){
        if(!start){
            btnHear.setVisibility(View.VISIBLE);
            btnNotHear.setVisibility(View.VISIBLE);
            leftImage.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.INVISIBLE);
            start = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        model.setEmergencyExit(true);
    }
}
