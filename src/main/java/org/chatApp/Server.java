package org.chatApp;


import java.io.*;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new  ServerSocket(7777 );
            System.out.println("Server Socket listen");
            List<Socket> clients = new ArrayList<>();
            while(true){
                Socket socket = serverSocket.accept();
                clients.add(socket);
                System.out.println("new client connected");
                Thread client =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream  input = socket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                            String msg = bufferedReader.readLine();
                            System.out.println(msg);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
                client.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}