package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.SocketConnection;

import java.io.IOException;

public class EnrollActivity extends AppCompatActivity {

    private  String name;
    private  String password;
    private  Boolean enrollFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll2);
        Button enroll = (Button) findViewById(R.id.enrollButton);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessage();
                enroll();
            }
        });
    }

    private void getMessage(){
        TextView nameText = (TextView) findViewById(R.id.usernameText);
        TextView passwordText = (TextView) findViewById(R.id.passwordText);
        name = nameText.getText().toString();
        password = passwordText.getText().toString();
    }

    private  void enroll(){
        Thread enrollThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SocketConnection socketConnection = new SocketConnection();
                try {
                    enrollFlag = socketConnection.handle(name, password, "1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        enrollThread.start();
        while(true){
            if(enrollFlag){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}