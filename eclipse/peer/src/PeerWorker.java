import at.ac.tuwien.software.architectures.ws2012.General;
import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.General.SearchRequest;
import at.ac.tuwien.software.architectures.ws2012.Peer;


public class PeerWorker extends Thread {
	String name;
	ConnectionManager manager;
	PeerManager peerManager;
	public PeerWorker(String n, ConnectionManager m, PeerManager mgr)
	{
		manager = m;
		name = n;
		peerManager=mgr;
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
				peerManager.AreYouAlive(req);
				break;
			case ARE_YOU_ALIVE_RESPONSE:
				peerManager.AreYouAliveResponse(req);
				break;
			case SEARCH_REQUEST:
				SearchRequest search=req.req.getExtension(Peer.searchRequest);
				int clientid = search.getClientid();
				break;
			case SEARCH_ABORT:
				break;
			case SEARCH_SUCCESFULL:
				break;
			case MONITORING_GET_PEERS_REQUEST:
				break;
			case MONITORING_REQUESTS_PROCESSED_REQUEST:
				break;
			case BOOTSTRAP_RESPONSE:
				peerManager.BootstrapResponse(req);
				break;
			case REGISTER_PEER_RESPONSE:
				peerManager.RegisterPeer(req);
			default:
				break;
			}
		}
	}

}
