import org.apache.log4j.Logger;


public class QueueSender extends Thread {
	static Logger log=Logger.getLogger(QueueSender.class.toString());
	AddressedRequestQueue outqueue;
	ConnectionManager connManager;
	
	public QueueSender(AddressedRequestQueue q, ConnectionManager manager)
	{
		outqueue= q;
		connManager=manager;
	}
	
	public void run()
	{
		while (true)
		{
			if (outqueue.Count()>0)
			{
				AddressedRequest req=outqueue.getElement();

				Connection conn = connManager.GetConnection(req.address);
				if (conn==null)
					conn=connManager.newConnection(req.address, req.server_conn);

				conn.Send(req.req);
				if (req.server_conn)
					log.info("message sent to server");
				else
					log.info("message sent");

				if (connManager.server)
				{
					//log.info("this is server, sending reply and removing connection");
					//connManager.removeConnection(conn.name);
				}
			}
			else
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
