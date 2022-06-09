package com.hit.aircraftwar.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {
     public boolean handle(String name , String password) throws IOException {
         Socket socket = new Socket("localhost", 6666);
         //获取输出流

         OutputStream os = socket.getOutputStream();
         PrintWriter pw = new PrintWriter(os);
         //输入流
         InputStream is = socket.getInputStream();
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         pw.write(name);
         pw.write(password);
         int s = (int) is.read();
         System.out.println(s);

         return true;
     }
}
