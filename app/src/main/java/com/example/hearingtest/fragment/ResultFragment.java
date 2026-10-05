package com.example.hearingtest.fragment;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.db.DBAdapter;
import com.example.hearingtest.adapter.GraphicAdapter;
import com.example.hearingtest.audiogram.Audiogram;

import java.util.List;
import java.util.Objects;

public class ResultFragment extends Fragment {

    private final List<Audiogram> audiograms;
    private Audiogram median;
    private Boolean status = false;

    public ResultFragment(List<Audiogram> audioCollection) {
        audiograms = audioCollection;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.result_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button detail = view.findViewById(R.id.buttonResultFragment);

        DBAdapter adapter = new DBAdapter(view.getContext());

        adapter.open();
        median = adapter.getLastRowMedians(((MainActivity) Objects.requireNonNull(getActivity())).getSelectedUserId());
        adapter.close();

        ViewPager2 pager = view.findViewById(R.id.fragmentResult);
        GraphicAdapter pageAdapter = new GraphicAdapter(getActivity(), audiograms);
        pageAdapter.setItemCount(1);
        pager.setAdapter(pageAdapter);

        detail.setOnClickListener(v -> showDetail());
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if(!status){
//            ((MainActivity) Objects.requireNonNull(getActivity())).startMenu();
//        }
//    }

    private void showDetail(){
        status = true;
        ((MainActivity) Objects.requireNonNull(getActivity())).returnResultFragment(audiograms.get(0), median);
    }
}












