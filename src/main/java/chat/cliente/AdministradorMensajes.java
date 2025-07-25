import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Iterator;

public class ClientMessageManager {
    private View view;

    public void processMessage(JSONObject message) {
	String type = message.optString("type");

	switch (type) {
	case "NEW_USER":
	    manageNewUser(message);
	    break;
	case "NEW_STATUS":
	    manageStateChange(message);
	    break;
	case "USER_LIST":
	    manageUserList(message);
	    break;
	case "TEXT_FROM":
	    managePrivateMessage(message);
	    break;
	case "PUBLIC_TEXT_FROM":
	    managePublicMessage(message);
	    break;
	case "INVITATION":
	    manageInvitation(message);
	    break;
	case "JOINED_ROOM":
	    manageUnionRoom(message);
	    break;
	case "ROOM_USER_LIST":
	    manageRoomUserList(message);
	    break;
	case "ROOM_TEXT_FROM":
	    manageRoomText(message);
	    break;
	case "LEFT_ROOM":
	    manageRoomAbandonment(message);
	    break;
	case "DISCONNECTED":
	    manageDisconnection(message);
	    break;
	default:
	    System.out.println("Mensaje desconocido: " + message.toString());
	}
    }

    private void manageNewUser(JSONObject message) {
	String username = message.optString("username");
	view.showMessage("Nuevo usuario conectado: " + username);
    }

    private void manageStateChange() {}

    private void manageUserList() {}

    private void managePrivateMessage() {}

    private void  managePublicMessage() {}

    private void manageInvitation() {}
    
    private void manageUnionRoom() {}
    
    private void manageRoomUserList() {}
    
    private void manageRoomText() {}
    
    private void manageRoomAbandonment() {}
    
    private void manageDisconnection() {}

}
