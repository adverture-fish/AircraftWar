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

    Boolean loginFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.loginButton);
        Button enroll = (Button)findViewById(R.id.enrollButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gameLunch();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollLunch();
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
                    loginFlag = socketConnection.handle(username, password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loginThread.start();
        while(true){
            if(loginFlag){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void enrollLunch(){
        Intent intent = new Intent(this, EnrollActivity.class);
        startActivity(intent);
    }
}