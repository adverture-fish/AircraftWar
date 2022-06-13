package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.SocketConnection;

import java.io.IOException;

public class WaitActivity extends AppCompatActivity {

    private String difficulty;

    private Boolean getPlayerFlag = false;

    private SocketConnection socketConnection = LoginActivity.getSocketConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        Button singleButton = (Button) findViewById(R.id.singleButton);
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleGameLunch();
            }
        });

        difficulty = getIntent().getStringExtra("difficulty");

        Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        getPlayerFlag = socketConnection.getPlayer(difficulty);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (true){
                        if(getPlayerFlag){
                            onlineGameLunch(difficulty);
                            break;
                        }
                    }
                }

            });
        t.start();

    }

    private void singleGameLunch (){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onlineGameLunch(String difficulty){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("onlineFlag", true);
        startActivity(intent);

    }
}