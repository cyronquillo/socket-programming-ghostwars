package instantiation;

import javax.swing.*;
import java.io.IOException;

public class GhostWars implements Constants{	
	public static void main(String [] args){
		Object[] selectionValues = { "Create Game","Join Game"};
		String initialSection = "Create Game";
		String server = "localhost";
		
		Object selection = JOptionPane.showInputDialog(null, "Login as : ", "GHOST WARS", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSection);
		if(selection.equals("Create Game")){
			String numberOfPlayers = JOptionPane.showInputDialog("Enter the number of Players");
			new GhostWarsServer(Integer.parseInt(numberOfPlayers));
			new ServerChat();
		}else if(selection.equals("Join Game")){
			server = JOptionPane.showInputDialog("Enter the Server ip adress");
		}

		String name = JOptionPane.showInputDialog("Enter your name");

		GhostWarsClient game = new GhostWarsClient(server, name);

		try {
			game.access.InitSocket(server, PORT, name);
			game.begin();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}