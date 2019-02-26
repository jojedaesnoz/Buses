package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import static jojeda.compartido.Constantes.*;

public class Servidor {

    public static ArrayList<HiloServidor> conexiones;

    public static void main(String[] args) {
        conexiones = new ArrayList<>();

        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress address = new InetSocketAddress(SERVER_IP, SERVER_PORT);
            serverSocket.bind(address);
            System.out.println("Servidor operativo");

            while (conexiones.size() < MAX_BUSES) {
                HiloServidor conexion = new HiloServidor(serverSocket.accept());
                conexion.start();
                conexiones.add(conexion);
                System.out.println("Conexión aceptada");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
