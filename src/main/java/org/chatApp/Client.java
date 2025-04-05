package org.chatApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    {
        try {
            Scanner scanner = new Scanner(System.in);
            Socket clientSocket = new Socket("127.0.0.1", 7777);
            String message = scanner.nextLine();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            out.println(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
