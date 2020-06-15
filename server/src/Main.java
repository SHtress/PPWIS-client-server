import java.awt.*;
import java.lang.Thread;

import controller.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import kek.Model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Application implements Runnable{
    static boolean isRunning=false;
    static Date date=new Date();
    static TextArea logText = new TextArea();
    @Override
    public void start(Stage primaryStage) throws Exception {
        Button startButton = new Button("start");
        Button stopButton = new Button("stop");
        HBox hbox =new HBox(startButton,stopButton);
        Label status=new Label("OFF");
        status.setTextFill(Color.TOMATO);
        VBox vbox = new VBox(status,hbox,logText);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!isRunning) {
                    isRunning = true;
                    Thread t = new Thread(new Main());
                    t.start();
                    status.setText("ON");
                    logText.setText(logText.getText()+date+": server start\n");
                    status.setTextFill(Color.GREEN);
                }
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isRunning) {
                    isRunning = false;
                    try {
                        Socket s = new Socket("127.0.0.1", 2525);
                        status.setText("OFF");
                        status.setTextFill(Color.TOMATO);
                        logText.setText(logText.getText()+date+": server stop\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2525);
            while (isRunning) {
                Thread t = new Thread(new ClientThread(serverSocket.accept()));
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

