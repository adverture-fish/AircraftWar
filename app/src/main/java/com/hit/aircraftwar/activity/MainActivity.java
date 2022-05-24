package com.hit.aircraftwar.activity;


import androidx.appcompat.app.AppCompatActivity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.MusicService;

public class MainActivity extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE = "difficulty";

    Connect conn ;
    public static MusicService.MyBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicLunch();
        Button easyButton  = findViewById(R.id.easyButton);
        easyButton.setOnClickListener(v -> gameLunch("easy"));
        Button mediumButton = findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(v -> gameLunch("medium"));
        Button difficultButton = findViewById(R.id.diffiucltButton);
        difficultButton.setOnClickListener(v -> gameLunch("difficult"));
        Switch musicSwitch = findViewById(R.id.musicSwitch);
        musicSwitch.setOnClickListener(v -> {
            if(musicSwitch.isChecked()){ musicLunch(); }
            else{ musicExit(); }
        });
    }


    private void musicLunch(){
        if(myBinder == null){
            conn = new Connect();
            Intent musicIntent = new Intent(this, MusicService.class);
            bindService(musicIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    public void musicExit(){
        if(conn != null){
            unbindService(conn);
            myBinder = null;
        }
    }


    public void gameLunch(String message){
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(EXTRA_MESSAGE, message);
        startActivity(gameIntent);
        if(myBinder != null){
            myBinder.playBgm();
        }
    }

    class Connect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            Log.i("music demo","Service Connnected");
            myBinder = (MusicService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(conn);
    }

}