package view;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import kek.Model;
import controller.Controller;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class MainWindow extends Application {
    public static void main(String[] args) {
        try {
                Scanner sc = new Scanner(new File("serverIp.properties"));
                String serverIp = sc.nextLine();
                socket = new Socket(serverIp, 2525);
                clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
                clientInputStream = new ObjectInputStream(socket.getInputStream());
                launch(args);
            }
        catch(Exception e)
            {
                e.printStackTrace();
            }
        finally
            {
                try {
                    clientInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    clientOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    private static Socket socket;
    public static ObjectOutputStream clientOutputStream;
    public static ObjectInputStream clientInputStream;

    @Override
    public void start(Stage mainStage) {
        try {
            Model model = new Model(10);
            Controller controller = new Controller(model);
            View view = new View(controller);
            mainStage = view.getStage();
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @Override
    public void stop() throws Exception
    {
        clientOutputStream.writeObject("exit");
    }
}
