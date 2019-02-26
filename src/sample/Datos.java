package sample;


import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Datos {



   public static ArrayList<Parada> getRuta(int id) {
       ArrayList<Parada> ruta = new ArrayList<>();
       String[] nombres = {
               "Camino de Vinateros", "Estrella Polar",
               "Cruz del Sur", "Avenida del Mediterráneo",
               "Paseo Reina Cristina", "Paseo Infanta Isabel",
               "Atocha Renfe", "Atocha",
               "Antón Martín", "Jacinto Benavente"
       };

       double x = 0;
       double y = 0;
       for (String nombre : nombres) {
           Parada parada = new Parada();
           parada.setNombre(nombre);
           x = Math.random() * 100 + 100;
           y += 75;
           parada.setPosicion(new Point2D(x, y));
           ruta.add(parada);
       }

//       ruta.add(new Parada("Camino de Vinateros", 200, 50));
//       ruta.add(new Parada("Estrella Polar", 160, 100));
//       ruta.add(new Parada("Cruz del Sur", 140, 150));
//       ruta.add(new Parada("Avenida del Mediterráneo", 180, 200));
//       ruta.add(new Parada("Paseo Reina Cristina", 200, 250));
//       ruta.add(new Parada("Paseo Infanta Isabel", 250, 300));
//       ruta.add(new Parada("Atocha Renfe", 240, 350));
//       ruta.add(new Parada("Atocha", 240, 400));
//       ruta.add(new Parada("Antón Martín", 250, 420));
//       ruta.add(new Parada("Jacinto Benavente", 240, 490));

       return ruta;
   }
}
