package com.hit.aircraftwar.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.hit.aircraftwar.R;

public class MainActivity extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE = "difficulty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button easyButton  = (Button) findViewById(R.id.easyButton);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLunch("easy");
            }
        });
        Button mediumButton = (Button) findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLunch("medium");
            }
        });
        Button difficultButton = (Button)findViewById(R.id.diffiucltButton);
        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLunch("difficult");
            }
        });
    }


    public void gameLunch(String message){
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(EXTRA_MESSAGE, message);
        startActivity(gameIntent);
    }

}