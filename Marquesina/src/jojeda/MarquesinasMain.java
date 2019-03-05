package jojeda;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import static javafx.application.Platform.runLater;
import static jojeda.Constantes.MARQUESINAS_PORT;
import static jojeda.Constantes.SERVER_IP;

public class MarquesinasMain extends Application {
    private Pane layout;
    private boolean operativo;
    private ArrayList<TextField> textFields;
    private ArrayList<Parada> paradas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        layout = new Pane();
        operativo = true;
        textFields = new ArrayList<>();
        conectarConServidor();


        primaryStage.setScene(new Scene(layout, 600, 800));
        primaryStage.show();
    }

    private void conectarConServidor() {
        try {
            // Conexion inicial
            byte[] buffer = new byte[1024];
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packetInicial = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getByName(SERVER_IP), MARQUESINAS_PORT);

            System.out.println("Enviando paquete inicial");
            socket.send(packetInicial);
            System.out.println("Enviado");

            // Recoger la ruta de los autobuses
            System.out.println("Recibiendo ruta de los autobuses");
            final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            System.out.println("Recibida");
            paradas = (ArrayList<Parada>) deserializar(packet.getData());
            pintarRecorrido(paradas);

            // Recibir mensajes de actualizacion
            new Thread(() -> {
                while (!socket.isClosed() && operativo) {
                    try {
                        socket.receive(packet);
                        MensajeBus mensajeBus = (MensajeBus) deserializar(packet.getData());
                        int posicion = paradas.indexOf(mensajeBus.getParadaSiguiente());

                        runLater(() -> {
                            if (posicion > 0)
                                textFields.get(posicion - 1).setText("");
                            String texto = String.format("Tiempo restante: %.2f segundos", mensajeBus.getTiempo());
                            textFields.get(posicion).setText(texto);
                        });
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("FIN");
        operativo = false;
    }

    private Object deserializar(byte[] datos) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(datos));
        Object objeto = ois.readObject();
        ois.close();
        return objeto;
    }

    public void pintarRecorrido(ArrayList<Parada> paradas) {
        Polyline polyline = new Polyline();
        for (Parada parada : paradas) {
            double x = parada.getPosicion().getX();
            double y = parada.getPosicion().getY();
            polyline.getPoints().addAll(x, y);
            Label paradaLabel = new Label(parada.getNombre());
            TextField paradaTextField = new TextField();
            textFields.add(paradaTextField);

            CornerRadii radio =  new CornerRadii(5);
            Insets padding = new Insets(-5);
            paradaLabel.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), radio, padding)));
            paradaLabel.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, radio, BorderWidths.DEFAULT, padding)));

            HBox caja = new HBox(10, paradaLabel, paradaTextField);
            layout.getChildren().add(caja);
            caja.relocate(x, y);
        }
        layout.getChildren().add(polyline);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
