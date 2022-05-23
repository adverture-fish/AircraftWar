package com.hit.aircraftwar.activity;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.hit.aircraftwar.R;

public class MainActivity extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE = "difficulty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button easyButton  = findViewById(R.id.easyButton);
        easyButton.setOnClickListener(v -> gameLunch("easy"));
        Button mediumButton = findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(v -> gameLunch("medium"));
        Button difficultButton = findViewById(R.id.diffiucltButton);
        difficultButton.setOnClickListener(v -> gameLunch("difficult"));
    }


    public void gameLunch(String message){
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(EXTRA_MESSAGE, message);
        startActivity(gameIntent);
    }

}