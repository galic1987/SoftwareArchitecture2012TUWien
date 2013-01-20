import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.Server.RegisterPeerRequest;

public class Peer {

	static Logger log=Logger.getLogger(ConnectionManager.class);
	
	public static void main(String[] args) {

		BasicConfigurator.configure();
		
		int port = Integer.parseInt(args[0]);
		
		int serverPort=5678;
		String serverIP="127.0.0.1";
		
		int numberOfPeers=5;
		String musicFolder="";
		
		int numOfWorkers=3;
		
		int refreshPeriod=10000;
		int hopsToLive=3;
		int clientID=100;
		
		String serverAddress=String.format("%s:%d", serverIP, serverPort);
		String localIP=GetLocalIP();
		String localaddress=String.format("%s:%d",localIP,port);

		log.info(String.format("Peer starting on port %d",port));
		
		AddressedRequestQueue inqueue=new AddressedRequestQueue("inqueue");
		AddressedRequestQueue outqueue=new AddressedRequestQueue("outqueue");
		
		List<PeerWorker> workers=new ArrayList<PeerWorker>();
		ConnectionManager manager= new ConnectionManager(port, inqueue, outqueue, false);
		PeerManager peerManager= new PeerManager(outqueue, serverAddress, numberOfPeers, musicFolder, localaddress, manager, hopsToLive, clientID);
		manager.peerManager=peerManager;
		
		PeerJobRunner runner=new PeerJobRunner(manager, peerManager, refreshPeriod);
		QueueSender sender=new QueueSender(outqueue, manager);
		
		for (int i=0;i<numOfWorkers;i++)
		{
			PeerWorker worker=new PeerWorker(String.valueOf(i), manager, peerManager);
			workers.add(worker);
			worker.start();
		}
		
		manager.start();
		sender.start();
		runner.start();
		
		ConnectToServer(outqueue, serverAddress, localaddress);
		
		log.info("waiting");
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void ConnectToServer(AddressedRequestQueue queue, String serveraddress, String localAddress) {
		RegisterPeerRequest ext=RegisterPeerRequest.newBuilder().setClientId(100).setPeerAddress(localAddress).build();
		Request req=Request.newBuilder().setRequestId(1).setRequestType(RequestType.REGISTER_PEER_REQUEST).setListenAddress(localAddress).setExtension(Server.registerPeerRequest, ext).build();
		
		queue.addElement(new AddressedRequest(req, serveraddress, true));
	}
	
	public static String GetLocalIP()
	{
		InetAddress i=null;
		try {
			i = InetAddress.getLocalHost();
			i=InetAddress.getByName(i.getHostName());
		} catch (UnknownHostException e) {

		}
		return i.getHostAddress();
	}
}
