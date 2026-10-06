package com.example.hearingtest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.db.DBAdapter;
import com.example.hearingtest.fragment.AllResultsFragment;
import com.example.hearingtest.fragment.DetailFragment;
import com.example.hearingtest.fragment.AdvancedTestFragment;
import com.example.hearingtest.fragment.ConnectToServerFragment;
import com.example.hearingtest.fragment.MenuFragment;
import com.example.hearingtest.fragment.MenuUserFragment;
import com.example.hearingtest.fragment.PreparationTestFragment;
import com.example.hearingtest.fragment.ResultFragment;
import com.example.hearingtest.fragment.TestFragment;
import com.example.hearingtest.fragment.UserFragment;
import com.example.hearingtest.norm.Median;
import com.example.hearingtest.norm.PopulationNorm;
import com.example.hearingtest.users.User;
import lombok.Getter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private DBAdapter db;
    private UserFragment userFragment;
    private DetailFragment detailFragment;
    private ResultFragment resultFragment;
    private User user;
    private int userId;
    @Getter
    private AudioManager audioManager;
    private Boolean calculateMedian = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        db = new DBAdapter(this);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        userId = preferences.getInt("ID", -1);
        user = new User();

        if (userId != -1) {
            db.open();
            try {
                user = db.getUser(userId);
            } finally {
                db.close();
            }

            if (user == null) {
                user = new User();
                userId = -1;
                preferences.edit().remove("ID").remove("NAME").apply();
            }
        }

        if (savedInstanceState == null) {
            if (userId == -1) {
                userFragment = new UserFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, userFragment)
                        .commit();
            } else {
                startMenu();
            }
        }
    }

    public void startMenu() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFragment, new MenuFragment(user))
                .commit();
    }

    //Возврат данных с UserFragment
    public void returnDataUser(User userData) {
        setUserData(userData);

        getSupportFragmentManager().popBackStackImmediate(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        );

        startMenu();
    }

    private void openScreen(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    //Запуск тестирования
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnPreparationTest(Boolean statusMedian) {
        calculateMedian = statusMedian;
        openScreen(new TestFragment());
    }

    //Запуск подробного тестирования
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnPreparationTestAdvanced(Boolean statusMedian) {
        calculateMedian = statusMedian;
        openScreen(new AdvancedTestFragment());
    }

    //Пересчет медианы по условию и показ результатов
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnResultTest(Audiogram audiogram) throws ParseException {
        audiogram.setIdUser(userId);
        db.open();
        int idAudio = (int)db.insertAudiogram(audiogram);
        int countAudiograms = db.searchAudiogram(userId);
        db.close();
        audiogram.setIdAudiogram(idAudio);

        if(calculateMedian || (countAudiograms >= 3 && countAudiograms <= 5)){
            Median medianCalc = new Median(this, userId);
            medianCalc.calculationMedian();
        }

        List<Audiogram> audiograms = new ArrayList<>();
        audiograms.add(audiogram);
        resultFragment = new ResultFragment(audiograms);
        showResultsAfterTest(resultFragment);
    }

    //Пересчет медианы по условию и показ результатов для расширенного теста
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnResultTestAdvanced(Audiogram audiogram) throws ParseException {
        audiogram.setIdUser(userId);
        db.open();
        int idAudio = (int)db.insertAudiogram(audiogram);
        int countAudiograms = db.searchAudiogram(userId);
        db.close();
        audiogram.setIdAudiogram(idAudio);

        if(calculateMedian || (countAudiograms >= 3 && countAudiograms <= 5)){
            Median medianCalc = new Median(this, userId);
            medianCalc.calculationMedian();
        }

        List<Audiogram> audiograms = new ArrayList<>();
        audiograms.add(audiogram);
        resultFragment = new ResultFragment(audiograms);
        showResultsAfterTest(resultFragment);
    }

    private void showResultsAfterTest(ResultFragment fragment) {
        // Возвращаемся к корневому экрану — главному меню.
        getSupportFragmentManager().popBackStackImmediate(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        );

        // Сохраняем только переход: главное меню → результаты.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    //Запуск деталей результатов каждого тестирования при просмотре всех результатов
    public void returnAllResults(Audiogram audiogram, Audiogram median) {
        detailFragment = new DetailFragment(
                audiogram,
                median,
                PopulationNorm.determinePopulationNorm(
                        user.getSexUser(),
                        user.getAgeUser()
                )
        );

        openScreen(detailFragment);
    }

    //Запуск деталей результатов каждого тестирования после тестирования
    public void returnResultFragment(Audiogram audiogram, Audiogram median) {
        detailFragment = new DetailFragment(
                audiogram,
                median,
                PopulationNorm.determinePopulationNorm(
                        user.getSexUser(),
                        user.getAgeUser()
                )
        );

        openScreen(detailFragment);
    }

    //Создается/редактируется пользователь
    public void returnMenuUser(User userData, int choice) {
        switch (choice) {
            case 1: {
                userId = -1;
                userFragment = new UserFragment();
                openScreen(userFragment);
                break;
            }
            case 2: {
                user = userData;
                userId = user.getIdUser();
                userFragment = new UserFragment();
                openScreen(userFragment);
                break;
            }
            case 3: {
                openScreen(new ConnectToServerFragment());
                break;
            }
            default:
                break;
        }
    }

    //Основное меню
    public void returnMenu(int choice){
        switch (choice){
            case 1: {
                openScreen(new PreparationTestFragment());
                break;
            }
            case 2: {
                openScreen(new AllResultsFragment());
                break;
            }
            case 3: {
                openScreen(new MenuUserFragment(user));
                break;
            }
            default: break;
        }
    }

    public int getSelectedUserId() {
        return userId;
    }

    public User getCurrentUser() {
        return user;
    }

    public void setUserData(User data) {
        user = data;
        userId = user.getIdUser();
    }
}