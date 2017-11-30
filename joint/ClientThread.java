package instantiation;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.net.Socket;
import java.io.IOException;

public class ClientThread extends Thread{
	private BufferedReader input;
	private PrintStream output;
	private Socket cSocket;
	public ClientThread(Socket cSocket){
		this.cSocket = cSocket;
	}

	public void run(){
		try{
			input = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			output = new PrintStream(cSocket.getOutputStream());
			String usr = cSocket.getRemoteSocketAddress().toString();


			//notify other clients that a new client has entered
			output.println("Hello " + usr + "!\n[NOTE: type 'bye' and press Enter to leave the room anytime]");
			
			for(int i = 0; i < ServerChat.threads.size(); i++){
				if(ServerChat.threads.get(i) != this){
					ServerChat.threads.get(i).output.println(usr +" has entered the chat room!");
				}
			}

			// message sending
			while(true){
				String line = input.readLine();
				if(line.equals("bye")) {
					break;
				}
				synchronized(this){
					for(int i = 0; i < ServerChat.threads.size(); i++){
						ServerChat.threads.get(i).output.println("[" + usr + "]: " + line);
					}
				}
			}


			// notify other clients that a client has left
			for(int i = 0; i < ServerChat.threads.size(); i++){
				if(ServerChat.threads.get(i) != this){
					ServerChat.threads.get(i).output.println(usr + " has left the chat room :(");
				}
			}

			output.println("You have left the chat room.");


			ServerChat.threads.remove(this);


			input.close();
			output.close();
			cSocket.close();
		} catch (IOException ioe){
			System.out.println(ioe);
		}
	}


}