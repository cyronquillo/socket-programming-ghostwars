package instantiation;
import java.util.ArrayList;
import java.util.HashMap;

public class Missile extends Thread implements Constants{
	private int x;
	private int y;
	private int x_inc;
	private int y_inc;
	private int speed;
	private String src;
	private String direction;
	private ArrayList<Missile> storage;
	private Boolean is_collided;
	GhostWarsServer broadcaster;
	GameState game;
	public Missile(int x, int y, String src, String direction, ArrayList<Missile> storage, GhostWarsServer broadcaster, GameState game){
		this.game = game;
		this.broadcaster = broadcaster;
		this.speed = 1;
		this.is_collided = false;
		this.x = x + 15;
		this.y = y + 15;
		this.src = src;
		this.direction = direction;
		this.storage = storage;
		switch(direction){
			case "Upwards":
				this.y_inc = -speed;
				this.x_inc = 0;
				break;
			case "Downwards":
				this.y_inc = speed;
				this.x_inc = 0;
				break;
			case "Leftwards":
				this.y_inc = 0;
				this.x_inc = -speed;
				break;
			case "Rightwards":
				this.y_inc = 0;
				this.x_inc = speed;
				break;
			default:
				System.out.println("edi wow");
		}
		this.start();
	}

	public void update(){
		this.x += this.x_inc;
		this.y += this.y_inc;
	}
	public String toString(){
		String return_string = "MISSILE " + this.src + " "
							 + this.x + " "
							 + this.y + " "
							 + this.is_collided;
		return return_string;
	}

	public String getSource(){
		return this.src;
	}

	public void setCollided(boolean collide){
		this.is_collided = collide;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}
	public void run(){
		while(this.is_collided == false){
			try { 
				Thread.sleep(4);
				this.update();

				//sprite collision
				HashMap<String, Sprite> playerList = game.getPlayers();
				for(String key: playerList.keySet()){
					Sprite sprite2 = playerList.get(key);
					if(sprite2.getName().equals(this.getSource())){
						continue;
					}
					if(broadcaster.colDect.checkCollision(this, sprite2) == HAS_COLLIDED){
						this.setCollided(true);
						break;
					}	
				}

				//block collision
				if(broadcaster.colDect.checkCollision(this,game.map.getTileMap().getMap())== HAS_COLLIDED){
					this.setCollided(true);
				}



				broadcaster.broadcast(game.toString());
			} catch(Exception e){}

		}
		storage.remove(this);
		broadcaster.broadcast(game.toString());

	}





}