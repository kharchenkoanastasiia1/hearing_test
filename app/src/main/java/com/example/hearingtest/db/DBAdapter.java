package com.example.hearingtest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.example.hearingtest.audiogram.Audiogram;
import com.example.hearingtest.users.User;
import com.example.hearingtest.users.UserCollection;

import static com.example.hearingtest.db.Query.*;
import static com.example.hearingtest.db.Query.F250_AUDIOGRAM_RIGHT;
import static com.example.hearingtest.db.Query.F500_AUDIOGRAM_RIGHT;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    private final Context context;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DBAdapter(Context mCtx) {
        this.context = mCtx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(context);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null)
            mDBHelper.close();
    }

    //=========User=========

    //вернуть всех пользователей
    private Cursor getAllEntries(){
        String[] columns = new String[] {USERS_COLUMN_ID, USERS_COLUMN_NAME, USERS_COLUMN_AGE, USERS_COLUMN_SEX};
        return  mDB.query(DB_TABLE_USERS, columns, null, null, null, null, null);
    }

    //заполнение коллекции пользователей
    public UserCollection getUsers(){
        UserCollection usersCollection = new UserCollection();
        Cursor cursor = getAllEntries();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_AGE));
            boolean sex = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_SEX)) != 0;
            usersCollection.getUsers().add(new User(id, name, age, sex));
        }
        cursor.close();
        return usersCollection;
    }

    //получение пользователя по id
    public User getUser(int id){
        User user = null;
        Cursor cursor = mDB.rawQuery(searchUser(), new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_AGE));
            boolean sex = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_SEX)) != 0;
            user = new User(id, name, age, sex);
        }
        cursor.close();
        return user;
    }

    public int getIDUser(String nameUser){
        int idUser = -1;
        Cursor cursor = mDB.rawQuery(searchUserForName(), new String[]{nameUser});
        if(cursor.moveToFirst()){
            idUser = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_ID));
        }
        cursor.close();
        return idUser;
    }

    public long insertUser(User user){
        ContentValues cv = new ContentValues();
        cv.put(USERS_COLUMN_NAME, user.getNameUser());
        cv.put(USERS_COLUMN_AGE, user.getAgeUser());
        cv.put(USERS_COLUMN_SEX, user.getSexUser());
        return mDB.insertOrThrow(DB_TABLE_USERS, null, cv);
    }

    public void updateUser(User user){
        String whereClause = USERS_COLUMN_ID + " = " + user.getIdUser();
        ContentValues cv = new ContentValues();
        cv.put(USERS_COLUMN_NAME, user.getNameUser());
        cv.put(USERS_COLUMN_AGE, user.getAgeUser());
        cv.put(USERS_COLUMN_SEX, user.getSexUser());
        mDB.update(DB_TABLE_USERS, cv, whereClause, null);
    }

    public void deleteUser(int userId){
        String whereClause = "idUser = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        mDB.delete(DB_TABLE_USERS, whereClause, whereArgs);
    }

    //=========Audiogram=========

    public long insertAudiogram(Audiogram audiogram){
        ContentValues cv = new ContentValues();
        cv.put(F125_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[0]);
        cv.put(F250_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[1]);
        cv.put(F500_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[2]);
        cv.put(F1000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[3]);
        cv.put(F2000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[4]);
        cv.put(F3000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[5]);
        cv.put(F4000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[6]);
        cv.put(F8000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[7]);
        cv.put(F125_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[0]);
        cv.put(F250_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[1]);
        cv.put(F500_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[2]);
        cv.put(F1000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[3]);
        cv.put(F2000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[4]);
        cv.put(F3000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[5]);
        cv.put(F4000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[6]);
        cv.put(F8000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[7]);
        cv.put(AUDIOGRAM_COLUMN_ID_USERS , audiogram.getIdUser());
        return mDB.insertOrThrow(DB_TABLE_AUDIOGRAMS, null, cv);
    }

    //Возврат количества аудиограмм у конкретного пользователя
    public int searchAudiogram(int userId){
        Cursor cursor = mDB.rawQuery(countRawInAudiogram(), new String[]{ String.valueOf(userId)});
        int count = 0;
        if(cursor.moveToFirst()){
            count = cursor.getInt(cursor.getColumnIndexOrThrow("Count(*)"));
        }
        cursor.close();
        return count;
    }

    public void deleteAudiogram(int audioId){
        String whereClause = AUDIOGRAM_LEFT_COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(audioId)};
        mDB.delete(DB_TABLE_AUDIOGRAMS, whereClause, whereArgs);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Audiogram> getAudiograms(int id) throws ParseException {
        List<Audiogram> audiograms = new ArrayList<>();
        Cursor cursor = mDB.rawQuery(getAllAudiograms(), new String[]{ String.valueOf(id)});
        while (cursor.moveToNext()){
            Integer[] left = new Integer[8];
            Integer[] right = new Integer[8];
            int idAudio = cursor.getInt(cursor.getColumnIndexOrThrow(AUDIOGRAM_LEFT_COLUMN_ID));
            left[0] = cursor.getInt(cursor.getColumnIndexOrThrow(F125_AUDIOGRAM_LEFT));
            left[1] = cursor.getInt(cursor.getColumnIndexOrThrow(F250_AUDIOGRAM_LEFT));
            left[2] = cursor.getInt(cursor.getColumnIndexOrThrow(F500_AUDIOGRAM_LEFT));
            left[3] = cursor.getInt(cursor.getColumnIndexOrThrow(F1000_AUDIOGRAM_LEFT));
            left[4] = cursor.getInt(cursor.getColumnIndexOrThrow(F2000_AUDIOGRAM_LEFT));
            left[5] = cursor.getInt(cursor.getColumnIndexOrThrow(F3000_AUDIOGRAM_LEFT));
            left[6] = cursor.getInt(cursor.getColumnIndexOrThrow(F4000_AUDIOGRAM_LEFT));
            left[7] = cursor.getInt(cursor.getColumnIndexOrThrow(F8000_AUDIOGRAM_LEFT));
            right[0] = cursor.getInt(cursor.getColumnIndexOrThrow(F125_AUDIOGRAM_RIGHT));
            right[1] = cursor.getInt(cursor.getColumnIndexOrThrow(F250_AUDIOGRAM_RIGHT));
            right[2] = cursor.getInt(cursor.getColumnIndexOrThrow(F500_AUDIOGRAM_RIGHT));
            right[3] = cursor.getInt(cursor.getColumnIndexOrThrow(F1000_AUDIOGRAM_RIGHT));
            right[4] = cursor.getInt(cursor.getColumnIndexOrThrow(F2000_AUDIOGRAM_RIGHT));
            right[5] = cursor.getInt(cursor.getColumnIndexOrThrow(F3000_AUDIOGRAM_RIGHT));
            right[6] = cursor.getInt(cursor.getColumnIndexOrThrow(F4000_AUDIOGRAM_RIGHT));
            right[7] = cursor.getInt(cursor.getColumnIndexOrThrow(F8000_AUDIOGRAM_RIGHT));
            String dateString = cursor.getString(cursor.getColumnIndexOrThrow(DATE_AUDIOGRAM));
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(AUDIOGRAM_COLUMN_ID_USERS));

            audiograms.add(new Audiogram(idAudio, left, right, date, idUser));
        }
        cursor.close();

        return audiograms;
    }

    //=========Median=========
    public void insertMedian(Audiogram audiogram){
        ContentValues cv = new ContentValues();
        cv.put(F125_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[0]);
        cv.put(F250_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[1]);
        cv.put(F500_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[2]);
        cv.put(F1000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[3]);
        cv.put(F2000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[4]);
        cv.put(F3000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[5]);
        cv.put(F4000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[6]);
        cv.put(F8000_AUDIOGRAM_LEFT, audiogram.getValueAmplitudeLeft()[7]);
        cv.put(F125_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[0]);
        cv.put(F250_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[1]);
        cv.put(F500_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[2]);
        cv.put(F1000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[3]);
        cv.put(F2000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[4]);
        cv.put(F3000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[5]);
        cv.put(F4000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[6]);
        cv.put(F8000_AUDIOGRAM_RIGHT, audiogram.getValueAmplitudeRight()[7]);
        cv.put(AUDIOGRAM_COLUMN_ID_USERS , audiogram.getIdUser());
        mDB.insertOrThrow(DB_TABLE_MEDIANS, null, cv);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Audiogram> getMedians(int id){
        List<Audiogram> audiograms = new ArrayList<>();
        Cursor cursor = mDB.rawQuery(getCountMedians(), new String[]{ String.valueOf(id)});
        while (cursor.moveToNext()){
            Integer[] left = new Integer[8];
            Integer[] right = new Integer[8];
            int idAudio = cursor.getInt(cursor.getColumnIndexOrThrow(MEDIANS_COLUMN_ID));
            left[0] = cursor.getInt(cursor.getColumnIndexOrThrow(F125_AUDIOGRAM_LEFT));
            left[1] = cursor.getInt(cursor.getColumnIndexOrThrow(F250_AUDIOGRAM_LEFT));
            left[2] = cursor.getInt(cursor.getColumnIndexOrThrow(F500_AUDIOGRAM_LEFT));
            left[3] = cursor.getInt(cursor.getColumnIndexOrThrow(F1000_AUDIOGRAM_LEFT));
            left[4] = cursor.getInt(cursor.getColumnIndexOrThrow(F2000_AUDIOGRAM_LEFT));
            left[5] = cursor.getInt(cursor.getColumnIndexOrThrow(F3000_AUDIOGRAM_LEFT));
            left[6] = cursor.getInt(cursor.getColumnIndexOrThrow(F4000_AUDIOGRAM_LEFT));
            left[7] = cursor.getInt(cursor.getColumnIndexOrThrow(F8000_AUDIOGRAM_LEFT));
            right[0] = cursor.getInt(cursor.getColumnIndexOrThrow(F125_AUDIOGRAM_RIGHT));
            right[1] = cursor.getInt(cursor.getColumnIndexOrThrow(F250_AUDIOGRAM_RIGHT));
            right[2] = cursor.getInt(cursor.getColumnIndexOrThrow(F500_AUDIOGRAM_RIGHT));
            right[3] = cursor.getInt(cursor.getColumnIndexOrThrow(F1000_AUDIOGRAM_RIGHT));
            right[4] = cursor.getInt(cursor.getColumnIndexOrThrow(F2000_AUDIOGRAM_RIGHT));
            right[5] = cursor.getInt(cursor.getColumnIndexOrThrow(F3000_AUDIOGRAM_RIGHT));
            right[6] = cursor.getInt(cursor.getColumnIndexOrThrow(F4000_AUDIOGRAM_RIGHT));
            right[7] = cursor.getInt(cursor.getColumnIndexOrThrow(F8000_AUDIOGRAM_RIGHT));
            String dateString = cursor.getString(cursor.getColumnIndexOrThrow(DATE_AUDIOGRAM));
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(AUDIOGRAM_COLUMN_ID_USERS));

            audiograms.add(new Audiogram(idAudio, left, right, date, idUser));
        }
        cursor.close();
        return audiograms;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Audiogram getLastRowMedians(int id){
        Audiogram audiogram = null;
        Cursor cursor = mDB.rawQuery(getNumberRowMedians(), new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            Integer[] left = new Integer[8];
            Integer[] right = new Integer[8];
            int idAudio = cursor.getInt(cursor.getColumnIndexOrThrow(MEDIANS_COLUMN_ID));
            left[0] = cursor.getInt(cursor.getColumnIndexOrThrow(F125_AUDIOGRAM_LEFT));
            left[1] = cursor.getInt(cursor.getColumnIndexOrThrow(F250_AUDIOGRAM_LEFT));
            left[2] = cursor.getInt(cursor.getColumnIndexOrThrow(F500_AUDIOGRAM_LEFT));
            left[3] = cursor.getInt(cursor.getColumnIndexOrThrow(F1000_AUDIOGRAM_LEFT));
            left[4] = cursor.getInt(cursor.getColumnIndexOrThrow(F2000_AUDIOGRAM_LEFT));
            left[5] = cursor.getInt(cursor.getColumnIndexOrThrow(F3000_AUDIOGRAM_LEFT));
            left[6] = cursor.getInt(cursor.getColumnIndexOrThrow(F4000_AUDIOGRAM_LEFT));
            left[7] = cursor.getInt(cursor.getColumnIndexOrThrow(F8000_AUDIOGRAM_LEFT));
            right[0] = cursor.getInt(cursor.getColumnIndexOrThrow(F125_AUDIOGRAM_RIGHT));
            right[1] = cursor.getInt(cursor.getColumnIndexOrThrow(F250_AUDIOGRAM_RIGHT));
            right[2] = cursor.getInt(cursor.getColumnIndexOrThrow(F500_AUDIOGRAM_RIGHT));
            right[3] = cursor.getInt(cursor.getColumnIndexOrThrow(F1000_AUDIOGRAM_RIGHT));
            right[4] = cursor.getInt(cursor.getColumnIndexOrThrow(F2000_AUDIOGRAM_RIGHT));
            right[5] = cursor.getInt(cursor.getColumnIndexOrThrow(F3000_AUDIOGRAM_RIGHT));
            right[6] = cursor.getInt(cursor.getColumnIndexOrThrow(F4000_AUDIOGRAM_RIGHT));
            right[7] = cursor.getInt(cursor.getColumnIndexOrThrow(F8000_AUDIOGRAM_RIGHT));
            String dateString = cursor.getString(cursor.getColumnIndexOrThrow(DATE_AUDIOGRAM));
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(AUDIOGRAM_COLUMN_ID_USERS));
            audiogram = new Audiogram(idAudio, left, right, date, idUser);
        }
        cursor.close();
        return audiogram;
    }
}
