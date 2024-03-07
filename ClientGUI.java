import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientGUI extends Application {
    static InetAddress host;
    static final int PORT = 12345;
    Label label = new Label("Response From Server Will Display Here");
    TextField actionField = new TextField();
    TextField dateField = new TextField();
    TextField timeField = new TextField();
    TextField roomField = new TextField();
    TextField classField = new TextField();
    Button sendButton = new Button("Send Request");

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        sendButton.setOnAction(event -> sendMessageToServer());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Action:"), actionField,
                new Label("Date:"), dateField,
                new Label("Time:"), timeField,
                new Label("Room:"), roomField,
                new Label("Class:"), classField,
                sendButton,
                label
        );

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Class Scheduler Client");
        primaryStage.show();
    }

    private void sendMessageToServer() {
        try (Socket socket = new Socket("localhost", PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            String action = actionField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String room = roomField.getText();
            String className = classField.getText();

            ClientMessage message = new ClientMessage(action, String.format("%s,%s,%s,%s,%s", date, time, room, className));
            out.writeObject(message);

            ServerMessage serverResponse = (ServerMessage) in.readObject();
            updateResponseLabel(serverResponse.toString());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateResponseLabel(String response) {
        label.setText(response);
    }
}
