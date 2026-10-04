package com.example.hearingtest.fragment;

import static com.example.hearingtest.referencebook.Constants.choiseAction;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.users.User;

import java.util.Objects;

public class MenuFragment extends Fragment {

    User user;
    TextView nickName;
    Button btnTest;
    Button btnLook;
    Button btnRedact;

    public MenuFragment(User userData){
        user = userData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nickName = view.findViewById(R.id.nickName);
        btnTest = view.findViewById(R.id.startTest);
        btnLook = view.findViewById(R.id.lookResult);
        btnRedact = view.findViewById(R.id.redactDataUser);

        nickName.setText(choiseAction + user.getNameUser() + " ;)");

        btnTest.setOnClickListener(v -> onClickTest());
        btnLook.setOnClickListener(v -> onClickLookResult());
        btnRedact.setOnClickListener(v -> onClickRedact());
    }

    public void onClickTest(){
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(1);
    }

    public void onClickLookResult(){
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(2);
    }

    public void onClickRedact(){
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(3);
    }
}
























