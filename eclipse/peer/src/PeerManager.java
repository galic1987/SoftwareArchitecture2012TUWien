import java.net.Authenticator.RequestorType;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General.PeerData;
import at.ac.tuwien.software.architectures.ws2012.General.PeerRegistrationStatus;
import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.Peer.AreYouAliveRequest;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.Peer;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.PeerDeadRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.RegisterPeerResponse;


public class PeerManager {
	static Logger log=Logger.getLogger(PeerManager.class.toString());	
	
	String serverAddress;
	int numOfPeers;
	String musicfolder;
	AddressedRequestQueue outqueue;
	String listenAddress;
	int reqid;
	ConnectionManager connManager;
	
	public PeerManager(AddressedRequestQueue out, String srvr, int peers, String music, String localadd, ConnectionManager mgr)
	{
		serverAddress=srvr;
		numOfPeers=peers;
		musicfolder=music;
		outqueue = out;
		listenAddress=localadd;
		reqid=0;
		connManager = mgr;
	}
	
	public void RegisterPeer(AddressedRequest req) {
		Request request=req.req;
		RegisterPeerResponse regResponse=request.getExtension(Server.registerPeerResponse);
		
		PeerRegistrationStatus status = regResponse.getStatus();
		if (status==PeerRegistrationStatus.PEER_OK)
		{
			BootstrapRequest boots=BootstrapRequest.newBuilder().setNumberOfPeers(numOfPeers).build();
			Request main=Request.newBuilder().setRequestId(2).setRequestType(RequestType.BOOTSTRAP_REQUEST).setTimestamp((new Date()).getTime()).setListenAddress(listenAddress).setExtension(Server.bootstrapRequest, boots).build();
			outqueue.addElement(new AddressedRequest(main, serverAddress, true));
		}
	}
	
	public void BootstrapResponse(AddressedRequest req)
	{
		Request request=req.req;
		at.ac.tuwien.software.architectures.ws2012.Server.BootstrapResponse bsresponse=request.getExtension(Server.bootstrapResponse);
		log.info(String.format("Received bootstrap response: %d contacts", bsresponse.getDataCount()));
		
		List<PeerData> peerlist = bsresponse.getDataList();
		Iterator<PeerData> it=peerlist.iterator();
		while(it.hasNext())
		{
			PeerData peer=it.next();
			String peerAddress=peer.getPeerAddress();
			log.info(String.format("Trying to connect to peer %s", peerAddress));
			checkAlive(peerAddress);
		}
	}

	public void AreYouAlive(AddressedRequest req) {
		Request request=req.req;
		AreYouAliveRequest areUalive=request.getExtension(Peer.areYouAliveRequest);
		
		String peerAddress=req.address;
		String listenAddress=request.getListenAddress();

		if (!req.server_conn)
			UpdateActual(peerAddress, listenAddress);
		
		Request response = Request.newBuilder().
				setRequestType(RequestType.ARE_YOU_ALIVE_RESPONSE).
				setRequestId(request.getRequestId()).
				setListenAddress(listenAddress).
				setTimestamp((new Date()).getTime()).
				build();
		outqueue.addElement(new AddressedRequest(response, peerAddress, false));
	}

	private void UpdateActual(String peerAddress, String listenaddr) {
		Connection conn;
		conn=connManager.GetConnection(peerAddress);
		if (conn!=null)
		{
			conn.listenAddress=listenaddr;
			conn.actualAddress=peerAddress;
		}
		else 
		{
			log.error("IMPOSSIBLE cannot have this");
		}
	}

	public void AreYouAliveResponse(AddressedRequest req) {
		Request request=req.req;
		at.ac.tuwien.software.architectures.ws2012.Peer.AreYouAliveResponse resp=request.getExtension(Peer.areYouAliveResponse);
		
		String actual=req.address;
		String listen=request.getListenAddress();
		UpdateActual(actual, listen);
		
		connManager.connectionLive(req.address, new Date());
	}

	public void reportDead(String name, String listen) {
		PeerDeadRequest peerDead=PeerDeadRequest.newBuilder().setDestinationPeer(listen).build();
		Request request=Request.newBuilder().setRequestId(++reqid).setListenAddress(listenAddress).setRequestType(RequestType.PEER_DEAD_REQUEST).setExtension(Server.peerDeadRequest, peerDead).build();
		
		log.info(String.format("Reporting dead peer: %s", name));
		outqueue.addElement(new AddressedRequest(request, serverAddress, true));
	}

	private String GetListen(String name) {
		Connection conn;
		conn=connManager.GetConnection(name);
		if (conn!=null)
			return conn.listenAddress;
			
		return "";
	}

	public void checkAlive(String address) {
		AreYouAliveRequest areyoualive=AreYouAliveRequest.newBuilder().setDestinationPeer(listenAddress).build();
		Request containerreq=Request.newBuilder().setRequestId(++reqid).setListenAddress(listenAddress).setRequestType(RequestType.ARE_YOU_ALIVE_REQUEST).setTimestamp((new Date()).getTime()).setExtension(Peer.areYouAliveRequest, areyoualive).build();
		outqueue.addElement(new AddressedRequest(containerreq, address, false));
	}
}
