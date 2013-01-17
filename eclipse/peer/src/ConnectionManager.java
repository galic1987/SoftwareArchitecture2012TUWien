import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;


public class ConnectionManager extends Thread {
	static Logger log=Logger.getLogger(ConnectionManager.class.toString());
	
	ServerSocket listener;
	Map<String,Connection> connectionMap;
	AddressedRequestQueue inqueue;
	AddressedRequestQueue outqueue;
	
	public ConnectionManager(int port, AddressedRequestQueue inq)
	{
		try {
			listener=new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectionMap = new ConcurrentHashMap<String,Connection>();
		inqueue = inq;
		outqueue=new AddressedRequestQueue();
	}
	
	public String GetSocketInfo(Socket sock)
	{
		return String.format("%s:%d", sock.getInetAddress(),sock.getPort());
	}
	
	public void run()
	{
		log.info(String.format("starting Connection manager thread"));
		while (true)
		{
			try {
				Socket sock=listener.accept();
				String info=GetSocketInfo(sock);
				log.info(String.format("Connection established: %s",info));
				Connection conn=new Connection(info, sock, this);
				connectionMap.put(info, conn);
				conn.run();
				
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
	
}