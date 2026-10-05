package com.example.hearingtest.fragment;

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
import com.example.hearingtest.db.DBAdapter;
import com.example.hearingtest.adapter.GraphicAdapter;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.dialog.DeleteDialogFragment;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class AllResultsFragment extends Fragment {
    private List<Audiogram> audiograms;
    private Audiogram median;
    private ViewPager2 pager;
    private Boolean status = false;

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

        DBAdapter adapter = new DBAdapter(view.getContext());

        Button detail = view.findViewById(R.id.buttonResultFragment);
        Button delete = view.findViewById(R.id.del);

        int idUser = ((MainActivity) Objects.requireNonNull(getActivity())).getSelectedUserId();

        adapter.open();
        try {
            audiograms = adapter.getAudiograms(idUser);
            median = adapter.getLastRowMedians(idUser);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter.close();

        pager = view.findViewById(R.id.fragmentResult);
        GraphicAdapter pageAdapter = new GraphicAdapter(getActivity(), audiograms);
        pageAdapter.setItemCount(audiograms.size());
        pageAdapter.setIdUser(idUser);
        pager.setAdapter(pageAdapter);

        detail.setOnClickListener(v -> showDetail());
        delete.setOnClickListener(v -> deleteAudiogram());
    }

    private void showDetail(){
        if(!audiograms.isEmpty()){
            status = true;
            ((MainActivity) Objects.requireNonNull(getActivity())).returnAllResults(audiograms.get(pager.getCurrentItem()), median);
        }
    }

    private void deleteAudiogram(){
        if(!audiograms.isEmpty()){
            status = true;
            DeleteDialogFragment dialog = new DeleteDialogFragment();
            Bundle args = new Bundle();
            args.putInt("idAudio", audiograms.get(pager.getCurrentItem()).getIdAudiogram());
            dialog.setArguments(args);
            dialog.show(((MainActivity) Objects.requireNonNull(getActivity())).getSupportFragmentManager(), "delete");
        }
    }
}
