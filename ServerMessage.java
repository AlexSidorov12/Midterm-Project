import java.io.Serializable;

public class ServerMessage implements Serializable {
    private String responseCode;
    private String responseMessage;

    public ServerMessage(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public String toString() {
        return responseCode + ": " + responseMessage;
    }
}
