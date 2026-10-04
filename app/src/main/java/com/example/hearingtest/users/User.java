package com.example.hearingtest.users;
import androidx.annotation.NonNull;

public class User {
    private int idCounter = 1;

    private Integer idUser;
    private String nameUser;
    private Integer ageUser;
    private Boolean sexUser;

    public User(){
        this.idUser = -1;
    }

    public User(String name, Integer age, Boolean sex){
        this.setNameUser(name);
        this.setAgeUser(age);
        this.setSexUser(sex);
        this.setIdUser(idCounter);
        this.setIdCounter(true);
    }

    public User(String name, Integer age, Boolean sex, Boolean status){
        this.setNameUser(name);
        this.setAgeUser(age);
        this.setSexUser(sex);
        this.setIdUser(idCounter);
        this.setIdCounter(status);
    }

    public User(Integer id, String name, Integer age, Boolean sex){
        this.setIdUser(id);
        this.setNameUser(name);
        this.setAgeUser(age);
        this.setSexUser(sex);
    }

    //-----Getters-----
    public Integer getIdUser(){
        return idUser;
    }

    public String getNameUser(){
        return nameUser;
    }

    public Integer getAgeUser(){
        return ageUser;
    }

    public Boolean getSexUser(){
        return sexUser;
    }

    public int getIdCounter() {
        return idCounter;
    }

    //-----Setters-----
    public void setIdUser(Integer id){
        this.idUser = id;
    }

    public void setNameUser(String name){
        this.nameUser = name;
    }

    public void setAgeUser(Integer age){
        this.ageUser = age;
    }

    public void setSexUser(Boolean sex){
        this.sexUser = sex;
    }

    public void setIdCounter(Boolean status) {
        if(status){
            this.idCounter++;
        } else{
            this.idCounter = 1;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ID=" + idUser + ", name=" + nameUser ;
    }
}
