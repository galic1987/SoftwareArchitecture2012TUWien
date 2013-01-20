import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.General.ValidateSearchStatus;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.Server.ValidateSearchRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.ValidateSearchResponse;

public class ConnectionManager extends Thread {
	static Logger log=Logger.getLogger(ConnectionManager.class.toString());
	
	ServerSocket listener;
	Map<String,Connection> connectionMap;
	AddressedRequestQueue inqueue;
	AddressedRequestQueue outqueue;
	
	boolean server;
	PeerManager peerManager;
	
	public ConnectionManager(int port, AddressedRequestQueue inq, AddressedRequestQueue outq, boolean srv)
	{
		try {
			listener=new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectionMap = new ConcurrentHashMap<String,Connection>();
		inqueue = inq;
		outqueue=outq;
		server=srv;
	}
	
	public String GetSocketInfo(Socket sock)
	{
		return String.format("%s:%d", sock.getInetAddress(),sock.getPort());
	}
	
	public String GetRemoteSocketInfo(Socket sock)
	{
		return String.format("%s:%d", sock.getInetAddress().getHostAddress(),sock.getPort());
	}
	
	public String GetLocalSocketInfo(Socket sock)
	{
		return String.format("%s:%d",sock.getLocalAddress().getHostAddress(), sock.getLocalPort());
	}
	
	public void run()
	{
		log.info(String.format("starting Connection manager thread"));
		while (true)
		{
			try {
				Socket sock=listener.accept();
				String info=GetRemoteSocketInfo(sock);
				log.info(String.format("Connection established: %s",info));
				Connection conn=new Connection(info, sock, this, false);
				connectionMap.put(info, conn);
				conn.start();
				
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	public Connection GetConnection(String address)
	{
		if (!connectionMap.containsKey(address))
		{
			log.error("Does not contain connection");
			return null;
		}
		else
			return connectionMap.get(address);
	}

	public AddressedRequestQueue GetInQueue()
	{
		return inqueue;
	}
	
	public AddressedRequestQueue getOutQueue()
	{
		return outqueue;
	}
	
	public Connection newConnection(String addr, boolean server_conn)
	{
		Socket sock;
		Connection conn=null;
		String[] parts=addr.split(":");
		try {
			log.info(String.format("establishing new connection to %s:%s",parts[0],parts[1]));
			sock=new Socket(parts[0], Integer.parseInt(parts[1]));
			String info=GetRemoteSocketInfo(sock);
			log.info(String.format("Connection established: %s",info));
			conn=new Connection(info, sock, this, server_conn);
			connectionMap.put(info, conn);
			conn.start();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public Connection removeConnection(String name) {
		if (connectionMap.containsKey(name))
		{
			Connection conn = connectionMap.remove(name);
			conn.Close();
			return conn;
		}
		return null;
	}

	public void connectionLive(String address, Date date) {
		if (server) 
			return;
		
		if (connectionMap.containsKey(address))
		{
			Connection conn=connectionMap.get(address);
			conn.timestamp=date;
		}
	}

	public void reportDead(String name, String listenaddr) {
		if (server)
			return;
		peerManager.reportDead(name, listenaddr);
		
	}

	public void sendToPeers(Request peersearch) {
		Collection<Connection> connections=connectionMap.values();
		Iterator<Connection> it=connections.iterator();
		while(it.hasNext())
		{
			Connection c=it.next();
			if (!c.server_connection && !c.client_connection)
			{
				log.info(String.format("forwarding search to %s", c.actualAddress));
				outqueue.addElement(new AddressedRequest(peersearch, c.actualAddress, false));
			}
		}
	}
}