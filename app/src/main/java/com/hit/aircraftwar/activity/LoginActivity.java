package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AlertDialog;
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

    Boolean falseFlag = false;

    private static  SocketConnection socketConnection = new SocketConnection();

    public static SocketConnection getSocketConnection() {
        return socketConnection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.loginButton);
        Button enroll = findViewById(R.id.enrollButton);
        login.setOnClickListener(v -> {
            try {
                gameLunch();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        enroll.setOnClickListener(v -> enrollLunch());
    }
    //获取游戏账户和密码，启动游戏等
    private void gameLunch() throws IOException, InterruptedException {
        EditText usernameText = findViewById(R.id.usernameText);
        EditText passwordText = findViewById(R.id.passwordText);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        Thread loginThread = new Thread(() -> {
            try {
                loginFlag = socketConnection.handle(username, password, "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loginThread.start();
        Thread waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    if(!loginFlag){
                        falseFlag = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waitThread.start();
        while(true){

            if(loginFlag){
                AlertDialog.Builder informDialog = new AlertDialog.Builder(LoginActivity.this);
                informDialog.setTitle("login success");
                informDialog.show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("online","true");
                startActivity(intent);
                break;
            }
            else if(falseFlag){
                AlertDialog.Builder failDialog = new AlertDialog.Builder(LoginActivity.this);
                failDialog.setTitle("Login Fail ,please retry");
                failDialog.show();
                passwordText.setText("");
                falseFlag = false;
                break;
            }
        }
    }

    private void enrollLunch(){
        Intent intent = new Intent(this, EnrollActivity.class);
        startActivity(intent);
    }
}