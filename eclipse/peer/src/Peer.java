import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		String serverIP=args[1];
		int serverPort=Integer.parseInt(args[2]);
		int numberOfPeers=Integer.parseInt(args[3]);
		int numOfWorkers=Integer.parseInt(args[4]);
		int hopsToLive=Integer.parseInt(args[5]);
		int clientID=Integer.parseInt(args[6]);

		String musicFolder="";
		
		
		int refreshPeriod=10000;
		
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
			//Thread.sleep(100000);
	        
			 

	        
	    // user input begins here
		BufferedReader stdIn = new BufferedReader(
	                                   new InputStreamReader(System.in));
		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
			String[] ccmm = getCommandArray(userInput);
			if(ccmm[0].equals("add")){
				// add the song
				log.info(peerManager.addSong(ccmm[1]));
				
			}else if(ccmm[0].equals("remove")){
				// remove the song
				log.info(peerManager.removeSong(ccmm[1]));


			}else if(ccmm[0].equals("update")){
				// remove the song
				peerManager.updateMusicLibrary();

			}else{
				log.info("remove or add song possible commands");
				
			}
		}

		// close all open connections
		stdIn.close();

		
		
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception" + e.toString() );
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
	
	
	// used to get command 
	private static String getCommandFromLine(String userInput){
			Scanner lineScanner = new Scanner(userInput);
			lineScanner.useDelimiter(" ");
			if(lineScanner.hasNext()){
				return lineScanner.next();
			}else{
				return "";
			}
	}
	
	
	// used to get command array with arguments from one string
	public static String[] getCommandArray(String userInput){
		String [] my_array = new String[32];
		int counter = 0;
		Scanner lineScanner = new Scanner(userInput);
		lineScanner.useDelimiter(" ");
		while(lineScanner.hasNext() && counter < 32){
			my_array[counter] =  lineScanner.next();
			counter = counter + 1;
		}
		return my_array;
	}
}
