package com.hit.aircraftwar.DAO;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * @author qh
 *数据具体存储类
 */
public class Record implements Serializable {
    private String username;
    private String time;
    private int score;
    
    public Record(String username, String time, int score){
        this.time = time;
        this.score = score;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString(){
        return "    "+String.format("%-20s", username) + String.format("%-20d", score) + String.format("%-20s", time);
    }



}
