package jojeda;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteUDP extends Application {

//    public static void main(String[] args) {
//        try {
//            byte[] bytes = new byte[1024];
//            InetAddress address = InetAddress.getByName("192.168.1.41");
//            DatagramSocket socket = new DatagramSocket();
//            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 5555);
//
//            System.out.println("Cliente inicializado");
//
//            packet.setData(("Hola, me estoy conectando").getBytes());
//            socket.send(packet);
//
//            while (true) {
//                socket.receive(packet);
//                System.out.println(new String(packet.getData()));
//                Thread.sleep(1000);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }

    private Pane layout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        layout = new Pane();
        try {
            byte[] bytes = new byte[1024];
            InetAddress address = InetAddress.getByName("192.168.1.41");
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 5555);

            System.out.println("Cliente inicializado");

            packet.setData(("Hola, me estoy conectando").getBytes());
            socket.send(packet);
            packet = new DatagramPacket(bytes, bytes.length);

            socket.receive(packet);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            ArrayList<Parada> ruta = null;
            try {
                ruta = (ArrayList<Parada>) ois.readObject();
            } catch (EOFException eof) {
                eof.printStackTrace();
            }
            ois.close();
            pintarRecorrido(ruta);

//            while (true) {
//                socket.receive(packet);
//                System.out.println(new String(packet.getData()));
//                Thread.sleep(1000);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setScene(new Scene(layout, 600, 800));
        primaryStage.show();
    }

//    private void conectarConServidor() {
//        try {
//            // Conectarse
//            Socket socket = new Socket(SERVER_IP, MARQUESINAS_PORT);
//            conexionInicial(socket);
//
//            System.out.println("Marquesina conectada");
//            // Mantenerse actualizado con la informacion de los buses
//            DatagramSocket datagramSocket = new DatagramSocket();
//            byte[] mensajeBytes = ("Marquesina conectada").getBytes();
//            DatagramPacket datagramPacket = new DatagramPacket(mensajeBytes, mensajeBytes.length, InetAddress.getByName(SERVER_IP), MARQUESINAS_PORT);
//            datagramSocket.send(datagramPacket);
//            while (!datagramSocket.isClosed()) {
//                datagramPacket = new DatagramPacket(new byte[1024], 1024);
//                if (datagramPacket.getData().length > 0)
//                    System.out.println("Marquesina: " + new String(datagramPacket.getData()));
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    private void conexionInicial(Socket socket) throws IOException, ClassNotFoundException {
        // Enviar la informacion de la IP
        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
//        salida.println(InetAddress.getLocalHost().getHostAddress());
        salida.println("192.168.1.41");

        // Recibir el recorrido del bus
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
        ArrayList<Parada> ruta = (ArrayList<Parada>) entrada.readObject();
        pintarRecorrido(ruta);
    }

    public void pintarRecorrido(ArrayList<Parada> paradas) {
        Polyline polyline = new Polyline();
        for (Parada parada : paradas) {
            double x = parada.getPosicion().getX();
            double y = parada.getPosicion().getY();
            polyline.getPoints().addAll(x, y);
            Label nombreParada = new Label(parada.getNombre());

            CornerRadii radio =  new CornerRadii(5);
            Insets padding = new Insets(-5);
            nombreParada.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), radio, padding)));
            nombreParada.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, radio, BorderWidths.DEFAULT, padding)));

            layout.getChildren().add(nombreParada);
            nombreParada.relocate(x, y);
        }

        layout.getChildren().add(polyline);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
