import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;


public class PeerWorker extends Thread {
	String name;
	ConnectionManager manager;
	public PeerWorker(String n, ConnectionManager m)
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
			case ARE_YOU_ALIVE_REQUEST:
				Request response = Request.newBuilder().
					setRequestType(RequestType.ARE_YOU_ALIVE_RESPONSE).
					setRequestId(req.req.getRequestId()).
					build();
				manager.GetConnection(req.address).Send(response);
				break;
			case SEARCH_REQUEST:
				break;
			case SEARCH_ABORT:
				break;
			case SEARCH_SUCCESFULL:
				break;
			case MONITORING_GET_PEERS_REQUEST:
				break;
			case MONITORING_REQUESTS_PROCESSED_REQUEST:
				break;
			default:
				break;
			}
		}
	}

}
