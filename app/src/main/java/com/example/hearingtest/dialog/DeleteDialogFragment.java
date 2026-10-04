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
import com.example.hearingtest.adapter.DBAdapter;

import java.util.Objects;

public class DeleteDialogFragment extends DialogFragment {

    public DBAdapter adapter;
    public int idAudiogram;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        adapter = new DBAdapter(context);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        idAudiogram = getArguments().getInt("idAudio");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(R.string.dialogWindow)
                .setIcon(android.R.drawable.ic_dialog_alert)
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

    public void deleteAudiogram(int idAudiogram){
        adapter.open();
        adapter.deleteAudiogram(idAudiogram);
        adapter.close();
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(2);
    }
}
