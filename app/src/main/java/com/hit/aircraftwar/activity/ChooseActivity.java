package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit.aircraftwar.R;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Button singleButton = (Button) findViewById(R.id.singleGame);
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleGameLunch();
            }
        });
        Button onlineButton = (Button) findViewById(R.id.onlineGame);
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineGameLunch();
            }
        });
    }

    private void singleGameLunch(){
        Intent singleGame = new Intent(this, MainActivity.class);
        startActivity(singleGame);
    }
    private void onlineGameLunch(){
        Intent onlineGame = new Intent(this, LoginActivity.class);
        startActivity(onlineGame);
    }
}