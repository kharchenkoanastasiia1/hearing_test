package com.example.hearingtest.fragment;

import static com.example.hearingtest.constants.Constants.frequency;
import static com.example.hearingtest.constants.Constants.hearingLevel;
import static com.example.hearingtest.constants.Constants.left;
import static com.example.hearingtest.constants.Constants.right;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.hearingtest.audiogram.Audiogram;
import lombok.Setter;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;

public class GraphicFragment extends Fragment {
    public List<Audiogram> audiograms;
    public Audiogram audiogram;
    private int pageNumber;
    @Setter
    private static int idUser;
    private XYPlot plot;

    public static GraphicFragment newInstance(List<Audiogram> audio, int page, int id) {
        GraphicFragment fragment = new GraphicFragment(audio);
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        setIdUser(id);
        return fragment;
    }

    public GraphicFragment(List<Audiogram> audio) {
        audiograms = audio;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
        audiogram = audiograms.get(pageNumber);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.graphic_fragment, container, false);

        plot = (XYPlot) result.findViewById(R.id.plot);
        //Настройка осей:
        plot.setDomainBoundaries(0,7, BoundaryMode.FIXED);
        plot.setDomainStep(StepMode.SUBDIVIDE, 8);  //сетка по оси X
        plot.setDomainLabel(frequency);
        plot.setRangeBoundaries(100, -10, BoundaryMode.FIXED);
        plot.setRangeStep(StepMode.SUBDIVIDE, 12);  //сетка по оси Y
        plot.setRangeLabel(hearingLevel);

        if(audiogram.getDateRecord() != null){
            plot.setTitle(audiogram.getDateRecord().toString());
        } else {
            plot.setTitle("");
        }

        assert audiogram != null;

        // Данные для построения графика:
        Number[] domainLabels = Audiogram.valueFrequency;
        Number[] series1Numbers = audiogram.getValueAmplitudeLeft();
        Number[] series2Numbers = audiogram.getValueAmplitudeRight();

        // Создаем серии, относительно данных аудиограммы:
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, left);
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, right);

        // Создаем средства форматирования для рисования серии:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(getActivity(), R.xml.line_point_formatter_with_labels_1);
        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(getActivity(), R.xml.line_point_formatter_with_labels_2);


        // Добавление сглаживания к линиям:
//        series1Format.setInterpolationParams(
//                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//        series2Format.setInterpolationParams(
//                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

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

        return result;
    }
}
