
public class ServerWorker extends Thread {
	String name;
	ConnectionManager manager;
	ServerManager srvManager;
	public ServerWorker(String n, ConnectionManager m, ServerManager s)
	{
		manager = m;
		name = n;
		srvManager=s;
	}
	
	public void run()
	{
		while (true)
		{
			AddressedRequest req=manager.GetInQueue().getElement();
			if (req==null)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					continue;				
					}
				continue;
			}
			
			switch(req.req.getRequestType())
			{
			case REGISTER_PEER_REQUEST:
				srvManager.RegisterPeer(req);
				break;
			case BOOTSTRAP_REQUEST:
				srvManager.Bootstrap(req);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				manager.removeConnection(req.address);
				break;
			case PEER_DEAD_REQUEST:
				break;
			case VALIDATE_SEARCH_REQUEST:
				break;
			default:
				break;
			}
		}
	}
}
