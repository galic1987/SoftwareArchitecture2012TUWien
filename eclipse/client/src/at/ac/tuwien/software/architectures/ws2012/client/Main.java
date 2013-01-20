package at.ac.tuwien.software.architectures.ws2012.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistry;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;
import at.ac.tuwien.software.architectures.ws2012.FingerprintUtil;
import at.ac.tuwien.software.architectures.ws2012.General.ClientSearchRequest;
import at.ac.tuwien.software.architectures.ws2012.General.PeerData;
import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapRequest;
import at.ac.tuwien.software.architectures.ws2012.General;
import at.ac.tuwien.software.architectures.ws2012.Peer;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapResponse;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int clientID=Integer.parseInt(args[0]);
		String sourceFileName = args[1];
		String serveraddress=args[2];
		int serverPort=Integer.parseInt(args[3]);
		
		ByteString bs = FingerprintSong(sourceFileName);
		if (bs==null)
			return;
		
		ExtensionRegistry registry = SetupPBRegistry();
		
		String peerAddress=BootstrapFromServer(registry);
		if (peerAddress==null)
			return;
		
		String results=SearchFingerprint(peerAddress, clientID, registry, bs);
		System.out.print(results);
	}

private static String SearchFingerprint(String peerAddress, int clientID, ExtensionRegistry registry,
			ByteString bs) {
	
	ClientSearchRequest csrch=ClientSearchRequest.newBuilder().setClientid(clientID).setFingerprint(bs).build();
	Request searchreq=Request.newBuilder().setRequestId(1001).setListenAddress("").setRequestType(RequestType.CLIENT_SEARCH_REQUEST).setTimestamp((new Date()).getTime()).setExtension(Peer.clientSearchRequest, csrch).build();
	try
	{
		String[] parts=peerAddress.split(":");
		Socket peer=new Socket(parts[0], Integer.parseInt(parts[1]));
		CodedOutputStream peercs=CodedOutputStream.newInstance(peer.getOutputStream());
		peercs.writeRawVarint32(searchreq.getSerializedSize());
		searchreq.writeTo(peercs);
		peercs.flush();
		CodedInputStream peeris=CodedInputStream.newInstance(peer.getInputStream());
		int size=peeris.readRawVarint32();
		int old = peeris.pushLimit(size);
		Request searchResponse=Request.parseFrom(peeris);
		peeris.popLimit(old);
		return searchResponse.toString();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return "";
}

private static String BootstrapFromServer(ExtensionRegistry registry)
{
	BootstrapRequest boots=BootstrapRequest.newBuilder().setNumberOfPeers(1).build();
	Request bootstrapreq=Request.newBuilder().setRequestId(2).setRequestType(RequestType.BOOTSTRAP_REQUEST).setTimestamp((new Date()).getTime()).setListenAddress("").setExtension(Server.bootstrapRequest, boots).build();
	Socket server;
	
	String peerAddress=null;	
	try {
		server = new Socket("127.0.0.1", 5678);
		CodedOutputStream cs=CodedOutputStream.newInstance(server.getOutputStream());
		cs.writeRawVarint32(bootstrapreq.getSerializedSize());
		bootstrapreq.writeTo(cs);
		cs.flush();
		CodedInputStream is=CodedInputStream.newInstance(server.getInputStream());
		int size=is.readRawVarint32();
		int old = is.pushLimit(size);
		Request bootstrapresp=Request.parseFrom(is, registry);
		is.popLimit(old);
		
		BootstrapResponse bootstrapreal=bootstrapresp.getExtension(Server.bootstrapResponse);
		PeerData peerData=bootstrapreal.getData(0);
		peerAddress=peerData.getPeerAddress();
	}
	catch (Exception e)
	{
		
	}
	return peerAddress;
}

    private static ExtensionRegistry SetupPBRegistry() {
		ExtensionRegistry registry;
		registry=ExtensionRegistry.newInstance();
		General.registerAllExtensions(registry);
		Server.registerAllExtensions(registry);
		Peer.registerAllExtensions(registry);
		return registry;
	}
	private static ByteString FingerprintSong(String sourceFileName) {
	    File file = new File(sourceFileName);
	    ByteString bs=null;
	    try
	    {
	    	AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
	    	Fingerprint f = FingerprintSystem.fingerprint(inputStream);
	        byte[] bytes=FingerprintUtil.serializeFingerprint(f);
	        bs=ByteString.copyFrom(bytes);
	    }		
	    catch (Exception e)
	    {
	    	
	    }
		return bs;
	}
  
}
