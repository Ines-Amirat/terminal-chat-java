package org.chatApp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private  String clientName;
    private  String serverIp;
    private  int port ;
    private PrintWriter out ;


    public Client(String clientName,String serverIp , int port) throws IOException {
        this.clientSocket = new Socket(serverIp,port);;
        this.clientName = clientName;
        this.serverIp = serverIp;
        this.port = port;
        this.out =  new PrintWriter(clientSocket.getOutputStream(), true);
        this.out.println(this.clientName);

    }
            public void threadReceiver(){
                Thread client =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream input = clientSocket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                            String msg = bufferedReader.readLine();
                            System.out.println(msg);

                            while((msg = bufferedReader.readLine()) != null){
                                System.out.println(msg);
                            }
                            System.out.println("Connexion finished by the server");
                            clientSocket.close();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
                client.start();
            }
            public void sendMessage(){
                Scanner scanner = new Scanner(System.in);

                //loop never stop
                while (true) {
                    String message = scanner.nextLine();
                    out.println(message);

                }
            }



    public static void main(String[] args) {
        String clientName = args[0];
        String serverIp = args[1] ;
        int port = Integer.parseInt(args[2]);
        try {
            Client user  = new Client(clientName ,serverIp ,port);
            user.threadReceiver();
            user.sendMessage();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
