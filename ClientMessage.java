import java.io.Serializable;

public class ClientMessage implements Serializable {
    private String action;
    private String eventDescription;

    public ClientMessage(String action, String eventDescription) {
        this.action = action;
        this.eventDescription = eventDescription;
    }

    public String getAction() {
        return action;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    @Override
    public String toString() {
        return "Action: " + action + ", Event Description: " + eventDescription;
    }
}
