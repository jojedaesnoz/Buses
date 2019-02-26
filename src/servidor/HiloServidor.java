package servidor;

import jojeda.compartido.MensajeBus;
import jojeda.compartido.MensajeServer;
import jojeda.compartido.Parada;
import jojeda.vista.Vista;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {

    private Socket clientSocket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private static int autoincrement;
    private int id;
    private Vista vista;

    public HiloServidor(Socket clientSocket) {
        this.clientSocket = clientSocket;
        id = autoincrement;
        autoincrement++;
    }

    @Override
    public void run() {
        vista = new Vista();
        try {
            salida = new ObjectOutputStream(clientSocket.getOutputStream());
            enviarInformacionInicial();

            // Escucha mientras el bus siga conectado
            entrada = new ObjectInputStream(clientSocket.getInputStream());
            MensajeBus mensajeBus = null;
            try {
                while (!clientSocket.isClosed()) {
                    mensajeBus = (MensajeBus) entrada.readObject();
                    System.out.printf("Tiempo restante a %s %f segundos \n", mensajeBus.getParadaSiguiente().getNombre(), mensajeBus.getTiempo());
                }
            } catch (EOFException e) {
                if (mensajeBus != null) {
                    System.out.println("El bus " + mensajeBus.getNombreBus() + " ha llegado al final de su recorrido.");
                }
            }

            // Libera el hueco para que se conecte otro
            Servidor.conexiones.remove(this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enviarInformacionInicial() throws IOException {
        ArrayList<Parada> ruta = Datos.getRuta(id);
        MensajeServer mensaje = new MensajeServer();
        mensaje.setNombre("32");
        mensaje.setParadas(ruta);
        mensaje.setVelocidad(5);
        salida.writeObject(mensaje);
        vista.pintarParadas(ruta);
    }
}
