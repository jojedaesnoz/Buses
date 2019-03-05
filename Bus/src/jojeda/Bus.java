package jojeda;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static jojeda.Constantes.*;

public class Bus {

    private static final long TIEMPO_ENTRE_CICLOS = 200;


    public static void main(String[] args) {
        new Bus();
    }

    public Bus() {
        try {
            // Se conecta
            Socket clientSocket = new Socket(SERVER_IP, BUSES_PORT);
            ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream salida = new ObjectOutputStream(clientSocket.getOutputStream());

            // Recoge la informacion inicial
            MensajeServer mensajeServer = (MensajeServer) entrada.readObject();
            String nombre = mensajeServer.getNombre();
            double velocidad = mensajeServer.getVelocidad();
            ArrayList<Parada> paradas = mensajeServer.getParadas();

            // Se mueve
            int indiceParada = 0;
            Point2D posicion = paradas.get(indiceParada).getPosicion();

            do {
                // Calcula la ruta
                Parada ultima = paradas.get(indiceParada);
                Parada siguiente = paradas.get(indiceParada + 1);
                Point2D ruta = siguiente.getPosicion().subtract(ultima.getPosicion()).normalize();

                double distancia;

                // Avanza hasta que llega a la parada
                while ((distancia = posicion.distance(siguiente.getPosicion())) > velocidad) {

                    // Se mueve
                    posicion = posicion.add(ruta.multiply(velocidad));
                    System.out.println(posicion);

                    // Notifica al servidor
                    MensajeBus mensajeBus = new MensajeBus();
                    mensajeBus.setNombreBus(nombre);
                    mensajeBus.setTiempo(distancia/velocidad);
                    mensajeBus.setParadaSiguiente(siguiente);
                    salida.writeObject(mensajeBus);

                    // Tiene un pequeño delay
                    Thread.sleep(TIEMPO_ENTRE_CICLOS);
                }

                // Aumenta el indice de parada
                indiceParada ++;

                // Repite hasta que llega a la ultima parada
            } while (indiceParada < (paradas.size() - 1));


            System.out.println("FIN DE TRAYECTO");
            clientSocket.close();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
