package cow.tests;

import shared.util.Log;
import cow.db.PlayerDAO;
import cow.model.Player;
import cow.db.SessionDAO;
import cow.model.Session;

public class SessionTests {
	
	Player player = null;
	Session session = null;

	public SessionTests() {

	}
	
	public Player getPlayer() {
		
		this.player = SessionDAO.sessionPlayer("AAASSS123");
		
		return this.player;
	}
	
	public Session getSession() {
		
		this.session = SessionDAO.playerSession(142);
		
		return this.session;
	}
	
	public void printData() {
		Log.println(this.getSession().toString());
		Log.println(this.getPlayer().getName());
	}
	
	public void run() {
		this.printData();
	}

	public static void main(String[] args) {
		SessionTests tests = new SessionTests();
		tests.run();
		
		System.exit(0);

	}

}
