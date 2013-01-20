import java.util.Iterator;

import org.apache.log4j.Logger;


public class PeerJobRunner extends Thread {
	static Logger log=Logger.getLogger(PeerJobRunner.class.toString());
	ConnectionManager connManager;
	PeerManager peerManager;
	int period;
	public PeerJobRunner(ConnectionManager connmanager, PeerManager peermanager, int periodinmilliseconds)
	{
		connManager=connmanager;
		peerManager=peermanager;
		period=periodinmilliseconds;
	}
	public void run()
	{
		while(true)
		{
			log.info("ping");
			Iterator<Connection> it=connManager.connectionMap.values().iterator();
			while (it.hasNext())
			{
				Connection c=it.next();
				if (!c.server_connection && !c.client_connection)
					peerManager.checkAlive(c.actualAddress);
			}
			
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
