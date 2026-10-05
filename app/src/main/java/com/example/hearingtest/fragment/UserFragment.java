package com.example.hearingtest.fragment;

import static com.example.hearingtest.constants.Constants.fillAllFields;
import static com.example.hearingtest.constants.Constants.nicknameUnique;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.db.DBAdapter;
import com.example.hearingtest.users.User;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class UserFragment extends Fragment {

    private User user;
    private Integer[] data;
    private Boolean choice = null;
    private Integer age;
    private EditText name;
    private DBAdapter adapterDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = new User();
        adapterDB = new DBAdapter(view.getContext());

        data = new Integer[100];
        for(int i = 0; i < 100; i++){
            data[i] = i+1;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        int userId = ((MainActivity) Objects.requireNonNull(getActivity())).getSelectedUserId();

        name = view.findViewById(R.id.nameBox);
        RadioButton male = view.findViewById(R.id.sexMale);
        RadioButton female = view.findViewById(R.id.sexFemale);
        Button save = view.findViewById(R.id.saveUserData);
        RadioGroup choiceSex = view.findViewById(R.id.radioGroupSexChoise);

        Spinner spinner = (Spinner) view.findViewById(R.id.ageBox);
        spinner.setAdapter(adapter);

        if(userId > 0){
            adapterDB.open();
            user = adapterDB.getUser(userId);
            choice = user.getSexUser();
            adapterDB.close();
            name.setText(user.getNameUser());
            spinner.setSelection(user.getAgeUser() - 1);
            if(user.getSexUser()){
                female.setChecked(true);
            } else{
                male.setChecked(true);
            }
        } else{
            spinner.setSelection(30);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                age = data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        save.setOnClickListener(v -> onClick(view));
        choiceSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch(id) {
                    case R.id.sexMale:
                        choice = false;
                        break;
                    case R.id.sexFemale:
                        choice = true;
                        break;
                    default:
                        break;
                }
            }});
    }


    private void onClick(View v){
        if(name.getText().toString().isEmpty() || choice == null){
            Toast.makeText(v.getContext(), fillAllFields, Toast.LENGTH_SHORT).show();
        } else{
            SharedPreferences pref = ((MainActivity) Objects.requireNonNull(getActivity()))
                    .getPreferences(((MainActivity) getActivity()).MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();
            adapterDB.open();
            user.setNameUser(name.getText().toString());
            user.setAgeUser(age);
            user.setSexUser(choice);
            if(user.getIdUser() == -1){
                if(adapterDB.getIDUser(user.getNameUser()) == -1){
                    long id = adapterDB.insertUser(user);
                    user.setIdUser((int)id);
                    prefEditor.putInt("ID", user.getIdUser());
                    prefEditor.putString("NAME", user.getNameUser());
                    prefEditor.apply();
                    ((MainActivity) Objects.requireNonNull(getActivity())).returnDataUser(user);
                } else{
                    Toast.makeText(v.getContext(), nicknameUnique, Toast.LENGTH_SHORT).show();
                }
            } else{
                adapterDB.updateUser(user);
                prefEditor.putInt("ID", user.getIdUser());
                prefEditor.putString("NAME", user.getNameUser());
                prefEditor.apply();
                ((MainActivity) Objects.requireNonNull(getActivity())).returnDataUser(user);
            }

            adapterDB.close();
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(3);
//    }
}



















