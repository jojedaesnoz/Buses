package compartido;

import java.io.Serializable;

public class MensajeBus implements Serializable {

    private String nombreBus;
    private double tiempo;
    private Parada paradaSiguiente;

    public MensajeBus() {
    }

    public MensajeBus(String nombre, double tiempo) {
        this.nombreBus = nombre;
        this.tiempo = tiempo;
    }

    public String getNombreBus() {
        return nombreBus;
    }

    public void setNombreBus(String nombreBus) {
        this.nombreBus = nombreBus;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public Parada getParadaSiguiente() {
        return paradaSiguiente;
    }

    public void setParadaSiguiente(Parada paradaSiguiente) {
        this.paradaSiguiente = paradaSiguiente;
    }
}
