package com.example.hearingtest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.db.DBAdapter;
import com.example.hearingtest.fragment.AllResultsFragment;
import com.example.hearingtest.fragment.DefinitionNormFragment;
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
import com.example.hearingtest.constants.AgeNormConstants;
import com.example.hearingtest.norm.PopulationNorm;
import com.example.hearingtest.users.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    DBAdapter db;
    TestFragment testFragment;
    AdvancedTestFragment advancedTestFragment;
    UserFragment userFragment;
    MenuFragment menuFragment;
    MenuUserFragment menuUserFragment;
    PreparationTestFragment preparationTestFragment;
    DefinitionNormFragment definitionNormFragment;
    DetailFragment detailFragment;
    AllResultsFragment allResultsFragment;
    ResultFragment resultFragment;
    ConnectToServerFragment connectToServerFragment;
    SharedPreferences sPref;
    User user;
    int userId;
    AudioManager audioManager;
    Boolean calculateMedian = false;
    Boolean userFragmentStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBAdapter(this);
        user = new User();
        userFragment = new UserFragment();

        sPref = getPreferences(MODE_PRIVATE);
        userId = sPref.getInt("ID", -1);

        if(userId == -1){
            userFragmentStatus = true;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameFragment, userFragment)
                    .commit();
        } else{
            db.open();
            user = db.getUser(userId);
            db.close();

            menuFragment = new MenuFragment(user);
            getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, menuFragment, null).commit();
        }

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        testFragment = new TestFragment();
        advancedTestFragment = new AdvancedTestFragment();
        preparationTestFragment = new PreparationTestFragment();
        menuUserFragment = new MenuUserFragment(user);
        definitionNormFragment = new DefinitionNormFragment();
        allResultsFragment = new AllResultsFragment();
        connectToServerFragment = new ConnectToServerFragment();
    }

    public void startMenu(){
        menuFragment = new MenuFragment(user);
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, menuFragment, null).addToBackStack(null).commit();
    }

    //Возврат данных с UserFragment
    public void returnDataUser(User userData){
        getSupportFragmentManager().beginTransaction().remove(userFragment).commit();
        user = userData;
        userId = user.getIdUser();
        menuFragment = new MenuFragment(user);
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, menuFragment, null).addToBackStack(null).commit();
    }

    //Запуск тестирования
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnPreparationTest(Boolean statusMedian){
        getSupportFragmentManager().beginTransaction().remove(preparationTestFragment).commit();
        calculateMedian = statusMedian;
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, testFragment, null).addToBackStack(null).commit();
    }

    //Запуск подробного тестирования
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnPreparationTestAdvanced(Boolean statusMedian){
        getSupportFragmentManager().beginTransaction().remove(preparationTestFragment).commit();
        calculateMedian = statusMedian;
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, advancedTestFragment, null).addToBackStack(null).commit();
    }

    //Пересчет медианы по условию и показ результатов
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnResultTest(Audiogram audiogram) throws ParseException {
        getSupportFragmentManager().beginTransaction().remove(testFragment).commit();
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
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, resultFragment, null).addToBackStack(null).commit();
    }

    //Пересчет медианы по условию и показ результатов для расширенного теста
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void returnResultTestAdvanced(Audiogram audiogram) throws ParseException {
        getSupportFragmentManager().beginTransaction().remove(advancedTestFragment).commit();
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
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, resultFragment, null).addToBackStack(null).commit();
    }

    //Запуск деталей результатов каждого тестирования при просмотре всех результатов
    public void returnAllResults(Audiogram audiogram, Audiogram median){
        getSupportFragmentManager().beginTransaction().remove(allResultsFragment).commit();
        detailFragment = new DetailFragment(audiogram, median, PopulationNorm.determinePopulationNorm(user.getSexUser(), user.getAgeUser()));
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, detailFragment, null).addToBackStack(null).commit();
    }

    //Запуск деталей результатов каждого тестирования после тестирования
    public void returnResultFragment(Audiogram audiogram, Audiogram median){
        getSupportFragmentManager().beginTransaction().remove(resultFragment).commit();
        detailFragment = new DetailFragment(audiogram, median, PopulationNorm.determinePopulationNorm(user.getSexUser(), user.getAgeUser()));
        getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, detailFragment, null).addToBackStack(null).commit();
    }

    //Создается/редактируется пользователь
    public void returnMenuUser(User userData, int choise){
        getSupportFragmentManager().beginTransaction().remove(connectToServerFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(menuUserFragment).commit();

        switch (choise){
            case 1: {
                userId = -1;
                userFragment = new UserFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, userFragment, null).addToBackStack(null).commit();
                break;
            }
            case 2: {
                user = userData;
                userId = user.getIdUser();
                userFragment = new UserFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, userFragment, null).addToBackStack(null).commit();
                break;
            }
            case 3:{
                connectToServerFragment = new ConnectToServerFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, connectToServerFragment, null).addToBackStack(null).commit();
                break;
            }
            default: break;
        }
    }

    //Основное меню
    public void returnMenu(int choise){
        getSupportFragmentManager().beginTransaction().remove(connectToServerFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(menuFragment).commit();

        switch (choise){
            case 1: {
                preparationTestFragment = new PreparationTestFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, preparationTestFragment, null).addToBackStack(null).commit();
                break;
            }
            case 2: {
                allResultsFragment = new AllResultsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, allResultsFragment).addToBackStack(null).commit();
                break;
            }
            case 3:{
                if(userFragmentStatus){
                    userFragmentStatus = false;
                    menuFragment = new MenuFragment(user);
                    getSupportFragmentManager().beginTransaction().add(R.id.frameFragment, menuFragment, null).addToBackStack(null).commit();
                } else{
                    menuUserFragment = new MenuUserFragment(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, menuUserFragment).addToBackStack(null).commit();
                }
                break;
            }
            case 4: {
                definitionNormFragment = new DefinitionNormFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, definitionNormFragment).addToBackStack(null).commit();
                break;
            }
            default: break;
        }
    }

    public int getUserIdForMain(){return userId;}

    public void setUserData(User data) {
        user = data;
        userId = user.getIdUser();
    }
    public AudioManager getAudioManager(){return audioManager;}
}