package com.example.hearingtest.db;

public class Query {

    public static final String DB_NAME = "hearingtest";

    public static final String DB_TABLE_USERS = "users";
    public static final String USERS_COLUMN_ID = "idUsers";
    public static final String USERS_COLUMN_NAME = "nameUsers";
    public static final String USERS_COLUMN_AGE = "ageUser";
    public static final String USERS_COLUMN_SEX = "sexUser";

    public static final String DB_TABLE_AUDIOGRAMS = "audiograms";
    public static final String AUDIOGRAM_LEFT_COLUMN_ID = "idAudiograms";
    public static final String F125_AUDIOGRAM_LEFT = "f125left";
    public static final String F250_AUDIOGRAM_LEFT = "f250left";
    public static final String F500_AUDIOGRAM_LEFT = "f500left";
    public static final String F1000_AUDIOGRAM_LEFT = "f1000left";
    public static final String F2000_AUDIOGRAM_LEFT = "f2000left";
    public static final String F3000_AUDIOGRAM_LEFT = "f3000left";
    public static final String F4000_AUDIOGRAM_LEFT = "f4000left";
    public static final String F8000_AUDIOGRAM_LEFT = "f8000left";
    public static final String F125_AUDIOGRAM_RIGHT = "f125right";
    public static final String F250_AUDIOGRAM_RIGHT = "f250right";
    public static final String F500_AUDIOGRAM_RIGHT = "f500right";
    public static final String F1000_AUDIOGRAM_RIGHT = "f1000right";
    public static final String F2000_AUDIOGRAM_RIGHT = "f2000right";
    public static final String F3000_AUDIOGRAM_RIGHT = "f3000right";
    public static final String F4000_AUDIOGRAM_RIGHT = "f4000right";
    public static final String F8000_AUDIOGRAM_RIGHT = "f8000right";
    public static final String DATE_AUDIOGRAM = "date";
    public static final String AUDIOGRAM_COLUMN_ID_USERS = "idUsers";

    public static final String DB_TABLE_MEDIANS = "medians";
    public static final String MEDIANS_COLUMN_ID = "idMedians";

    public static final String FOREIGN_KEY_USERS_AUDIOGRAM = "fkAudiogramAndUser";
    public static final String FOREIGN_KEY_USERS_MEDIAN = "fkMedianAndUser";

    public static String createTableUsers() {
        return "CREATE TABLE " + DB_TABLE_USERS + " (\n" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                USERS_COLUMN_NAME + " VARCHAR NOT NULL,\n" +
                USERS_COLUMN_AGE + " INTEGER NOT NULL,\n" +
                USERS_COLUMN_SEX + " BOOLEAN NOT NULL);";
    }

    public static String createTableAudiograms() {
        return "CREATE TABLE " + DB_TABLE_AUDIOGRAMS + " (\n" +
                AUDIOGRAM_LEFT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                F125_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F250_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F500_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F1000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F2000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F3000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F4000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F8000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F125_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F250_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F500_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F1000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F2000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F3000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F4000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F8000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                DATE_AUDIOGRAM + " DATE DEFAULT CURRENT_TIMESTAMP,\n" +
                AUDIOGRAM_COLUMN_ID_USERS + " INTEGER NOT NULL,\n" +
                "CONSTRAINT " + FOREIGN_KEY_USERS_AUDIOGRAM + "\n" +
                "FOREIGN KEY (" + AUDIOGRAM_COLUMN_ID_USERS + ")\n REFERENCES " + DB_TABLE_USERS + "(" + USERS_COLUMN_ID + "));";
    }


    public static String createTableMedian(){
        return "CREATE TABLE " + DB_TABLE_MEDIANS + " (\n" +
                MEDIANS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                F125_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F250_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F500_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F1000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F2000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F3000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F4000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F8000_AUDIOGRAM_LEFT + " INTEGER NOT NULL,\n" +
                F125_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F250_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F500_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F1000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F2000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F3000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F4000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                F8000_AUDIOGRAM_RIGHT + " INTEGER NOT NULL,\n" +
                DATE_AUDIOGRAM + " DATE DEFAULT CURRENT_TIMESTAMP,\n" +
                AUDIOGRAM_COLUMN_ID_USERS + " INTEGER NOT NULL,\n" +
                "CONSTRAINT " + FOREIGN_KEY_USERS_MEDIAN + "\n" +
                "FOREIGN KEY (" + AUDIOGRAM_COLUMN_ID_USERS + ")\n REFERENCES " + DB_TABLE_USERS + "(" + USERS_COLUMN_ID + "));";
    }


    public static String dropTableUsers(){
        return "DROP TABLE IF EXISTS " + DB_TABLE_USERS;
    }

    public static String dropTableAudiograms(){
        return "DROP TABLE IF EXISTS " + DB_TABLE_AUDIOGRAMS;
    }

    public static String dropTableMedians(){
        return "DROP TABLE IF EXISTS " + DB_TABLE_MEDIANS;
    }


    public static String searchUser(){
        return "SELECT * FROM " + DB_TABLE_USERS + " WHERE " + USERS_COLUMN_ID + " = ?";
    }

    public static String searchUserForName(){
        return "SELECT * FROM " + DB_TABLE_USERS + " WHERE " + USERS_COLUMN_NAME + " = ?";
    }

    public static String countRawInAudiogram(){
        return "SELECT Count(*) FROM " + DB_TABLE_AUDIOGRAMS + " WHERE " + AUDIOGRAM_COLUMN_ID_USERS + " = ?";
    }

    public static String getNumberRowAudiograms(){
        return "SELECT * FROM " + DB_TABLE_AUDIOGRAMS + " WHERE " + AUDIOGRAM_COLUMN_ID_USERS + " = ?" + " LIMIT 1 OFFSET ?";
    }

    public static String getAllAudiograms(){
        return "SELECT * FROM " + DB_TABLE_AUDIOGRAMS + " WHERE " + AUDIOGRAM_COLUMN_ID_USERS + " = ?";
    }

    public static String getCountAudiograms(){
        return "SELECT * FROM " + DB_TABLE_AUDIOGRAMS + " WHERE " + AUDIOGRAM_COLUMN_ID_USERS + " = ?" + " LIMIT ?";
    }

    public static String getCountMedians(){
        return "SELECT * FROM " + DB_TABLE_MEDIANS + " WHERE " + AUDIOGRAM_COLUMN_ID_USERS + " = ?";
    }

    public static String getNumberRowMedians(){
        return "SELECT * FROM " + DB_TABLE_MEDIANS + " WHERE " + AUDIOGRAM_COLUMN_ID_USERS + " = ? ORDER BY " + MEDIANS_COLUMN_ID + " DESC LIMIT 1";
    }
}
