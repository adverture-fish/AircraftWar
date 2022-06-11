package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
    private  Boolean failFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll2);
        Button enroll = findViewById(R.id.enrollButton);
        enroll.setOnClickListener(v -> {
            getMessage();
            enroll();
        });
    }

    private void getMessage(){
        TextView nameText = findViewById(R.id.usernameText);
        TextView passwordText = findViewById(R.id.passwordText);
        name = nameText.getText().toString();
        password = passwordText.getText().toString();
    }

    private  void enroll(){
        Thread enrollThread = new Thread(() -> {
            SocketConnection socketConnection = new SocketConnection();
            try {
                enrollFlag = socketConnection.handle(name, password, "1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        enrollThread.start();
        Thread waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    if(!enrollFlag){
                       failFlag = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waitThread.start();
        while(true){
            if(enrollFlag){
                AlertDialog.Builder informDialog = new AlertDialog.Builder(EnrollActivity.this);
                informDialog.setTitle("enroll success");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
            else if(failFlag){
                AlertDialog.Builder failDialog = new AlertDialog.Builder(EnrollActivity.this);
                failDialog.setTitle("fail,please retry");
                failDialog.show();
                failFlag = false;
                break;
            }
        }
    }
}