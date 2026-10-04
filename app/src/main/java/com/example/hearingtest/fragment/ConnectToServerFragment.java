package com.example.hearingtest.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.hearingtest.MainActivity;
import com.example.hearingtest.R;
import com.example.hearingtest.db.DBAdapter;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.audiogram.SyncOfflineAudiogramsUseCase;
import com.example.hearingtest.audiogram.AudiogramForRemoteDB;
import com.example.hearingtest.server.JavaScriptInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ConnectToServerFragment extends Fragment {
    private MutableLiveData<Date> lastDateAudiogram = new MutableLiveData<>(null);
    private MutableLiveData<Date> lastDateMedian = new MutableLiveData<>(null);
    private MutableLiveData<String> usernameServer = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> statusResponse = new MutableLiveData<>(false);
    private WebView webView;
    private List<Audiogram> audiograms;
    private List<AudiogramForRemoteDB> audiogramForRemoteDBS;
    private Audiogram median;
    private AudiogramForRemoteDB medianForServer;
    private JavaScriptInterface javaScriptInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.connect_to_server_fragment, container, false);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        javaScriptInterface = new JavaScriptInterface((MainActivity) Objects.requireNonNull(getActivity()));

        connectToDatabase(view);

        Button btnDisconnect = view.findViewById(R.id.buttonDisconnect);
        webView = view.findViewById(R.id.webView);
        //TODO: URL
        webView.loadUrl("http://192.168.1.101:8080/hearing_monitoring/login");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()
        {
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:selectDateExamination()");
            }
        });

        webView.addJavascriptInterface(javaScriptInterface, "AndroidFunction");

        try {
            execute();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        btnDisconnect.setOnClickListener(v -> onClickReturnMenu());
    }

    public void onClickReturnMenu(){
        ((MainActivity) Objects.requireNonNull(getActivity())).returnMenu(3);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void connectToDatabase(View view){
        DBAdapter adapter = new DBAdapter(view.getContext());
        int idUser = ((MainActivity) Objects.requireNonNull(getActivity())).getUserIdForMain();
        adapter.open();
        try {
            audiograms = adapter.getAudiograms(idUser);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        median = adapter.getLastRowMedians(idUser);
        adapter.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void transformationAudiogram() throws ParseException, JsonProcessingException {
        List<Audiogram> newList = SyncOfflineAudiogramsUseCase.selectAudiogramCollection(audiograms, lastDateAudiogram.getValue());
        if(newList != null){
            audiogramForRemoteDBS = new ArrayList<>();
            for(Audiogram audio : newList){
                audiogramForRemoteDBS.add(new AudiogramForRemoteDB(audio, usernameServer.getValue()));
            }
        }
        if(median != null && SyncOfflineAudiogramsUseCase.selectMedian(median, lastDateMedian.getValue()) != null){
            medianForServer = new AudiogramForRemoteDB(median, usernameServer.getValue());
        }
    }

    public void execute() throws JsonProcessingException {
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while(lastDateAudiogram.getValue() == null || lastDateMedian.getValue() == null || usernameServer.getValue() == null){
                    try {
                        Thread.sleep(500);
                        if(javaScriptInterface.getLastDateAudiogram() != null){
                            lastDateAudiogram.postValue(javaScriptInterface.getLastDateAudiogram());
                        }
                        if(javaScriptInterface.getLastDateMedian() != null){
                            lastDateMedian.postValue(javaScriptInterface.getLastDateMedian());
                        }
                        if(javaScriptInterface.getUsername() != null){
                            usernameServer.postValue(javaScriptInterface.getUsername());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    transformationAudiogram();
                    statusResponse.postValue(true);
                    if(audiogramForRemoteDBS != null){
                        webView.post(() -> {
                            ObjectMapper objectMapper = new ObjectMapper();
                            String str= null;
                            try {
                                str = objectMapper.writeValueAsString(audiogramForRemoteDBS);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            webView.evaluateJavascript("javascript:addAudiogram('" + str + "')", null);
                        });
                    }

                    if(medianForServer != null){
                        webView.post(() -> {
                            ObjectMapper objectMapper = new ObjectMapper();
                            String str= null;
                            try {
                                str = objectMapper.writeValueAsString(medianForServer);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            webView.evaluateJavascript("javascript:addMedian('" + str + "')", null);
                        });
                    }

                } catch (ParseException | JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
