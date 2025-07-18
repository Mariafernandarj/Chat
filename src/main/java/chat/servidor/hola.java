public void inviteUsers(Socket client, JSONObject message) {
    String roomName = message.optString();
    
    try {
	
	PrintWriter out =  new PrintWriter(client.getOutputStream(), true);
	     
	if (!rooms.containsKey(roomName)) {
	    JSONObject answer = new JSONObject();
	    answer.put("type", "RESPONSE");
	    answer.put("operation", "INVITE");
	    answer.put("result", "NO_SUCH_ROOM");
	    answer.put("extra", roomName);

	    out.println(answer.toString());
	    return();
	}

	JSONArray guestsJson = message.getJSONArray("username");
	List<String> guests = new ArrayList<>();
	for (int i = 0; i < guestsJson.length(); i++) {
	    guests.add(guestsJson.getString(i));
	}
	
	for (String guest : guests) {
	    if (users.containsKey(guest)) {
		if (!invitations.containsKey(roomName)) {
		    invitations.put(roomName, nem ArrayList<>());
		}
		invitations.get(roomName).add(guest);
		
		JSONObject invitation = new JSONObject();
		invitation.put("type", "INVITATION");
		invitation.put("username", guest );
		invitation.put("roomname", roomName);

		Socket invalidClient = (Socket) users.get(guest).get("client");
		PrintWriter outGuest =  new PrintWriter(invalidClient.getOutputStream(), true);
		invalidGuest.println(invitation.toString());
	    } else {
		JSONObject answer = new JSONObject();
		answer.put("type", "RESPONSE");
		answer.put("operation", "INVITE");
		answer.put("result", "NO_SUCH_USER");
		answer.put("extra", guest);
			 
		out.println(respuesta.toString());
		return;
	    }
	} 
    } catch (Exception e) {
	e.printStackTrace();
    }
}
