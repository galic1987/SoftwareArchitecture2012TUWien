import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.General.Request;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistry;


public class Connection extends Thread {
	static Logger log=Logger.getLogger(Connection.class);
	Socket socket;
	ConnectionManager manager;
	String name;
	
	ExtensionRegistry registry;
	public Connection(String n, Socket sock, ConnectionManager m)
	{
		log.info("Binding connection to socket");
		socket=sock;
		manager=m;
		name=n;
		
		registry=ExtensionRegistry.newInstance();
		General.registerAllExtensions(registry);
		Server.registerAllExtensions(registry);

	}
	
	public void run()
	{
		CodedInputStream is;
		try {
			is = CodedInputStream.newInstance(socket.getInputStream());
			
		} catch (IOException e1) {
			log.error(String.format("could not get stream from socket: %s",e1.getMessage()));
			return;
		}
		while (socket.isConnected())
		{
			try {
				long size=is.readRawVarint32();
				int old=is.pushLimit((int) size);
				Request req=Request.parseFrom(is,registry);
				is.popLimit(old);
				log.info("Extracted message from socket, forwarding to queue");
				
				manager.GetInQueue().addElement(new AddressedRequest(req, name));
			} catch (IOException e) {
				log.error(e.getMessage());
				break;
			}
		}
	}
	
	public void Send(Request req)
	{
		CodedOutputStream os;
		try {
			os = CodedOutputStream.newInstance(socket.getOutputStream());
			os.writeRawVarint32(req.getSerializedSize());
			req.writeTo(os);
			os.flush();
		} catch (IOException e) {
			log.error(String.format("Error writing to output stream: %s", e.getMessage()));
		}
		
	}
}