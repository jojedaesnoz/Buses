package jojeda;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import static jojeda.Constantes.*;

public class ServidorMain extends Application {

    public  ArrayList<HiloBuses> conexionesBuses;
    public  ArrayList<String> ipsMarquesinas;
    public  ArrayList<DatagramPacket> packets;
    byte[] buffer = new byte[1024];
    DatagramSocket socketParadas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        conexionesBuses = new ArrayList<>();
        ipsMarquesinas = new ArrayList<>();
        packets = new ArrayList<>();
        byte[] rutaSerializada = serializar(getRuta());

        // Permitir que se conecten los autobuses
        new Thread(() -> {
            try {
                ServerSocket socketBuses = new ServerSocket(BUSES_PORT);
                while (!socketBuses.isClosed() && conexionesBuses.size() < MAX_BUSES) {
                    HiloBuses conexionBus = new HiloBuses(socketBuses.accept(), this);
                    conexionesBuses.add(conexionBus);
                    conexionBus.start();
                    System.out.println("Conexión aceptada");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        // Permitir que se conecten las paradas
        new Thread(() -> {
            try {
                DatagramPacket packetEntradas = new DatagramPacket(buffer, buffer.length);
                socketParadas = new DatagramSocket(MARQUESINAS_PORT);
                while (!socketParadas.isClosed()) {
                    System.out.println("Recibiendo paquete inicial");
                    socketParadas.receive(packetEntradas);
                    System.out.println("Recibido");
                    InetAddress address = packetEntradas.getAddress();
                    int port = packetEntradas.getPort();
                    System.out.println("Enviando ruta de los autobuses");
                    DatagramPacket packetParada = new DatagramPacket(rutaSerializada, rutaSerializada.length, address, port);
                    socketParadas.send(packetParada);

                    packetParada = new DatagramPacket(buffer, buffer.length, address, port);
                    packets.add(packetParada);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public  void notificarParadas(MensajeBus mensajeBus) throws IOException {
        for (DatagramPacket packet : packets) {
            packet.setData(serializar(mensajeBus));
            socketParadas.send(packet);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private  byte[] serializar(Object object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return byteStream.toByteArray();
    }

    public  ArrayList<Parada> getRuta() {
        ArrayList<Parada> ruta = new ArrayList<>();

        ruta.add(new Parada("Camino de Vinateros", 200, 75));
        ruta.add(new Parada("Estrella Polar", 160, 150));
        ruta.add(new Parada("Cruz del Sur", 140, 225));
        ruta.add(new Parada("Avenida del Mediterráneo", 180, 300));
        ruta.add(new Parada("Paseo Reina Cristina", 200, 375));
        ruta.add(new Parada("Paseo Infanta Isabel", 250, 450));
        ruta.add(new Parada("Atocha Renfe", 240, 525));
        ruta.add(new Parada("Atocha", 240, 600));
        ruta.add(new Parada("Antón Martín", 250, 675));
        ruta.add(new Parada("Jacinto Benavente", 240, 750));

        return ruta;
    }
}
