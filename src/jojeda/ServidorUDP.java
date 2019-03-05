package jojeda;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class ServidorUDP {

    public static void main(String[] args) {
        try {
            byte[] bytes = new byte[1024];
            InetAddress address = InetAddress.getByName("192.168.1.41");
            DatagramSocket socket = new DatagramSocket(5555);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

            System.out.println("Servidor inicializado");

            socket.receive(packet);
            System.out.println(new String(packet.getData()));
            System.out.println("Enviando la ruta");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(byteArrayOutputStream);
            oo.writeObject(getRuta());
            oo.close();

            byte[] rutaSerializada = byteArrayOutputStream.toByteArray();
            System.out.println(rutaSerializada.length);
            packet = new DatagramPacket(rutaSerializada, rutaSerializada.length, packet.getAddress(), packet.getPort());
            socket.send(packet);

//            int i = 0;
//            while (true){
//                packet.setData(("Mensaje del servidor (" + i + ")").getBytes());
//                i++;
//                socket.send(packet);
//                Thread.sleep(500);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Parada> getRuta() {
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
