package com.example.hearingtest.fragment;

import static com.example.hearingtest.referencebook.Constants.choiseUser;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.adapter.DBAdapter;
import com.example.hearingtest.referencebook.Constants;
import com.example.hearingtest.users.User;
import com.example.hearingtest.users.UserCollection;

import java.util.Objects;

public class MenuUserFragment extends Fragment {
    Button btnCreate;
    Button btnRedact;
    Button btnConnect;
    TextView userText;
    DBAdapter adapterDB;
    UserCollection usersCollection;
    User user;
    String[] userData;
    String currentUserName;
    Boolean change = false;
    Boolean status = false;

    public MenuUserFragment(User userMain){
        user = userMain;
        currentUserName = userMain.getNameUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menuuser_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapterDB = new DBAdapter(view.getContext());

        btnCreate = view.findViewById(R.id.createUser);
        btnRedact = view.findViewById(R.id.redactUserButton);
        btnConnect = view.findViewById(R.id.connectToServer);
        userText = view.findViewById(R.id.textView5);

        fillUsersData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, userData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerUsers);
        spinner.setAdapter(adapter);

        userText.setText(Constants.user + currentUserName);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(change){
                    if(position != 0){
                        user = usersCollection.getUsers().get(position-1);
                        SharedPreferences pref = ((MainActivity) Objects.requireNonNull(getActivity())).getPreferences(((MainActivity) getActivity()).MODE_PRIVATE);
                        SharedPreferences.Editor prefEditor = pref.edit();
                        prefEditor.putInt("ID", user.getIdUser());
                        prefEditor.putString("NAME", user.getNameUser());
                        prefEditor.apply();
                        userText.setText(Constants.user + user.getNameUser());
                        ((MainActivity) Objects.requireNonNull(getActivity())).setUserData(user);
                    }
                }
                change = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        btnCreate.setOnClickListener(v -> onClickCreate());
        btnRedact.setOnClickListener(v -> onClickRedact(view));
        btnConnect.setOnClickListener(v -> onClickConnect());
    }

    public void onClickCreate(){
        status = true;
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenuUser(user, 1);
    }

    public void onClickRedact(View v){
        status = true;
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenuUser(user, 2);
    }

    public void onClickConnect(){
        status = true;
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenuUser(user, 3);
    }

    public void fillUsersData(){
        adapterDB.open();
        usersCollection = adapterDB.getUsers();
        adapterDB.close();

        userData = new String[usersCollection.users.size()+1];
        userData[0] = choiseUser;
        for(int i = 0; i < usersCollection.users.size(); i++ ){
            userData[i+1] = usersCollection.getUsers().get(i).getNameUser();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!status){
            ((MainActivity) Objects.requireNonNull(getActivity())).startMenu();
        }
    }
}














