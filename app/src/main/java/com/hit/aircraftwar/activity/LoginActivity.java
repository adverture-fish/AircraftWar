package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.SocketConnection;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gameLunch();
                    this.wait();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //获取游戏账户和密码，启动游戏等
    private void gameLunch() throws IOException, InterruptedException {
        EditText usernameText = (EditText)findViewById(R.id.usernameText);
        EditText passwordText = (EditText)findViewById(R.id.passwordText);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        Thread loginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SocketConnection socketConnection = new SocketConnection();
                try {
                    socketConnection.handle(username, password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loginThread.start();
        this.notify();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}