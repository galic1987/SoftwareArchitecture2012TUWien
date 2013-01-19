
public class QueueSender extends Thread {
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
				if (conn!=null)
				{
					conn.Send(req.req);
				}
				else
				{
					String address=req.address.split(":")[0];
					int port = Integer.parseInt(req.address.split(":")[1]);
					connManager.newConnection(address, port);
					conn=connManager.GetConnection(req.address);
					conn.Send(req.req);
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
