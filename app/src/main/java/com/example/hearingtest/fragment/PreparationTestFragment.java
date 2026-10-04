package com.example.hearingtest.fragment;

import static com.example.hearingtest.referencebook.Constants.onHeadset;
import static com.example.hearingtest.referencebook.Constants.onMaxVolume;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.viewmodel.ViewModelPreparation;
import java.util.Objects;

public class PreparationTestFragment extends Fragment {

    CheckBox headphones;
    CheckBox maxVolume;
    CheckBox individualNorm;
    Button startTest;
    Button startTestAdvanced;
    Boolean allowStartVolume = false;
    Boolean allowStartHeadphones = false;
    Boolean status = false;
    Boolean end = false;
    ViewModelPreparation model;
    AudioManager audioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preparationtest_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ViewModelPreparation.class);
        audioManager = ((MainActivity) Objects.requireNonNull(getActivity())).getAudioManager();

        headphones = view.findViewById(R.id.onHeadphones);
        maxVolume = view.findViewById(R.id.onMaxVolume);
        individualNorm = view.findViewById(R.id.checkBoxIndNorm);
        startTest = view.findViewById(R.id.startTesting);
        startTestAdvanced = view.findViewById(R.id.startTestingAdvanced);

        startTest.setOnClickListener(v -> onClickStartTest(view));
        startTestAdvanced.setOnClickListener(v -> onClickStartTestAdvanced(view));

        model.getAllowStartVolume().observe(getViewLifecycleOwner(), value -> {
            allowStartVolume = value;
            maxVolume.setChecked(allowStartVolume);
        });

        model.getAllowStartHeadphones().observe(getViewLifecycleOwner(), value -> {
            allowStartHeadphones = value;
            headphones.setChecked(allowStartHeadphones);
        });

        check();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickStartTest(View view){
        if(allowStartHeadphones && allowStartVolume){   //if(allowStartVolume){
            model.setIsStarted(true);
            end = true;
            model.restart();
            ((MainActivity) Objects.requireNonNull(getActivity())).returnPreparationTest(individualNorm.isChecked());
        } else{
            if(!allowStartHeadphones){
                Toast.makeText(view.getContext(), onHeadset, Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(view.getContext(), onMaxVolume, Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickStartTestAdvanced(View view){
        if(allowStartHeadphones && allowStartVolume){   //if(allowStartVolume){
            model.setIsStarted(true);
            end = true;
            model.restart();
            ((MainActivity) Objects.requireNonNull(getActivity())).returnPreparationTestAdvanced(individualNorm.isChecked());
        } else{
            if(!allowStartHeadphones){
                Toast.makeText(view.getContext(), onHeadset, Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(view.getContext(), onMaxVolume, Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int volume;

                while(!model.getIsStarted().getValue()){
                    volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    model.setAllowStartVolume(volume == audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

                    AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
                    int count = 0;
                    status = false;
                    for(AudioDeviceInfo device : devices){
                        if(device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET){
                            status = true;
                            break;
                        } else if(device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO){
                            count++;
                            if(count == 2){
                                status = true;
                                break;
                            }
                        } else if(device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP){
                            count++;
                            if(count == 2){
                                status = true;
                                break;
                            }
                        }
                    }
                    model.setAllowStartHeadphones(status);
                }
            }};
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!end){
            model.restart();
            ((MainActivity) Objects.requireNonNull(getActivity())).startMenu();
        }
    }
}















