import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    static final int PORT = 12345;
    private static Map<String, List<String>> classSchedule = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                ClientMessage clientMessage = (ClientMessage) in.readObject();
                System.out.println("Received message from client: " + clientMessage);

                ServerMessage serverResponse = processClientMessage(clientMessage);
                out.writeObject(serverResponse);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ServerMessage processClientMessage(ClientMessage clientMessage) {
        String action = clientMessage.getAction();
        String[] details = clientMessage.getEventDescription().split(",");

        switch (action) {
            case "Add Class":
                return handleAddClass(details);
            case "Remove Class":
                return handleRemoveClass(details);
            case "Display Schedule":
                return handleDisplaySchedule(details);
            case "STOP":
                return new ServerMessage("TERMINATE", "Communication terminated.");
            default:
                return new ServerMessage("Error", "Invalid action.");
        }
    }

    private static ServerMessage handleAddClass(String[] details) {
        String date = details[0];
        String time = details[1];
        String room = details[2];
        String className = details[3];

        String key = date + "-" + time;
        if (!classSchedule.containsKey(key)) {
            classSchedule.put(key, new ArrayList<>());
        }

        List<String> classesAtSlot = classSchedule.get(key);
        if (!classesAtSlot.contains(className)) {
            classesAtSlot.add(className);
            return new ServerMessage("Success", "Class added successfully.");
        } else {
            return new ServerMessage("Error", "Class schedule conflict.");
        }
    }

    private static ServerMessage handleRemoveClass(String[] details) {
        String date = details[0];
        String time = details[1];
        String room = details[2];
        String className = details[3];

        String key = date + "-" + time;
        if (classSchedule.containsKey(key)) {
            List<String> classesAtSlot = classSchedule.get(key);
            if (classesAtSlot.contains(className)) {
                classesAtSlot.remove(className);
                return new ServerMessage("Success", "Class removed successfully.");
            } else {
                return new ServerMessage("Error", "Class not found at the specified slot.");
            }
        } else {
            return new ServerMessage("Error", "Invalid slot.");
        }
    }

    private static ServerMessage handleDisplaySchedule(String[] details) {
        String className = details[0];

        StringBuilder schedule = new StringBuilder("Schedule for " + className + ":\n");

        for (Map.Entry<String, List<String>> entry : classSchedule.entrySet()) {
            String slot = entry.getKey();
            List<String> classesAtSlot = entry.getValue();
            if (classesAtSlot.contains(className)) {
                schedule.append(slot).append("\n");
            }
        }

        return new ServerMessage("Display Schedule", schedule.toString());
    }
}
