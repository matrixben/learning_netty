package jason.luo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BioTimeServerHandler implements Runnable {
    private Socket clientSocket;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    BioTimeServerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = reader.readLine();
            if ("time".equals(request)){
                String currentTime = dateFormat.format(new Date());
                writer.println("Current date time is " + currentTime);
            }else {
                writer.println("Bad request.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
