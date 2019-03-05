package jojeda;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Random;

import static jojeda.Constantes.*;

public class HiloBuses extends Thread {

    private Socket cliente;
    private ServidorMain servidor;
    private static int autoincrement;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    public HiloBuses(Socket cliente, ServidorMain servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
        try {
            // Enviar la informacion inicial
            salida = new ObjectOutputStream(cliente.getOutputStream());
            MensajeServer mensajeInicial = new MensajeServer();
            autoincrement++;
            mensajeInicial.setNombre("Autobús " + autoincrement);
            mensajeInicial.setParadas(servidor.getRuta());
            mensajeInicial.setVelocidad(new Random().nextInt(10));
            salida.writeObject(mensajeInicial);
            System.out.println("Autobus inicializado (servidor): OK");

            // Recibir actualizaciones del bus mientras este conectado
            entrada = new ObjectInputStream(cliente.getInputStream());
            MensajeBus mensajeBus = null;
            try {
                while (!cliente.isClosed()) {
                    mensajeBus = (MensajeBus) entrada.readObject();
                    servidor.notificarParadas(mensajeBus);
                }
            } catch (EOFException e) {
                if (mensajeBus != null) {
                    System.out.println("El bus " + mensajeBus.getNombreBus() + " ha llegado al final de su recorrido.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
    }
}
