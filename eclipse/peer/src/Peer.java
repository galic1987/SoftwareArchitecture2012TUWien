import java.io.IOException;
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
		log.info(String.format("Peer starting on port %d",port));
		AddressedRequestQueue inqueue=new AddressedRequestQueue();
		
		List<PeerWorker> workers=new ArrayList<PeerWorker>();
		ConnectionManager manager= new ConnectionManager(port, inqueue);
		for (int i=0;i<10;i++)
		{
			PeerWorker worker=new PeerWorker(String.valueOf(i), manager);
			workers.add(worker);
			worker.start();
		}
		
		manager.start();
		
		RegisterPeerRequest ext=RegisterPeerRequest.newBuilder().setClientId(100).setPeerAddress("sdfsdf").build();
		Request req=Request.newBuilder().setRequestId(1).setRequestType(RequestType.REGISTER_PEER_REQUEST).setExtension(Server.registerPeerRequest, ext).build();
		
		try {
			Socket sock=new Socket("127.0.0.1", 5678);
			CodedOutputStream os = CodedOutputStream.newInstance(sock.getOutputStream());
			os.writeRawVarint32(req.getSerializedSize());
			req.writeTo(os);
			CodedInputStream is = CodedInputStream.newInstance(sock.getInputStream());
			int size = is.readRawVarint32();
			is.pushLimit(size);
			Request resp=Request.parseFrom(is);
			is.popLimit(size);
			log.info(resp.toString());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
