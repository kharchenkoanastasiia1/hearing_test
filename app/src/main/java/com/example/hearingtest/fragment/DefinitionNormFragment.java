package com.example.hearingtest.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.adapter.GraphicAdapter;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.adapter.DBAdapter;
import com.example.hearingtest.median.Median;

import java.util.List;
import java.util.Objects;

public class DefinitionNormFragment extends Fragment {

    public List<Audiogram> audiogram;
    public Integer idUser;
    public Button detail;
    public ViewPager2 pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.definitionnorm_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detail = view.findViewById(R.id.buttonResultFragment);

        DBAdapter adapter = new DBAdapter(view.getContext());

        idUser = ((MainActivity) Objects.requireNonNull(getActivity())).getUserIdForMain();

        adapter.open();
        audiogram = adapter.getMedians(idUser);
        adapter.close();

        pager = view.findViewById(R.id.fragmentDefinitionNorm);
        GraphicAdapter pageAdapter = new GraphicAdapter(getActivity(), audiogram);
        pageAdapter.setItemCount(audiogram.size());
        pageAdapter.setIdUser(idUser);
        pager.setAdapter(pageAdapter);

        detail.setOnClickListener(v -> showDetail(view));
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) Objects.requireNonNull(getActivity())).startMenu();
    }

    public void showDetail(View view){
        //Toast.makeText(view.getContext(), String.valueOf(pager.getCurrentItem()), Toast.LENGTH_SHORT).show();
    }
}
