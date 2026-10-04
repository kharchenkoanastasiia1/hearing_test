package com.example.hearingtest.users;
import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
        nameUser = name;
        ageUser = age;
        sexUser = sex;
        idUser = idCounter;
        setIdCounter(true);
    }

    public User(String name, Integer age, Boolean sex, Boolean status){
        nameUser = name;
        ageUser = age;
        sexUser = sex;
        idUser = idCounter;
        setIdCounter(status);
    }

    public User(Integer id, String name, Integer age, Boolean sex){
        idUser = id;
        nameUser = name;
        ageUser = age;
        sexUser = sex;
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
