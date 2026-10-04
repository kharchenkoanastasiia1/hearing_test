package com.example.hearingtest.norm;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.db.DBAdapter;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.util.List;

@Getter
@Setter
public class Median {
    private List<Audiogram> audiograms;
    private Audiogram audiogram;
    private Integer idUser;
    private final DBAdapter adapter;

    public Median(Context context, Integer id){
        idUser = id;
        adapter = new DBAdapter(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculationMedian() throws ParseException {
        setAudiogramsFromDB();
        if(audiograms.size() > 1){
            Integer[] left = new Integer[audiograms.get(0).getValueAmplitudeLeft().length];
            Integer[] right = new Integer[audiograms.get(0).getValueAmplitudeRight().length];
            Integer[][] matrixValueLeft = new Integer[audiograms.get(0).getValueAmplitudeLeft().length][audiograms.size()];
            Integer[][] matrixValueRight = new Integer[audiograms.get(0).getValueAmplitudeRight().length][audiograms.size()];

            for(int i = 0; i < audiograms.size(); i++){
                for(int j = 0; j < audiograms.get(0).getValueAmplitudeLeft().length; j++){
                    matrixValueLeft[j][i] = audiograms.get(i).getValueAmplitudeLeft()[j];
                    matrixValueRight[j][i] = audiograms.get(i).getValueAmplitudeRight()[j];
                }
            }
            sortMatrix(matrixValueLeft);
            sortMatrix(matrixValueRight);

            int index = 0;

            if((audiograms.size() % 2) == 0){
                index = audiograms.size() / 2 - 1;
                for(int i = 0; i < left.length; i++){
                    left[i] = (matrixValueLeft[i][index] + matrixValueLeft[i][index+1]) / 2;
                    right[i] = (matrixValueRight[i][index] + matrixValueRight[i][index+1]) / 2;
                }
            } else{
                index = (int) Math.floor(audiograms.size() / 2);
                for(int i = 0; i < left.length; i++){
                    left[i] = matrixValueLeft[i][index];
                    right[i] = matrixValueRight[i][index];
                }
            }

            audiogram = new Audiogram(left, right, idUser);
        } else{
            audiogram = audiograms.get(0);
        }
        setDBMedian();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAudiogramsFromDB() throws ParseException {
        adapter.open();
        audiograms = adapter.getAudiograms(idUser);
        adapter.close();
    }

    private void setDBMedian(){
        adapter.open();
        audiogram.setIdUser(idUser);
        adapter.insertMedian(audiogram);
        adapter.close();
    }

    private void sortMatrix(Integer[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            while (true){
                int status = 0;
                for(int j = 1; j < matrix[0].length; j++){
                    if(matrix[i][j] < matrix[i][j-1]){
                        int tmp = matrix[i][j-1];
                        matrix[i][j-1] = matrix[i][j];
                        matrix[i][j] = tmp;
                        status++;
                    }
                }
                if(status == 0){
                    break;
                }
            }
        }
    }
}















