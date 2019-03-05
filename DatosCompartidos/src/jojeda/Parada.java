package jojeda;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Parada implements Serializable {

    private String nombre;
    Point2D posicion;

    public Parada() {
    }

    public Parada(String nombre, Point2D posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
    }

    public Parada(String nombre, double x, double y) {
        this.nombre = nombre;
        this.posicion = new Point2D(x, y);
    }

    // Como Point2D no es serializable, implementamos estos metodos para hacer que parada lo sea
    private void writeObject(ObjectOutputStream salida) throws IOException {
        salida.writeUTF(nombre);
        salida.writeDouble(posicion.getX());
        salida.writeDouble(posicion.getY());
    }

    private void readObject(ObjectInputStream entrada) throws IOException {
        nombre = entrada.readUTF();
        double x = entrada.readDouble();
        double y = entrada.readDouble();
        posicion = new Point2D(x, y);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Point2D getPosicion() {
        return posicion;
    }

    public void setPosicion(Point2D posicion) {
        this.posicion = posicion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Parada))
            return false;
        return this.nombre.equals(((Parada) obj).nombre);
    }
}
