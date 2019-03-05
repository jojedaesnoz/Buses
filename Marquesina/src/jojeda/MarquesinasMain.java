package jojeda;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import static jojeda.Constantes.*;

public class MarquesinasMain extends Application {
    private Pane layout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        layout = new Pane();
        conectarConServidor();

    }

    private void conectarConServidor() {
        try {
            // Conexion inicial
            byte[] buffer = new byte[1024];
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getByName(SERVER_IP), MARQUESINAS_PORT);

            System.out.println("Enviando paquete inicial");
            socket.send(packet);
            System.out.println("Enviado");

            // Recoger la ruta de los autobuses
            System.out.println("Recibiendo ruta de los autobuses");
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            System.out.println("Recibida");
            ArrayList<Parada> paradas = (ArrayList<Parada>) deserializar(packet.getData());
            pintarRecorrido(getRuta());
            System.out.println("Pintadao");

            // Recibir mensajes de actualizacion
            while (!socket.isClosed()) {
                socket.receive(packet);
                MensajeBus mensajeBus = (MensajeBus) deserializar(packet.getData());

                String mensaje = "Tiempo restante a "
                        + mensajeBus.getParadaSiguiente().getNombre()
                        + ": " + mensajeBus.getTiempo() + " segundos";
                System.out.println(mensaje);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object deserializar(byte[] datos) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(datos));
        Object objeto = ois.readObject();
        ois.close();
        return objeto;
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
        primaryStage.setScene(new Scene(layout, 600, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
