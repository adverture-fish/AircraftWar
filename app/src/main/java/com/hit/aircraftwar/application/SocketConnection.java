package com.hit.aircraftwar.application;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;

public class SocketConnection {


    public Socket getSocket() {
        return socket;
    }

    private Socket socket;

    private PrintWriter pw;



    private  BufferedReader br;
     public boolean handle(String name , String password, String enrollFlag) throws IOException {
         socket = new Socket("10.0.2.2", 9999);
         //获取输出流
         Log.i("client", "handle: connect to server");
         OutputStream os = socket.getOutputStream();
         pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8)),true);
         //输入流
         InputStream is = socket.getInputStream();
         br = new BufferedReader(new InputStreamReader(is));
         pw.println(enrollFlag);
         pw.println(name);
         pw.println(password);

         String s = br.readLine();
         System.out.println(s);
         while(!Objects.equals(s, "finish")){
             System.out.println(s);
             s = br.readLine();
         }
         return true;
     }

     public boolean getPlayer(String difficulty) throws IOException {
         pw.println(difficulty);
         String s = br.readLine();
         System.out.println(s);
         while(!Objects.equals(s, "start")){
             System.out.println(s);
             s = br.readLine();
         }
         return true;
     }



     public void sendScore(int score) throws IOException {
         pw.println(score);
         String s = br.readLine();

     }

     public String readMessage() throws IOException {
        return br.readLine();
     }
}
