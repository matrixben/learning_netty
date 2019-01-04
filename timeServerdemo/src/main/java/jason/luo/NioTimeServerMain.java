package jason.luo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioTimeServerMain {
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args){
        int port = 8001;
        NioTimeServer timeServer = new NioTimeServer(port);

        executor.execute(timeServer);
    }
}
