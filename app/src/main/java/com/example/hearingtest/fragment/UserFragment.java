package com.example.hearingtest.fragment;

import static com.example.hearingtest.referencebook.Constants.fillAllFields;
import static com.example.hearingtest.referencebook.Constants.nicknameUnique;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.adapter.DBAdapter;
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

    User user;
    Integer[] data;
    int userId;
    Boolean choise = null;
    Integer age;
    EditText name;
    RadioGroup choiseSex;
    RadioButton male;
    RadioButton female;
    Button save;
    DBAdapter adapterDB;


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

        userId = ((MainActivity) Objects.requireNonNull(getActivity())).getUserIdForMain();

        name = view.findViewById(R.id.nameBox);
        male = view.findViewById(R.id.sexMale);
        female = view.findViewById(R.id.sexFemale);
        save = view.findViewById(R.id.saveUserData);
        choiseSex = view.findViewById(R.id.radioGroupSexChoise);

        Spinner spinner = (Spinner) view.findViewById(R.id.ageBox);
        spinner.setAdapter(adapter);

        if(userId > 0){
            adapterDB.open();
            user = adapterDB.getUser(userId);
            choise = user.getSexUser();
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
        choiseSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch(id) {
                    case R.id.sexMale:
                        choise = false;
                        break;
                    case R.id.sexFemale:
                        choise = true;
                        break;
                    default:
                        break;
                }
            }});
    }


    public void onClick(View v){
        if(name.getText().toString().equals("") || choise == null){
            Toast.makeText(v.getContext(), fillAllFields, Toast.LENGTH_SHORT).show();
        } else{
            SharedPreferences pref = ((MainActivity) Objects.requireNonNull(getActivity())).getPreferences(((MainActivity) getActivity()).MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();
            adapterDB.open();
            user.setNameUser(name.getText().toString());
            user.setAgeUser(age);
            user.setSexUser(choise);
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

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(3);
    }
}



















