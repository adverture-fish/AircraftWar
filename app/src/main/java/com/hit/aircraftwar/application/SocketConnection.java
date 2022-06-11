package com.hit.aircraftwar.application;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {
     public boolean handle(String name , String password, String enrollFlag) throws IOException {
         Socket socket = new Socket("10.0.2.2", 9999);
         //获取输出流
         Log.i("client", "handle: connect to server");
         OutputStream os = socket.getOutputStream();
         PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, "UTF-8" )),true);
         //输入流
         InputStream is = socket.getInputStream();
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         pw.println(enrollFlag);
         pw.println(name);
         pw.println(password);
         String s = br.readLine();
         if(s.equals("finish")){
             return true;
         }
         return false;
     }
}
