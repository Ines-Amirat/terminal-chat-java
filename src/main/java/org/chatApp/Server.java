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

    private int port ;
    Map<Socket,PrintWriter> writers = new HashMap<>();
    Map<Socket,String> clients = new HashMap<>();

    public Server(int port){
        this.port = port ;
    }


            public void threadClient(Socket currentSocket , PrintWriter out) {
                Thread client = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            InputStream input = currentSocket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                            String clientName = bufferedReader.readLine();

                            clients.put(currentSocket, clientName);
                            writers.put(currentSocket, out);

                            connexionNotice(currentSocket, clientName);
                            existingClientsNotice(currentSocket,out);
                            broadcastMessage(currentSocket, clientName,bufferedReader);


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
                client.start();
            }
            public void connexionNotice(Socket currentSocket , String clientName) {
                for (Socket s : clients.keySet()) {
                    if (s != currentSocket) {
                        PrintWriter out = writers.get(s);
                        out.println(clientName + " connected");
                    }
                }
            }
            public void existingClientsNotice(Socket currentSocket , PrintWriter out ){
                for(Socket s : clients.keySet()) {
                    if(s != currentSocket) {
                        String name = clients.get(s);
                        out.println(name + " is already connected");
                    }
                }
            }

            public void broadcastMessage(Socket currentSocket ,String clientName ,BufferedReader bufferedReader ) throws IOException {
                while (true) {
                    String msg = bufferedReader.readLine();
                    if (msg == null || msg.equals("quit")) {

                        for (Socket s : clients.keySet()) {
                            if (s != currentSocket) {
                                PrintWriter out = writers.get(s);
                                out.println(clientName + " quit the chat");
                            }
                        }
                        clients.remove(currentSocket);
                        writers.remove(currentSocket);
                        currentSocket.close();

                        break;

                    } else {
                        System.out.println(msg);
                        for (Socket s : clients.keySet()) {
                            if (s != currentSocket) {
                                PrintWriter out = writers.get(s);
                                out.println(clientName + ":" + msg);
                            }
                        }
                    }
                }
            }
    public static void main(String[] args) {
        try {
            Server server = new Server(7778);
            ServerSocket serverSocket = new  ServerSocket(server.port);
            System.out.println("Server Socket listen");

            while(true){
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                server.threadClient(socket,out);


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}