package com.example.hearingtest.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.db.DBAdapter;

import java.util.Objects;

public class DeleteDialogFragment extends DialogFragment {

    private DBAdapter adapter;
    private int idAudiogram;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        adapter = new DBAdapter(context);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        assert getArguments() != null;
        idAudiogram = getArguments().getInt("idAudio");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(R.string.dialogWindow)
                .setIcon(R.drawable.good4)
                .setMessage(R.string.dialogDeleteAudiogram)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAudiogram(idAudiogram);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void deleteAudiogram(int idAudiogram){
        adapter.open();
        adapter.deleteAudiogram(idAudiogram);
        adapter.close();
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(2);
    }
}
