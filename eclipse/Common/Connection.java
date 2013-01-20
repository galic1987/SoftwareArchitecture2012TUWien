import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General;
import at.ac.tuwien.software.architectures.ws2012.Peer;
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
	public Date timestamp;
	
	static boolean server;
	boolean server_connection;
	
	public Connection(String n, Socket sock, ConnectionManager m, boolean srv_conn)
	{
		log.info("Binding connection to socket");
		socket=sock;
		manager=m;
		name=n;
		server_connection=srv_conn;
		
		registry=ExtensionRegistry.newInstance();
		General.registerAllExtensions(registry);
		Server.registerAllExtensions(registry);
		Peer.registerAllExtensions(registry);
	}
	
	public void run()
	{
		CodedInputStream is;
		try {
			is = CodedInputStream.newInstance(socket.getInputStream());
			
		} catch (IOException e1) {
			RemoveConnection(e1);
			return;
		}
		while (socket.isConnected())
		{
			try {
				long size=is.readRawVarint32();
				int old=is.pushLimit((int) size);
				Request req=Request.parseFrom(is,registry);
				is.popLimit(old);
				manager.GetInQueue().addElement(new AddressedRequest(req, name, server_connection));
			} catch (IOException e) {
				RemoveConnection(e);
				return;
			}
		}
	}
	
	private void RemoveConnection(Exception e) {
		log.warn(String.format("Removing connection, Exception: %s", e.getMessage()));
		manager.removeConnection(name);
		
		if (!server_connection && !server)
			manager.reportDead(name);
		
		try {
			socket.close();
		} catch (IOException e1) {
			log.warn(String.format("error shutting down socket: %s", e1.getStackTrace()));
			return;
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
			RemoveConnection(e);
		}
	}

	public void Close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}