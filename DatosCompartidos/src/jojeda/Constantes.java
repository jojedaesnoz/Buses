package jojeda;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Constantes {

    public static final int MAX_BUSES = 3;
    public static final BufferedReader TECLADO = new BufferedReader(new InputStreamReader(System.in));
    public static String SERVER_IP;

    static {
        try {
            SERVER_IP = TECLADO.readLine();
        } catch (IOException e) {
            SERVER_IP = "192.168.255.86";
        }
    }

    public final static int MARQUESINAS_PORT = 5555;
    public final static int BUSES_PORT = 5556;

}
