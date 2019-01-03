package jason.luo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BioTimeClient {
    public static void main(String[] args){
        int port = 8001;
        Socket clientSocket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            clientSocket = new Socket("localhost", port);
            reader = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("time");
            System.out.println("Client ask current time.");
            String response = reader.readLine();
            System.out.println("From server: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
