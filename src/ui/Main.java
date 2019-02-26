package ui;


import compartido.Parada;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import servidor.Datos;

import java.util.ArrayList;

public class Main extends Application {

    private Pane layout;


    @Override
    public void start(Stage primaryStage) throws Exception{
        layout = new Pane();



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(layout, 475, 850));
        primaryStage.show();
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
