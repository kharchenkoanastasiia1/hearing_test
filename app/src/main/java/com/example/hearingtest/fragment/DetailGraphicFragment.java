package com.example.hearingtest.fragment;

import static com.example.hearingtest.constants.Constants.frequency;
import static com.example.hearingtest.constants.Constants.hearingLevel;
import static com.example.hearingtest.constants.Constants.leftEar;
import static com.example.hearingtest.constants.Constants.measurement;
import static com.example.hearingtest.constants.Constants.norm;
import static com.example.hearingtest.constants.Constants.rightEar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.hearingtest.R;
import com.example.hearingtest.analize.AnalyzeAudiogram;
import com.example.hearingtest.audiogram.Audiogram;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

public class DetailGraphicFragment extends Fragment {
    public TextView verdict;
    public ImageView imageVerdict;
    public Audiogram audiogram;
    public Audiogram median;
    private XYPlot plot;
    public int ear = 0;
    public Boolean typeNorm;

    public DetailGraphicFragment(Audiogram audio, Audiogram med, int numEar, Boolean type) {
        audiogram = audio;
        median = med;
        ear = numEar;
        typeNorm = type;
    }

    public static DetailGraphicFragment newInstance(Audiogram audio, Audiogram med, int numEar, Boolean type) {
        return new DetailGraphicFragment(audio, med, numEar, type);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.detailgraphic_fragment, container, false);

        plot = (XYPlot) result.findViewById(R.id.plotDetail);
        imageVerdict = result.findViewById(R.id.imageView);
        verdict = result.findViewById(R.id.resultText);

        //Настройка осей:
        plot.setDomainBoundaries(0,7, BoundaryMode.FIXED);
        plot.setDomainStep(StepMode.SUBDIVIDE, 8);  //сетка по оси X
        plot.setDomainLabel(frequency);
        plot.setRangeBoundaries(100, -10, BoundaryMode.FIXED);
        plot.setRangeStep(StepMode.SUBDIVIDE, 12);  //сетка по оси Y
        plot.setRangeLabel(hearingLevel);

        assert audiogram != null;
        assert median != null;

        // Данные для построения графика:
        Number[] domainLabels = Audiogram.valueFrequency;
        Number[] series1Numbers;
        Number[] series2Numbers;
        if(ear == 0){
            series1Numbers = audiogram.getValueAmplitudeLeft();
            series2Numbers = median.getValueAmplitudeLeft();
            plot.setTitle(leftEar);
        } else{
            series1Numbers = audiogram.getValueAmplitudeRight();
            series2Numbers = median.getValueAmplitudeRight();
            plot.setTitle(rightEar);
        }

        // Создаем серии, относительно данных аудиограммы:
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, measurement);
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, norm);

        // Создаем средства форматирования для рисования серии:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(getActivity(), R.xml.line_point_formatter_with_labels_median1);
        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(getActivity(), R.xml.line_point_formatter_with_labels_median2);

        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

        measurementAnalysis();

        return result;
    }

    public void measurementAnalysis(){
        AnalyzeAudiogram analyzeAudiogram = new AnalyzeAudiogram(audiogram, median, ear, typeNorm);
        StringBuilder str = analyzeAudiogram.methodAnalyzeOfNorm();
        int numIm = analyzeAudiogram.getAnimation();
        if(numIm == 0){
            imageVerdict.setImageResource(R.drawable.good_smile);
        } else if(numIm == 1){
            imageVerdict.setImageResource(R.drawable.good_smile);
        } else{
            imageVerdict.setImageResource(R.drawable.bad_smile);
        }
        verdict.setText(str);
    }
}
