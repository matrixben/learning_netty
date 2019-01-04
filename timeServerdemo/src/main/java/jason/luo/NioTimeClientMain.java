package jason.luo;

public class NioTimeClientMain {
    public static void main(String[] args){
        new Thread(new NioTimeClientHandle("localhost", 8001)).start();
    }
}
