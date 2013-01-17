
public class ServerWorker extends Thread {
	String name;
	ConnectionManager manager;
	public ServerWorker(String n, ConnectionManager m)
	{
		manager = m;
		name = n;
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
				break;
			case BOOTSTRAP_REQUEST:
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
