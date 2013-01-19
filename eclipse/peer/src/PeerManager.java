import java.util.Date;

import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General.PeerRegistrationStatus;
import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.RegisterPeerResponse;


public class PeerManager {
	static Logger log=Logger.getLogger(PeerManager.class.toString());	
	
	String serverAddress;
	int numOfPeers;
	String musicfolder;
	AddressedRequestQueue outqueue;
	String listenAddress;
	
	public PeerManager(AddressedRequestQueue out, String srvr, int peers, String music, String localadd)
	{
		serverAddress=srvr;
		numOfPeers=peers;
		musicfolder=music;
		outqueue = out;
		listenAddress=localadd;
	}
	
	public void RegisterPeer(AddressedRequest req) {
		Request request=req.req;
		RegisterPeerResponse regResponse=request.getExtension(Server.registerPeerResponse);
		
		PeerRegistrationStatus status = regResponse.getStatus();
		if (status==PeerRegistrationStatus.PEER_OK)
		{
			BootstrapRequest boots=BootstrapRequest.newBuilder().setNumberOfPeers(numOfPeers).build();
			Request main=Request.newBuilder().setRequestId(2).setRequestType(RequestType.BOOTSTRAP_REQUEST).setTimestamp((new Date()).getTime()).setListenAddress(listenAddress).setExtension(Server.bootstrapRequest, boots).build();
			outqueue.addElement(new AddressedRequest(main, serverAddress));
		}
	}
	
	public void BootstrapResponse(AddressedRequest req)
	{
		Request request=req.req;
		at.ac.tuwien.software.architectures.ws2012.Server.BootstrapResponse bsresponse=request.getExtension(Server.bootstrapResponse);
		
		log.info(request.toString());
	}
}
