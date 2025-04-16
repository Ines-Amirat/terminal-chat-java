package org.chatApp;


import java.io.*;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.PrintWriter;
import java.util.Map;

import org.chatApp.Client;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new  ServerSocket(7778 );
            System.out.println("Server Socket listen");
            Map<Socket,PrintWriter> writers = new HashMap<>();
            Map<Socket,String> clients = new HashMap<>();


            while(true){
                Socket socket = serverSocket.accept();
                final  Socket currentSocket = socket;

                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

                Thread client =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream input = currentSocket.getInputStream();
                            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(input));
                            String clientName = bufferedReader.readLine();

                            clients.put(currentSocket,clientName);
                            writers.put(currentSocket,out);

                            for(Socket s : clients.keySet()){
                                if(s != currentSocket){
                                    PrintWriter out = writers.get(s);
                                    out.println(clientName + " connected");
                                }
                            }

                            for(Socket s : clients.keySet()) {
                                if(s != currentSocket) {
                                    String name = clients.get(s);
                                    out.println(name + " is already connected");
                                }
                            }

                            while(true) {
                                String msg = bufferedReader.readLine();
                                if(msg ==null || msg.equals("quit")){

                                    for(Socket s : clients.keySet()) {
                                        if(s != currentSocket) {
                                            PrintWriter out = writers.get(s);
                                            out.println(clientName + " quit the chat");
                                        }
                                    }
                                     clients.remove(currentSocket);
                                     writers.remove(currentSocket);
                                     currentSocket.close();

                                     break;

                                }

                                else {
                                    System.out.println(msg);
                                    for(Socket s : clients.keySet()){
                                        if(s != currentSocket){
                                            PrintWriter out = writers.get(s);
                                            out.println(clientName + ":" + msg);
                                        }
                                    }
                                }
                            }

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