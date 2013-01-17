import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class Server_Mock {
	static Logger log=Logger.getLogger(Server_Mock.class.toString());
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		int port = Integer.parseInt(args[0]);
		log.info(String.format("Server starting on port %d",port));
		AddressedRequestQueue inqueue=new AddressedRequestQueue();
		
		List<ServerWorker> workers=new ArrayList<ServerWorker>();
		ConnectionManager manager= new ConnectionManager(port, inqueue);
		for (int i=0;i<10;i++)
		{
			ServerWorker worker=new ServerWorker(String.valueOf(i), manager);
			workers.add(worker);
			worker.start();
		}
		
		manager.start();
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String GetSocketInfo(Socket sock)
	{
		return String.format("%s:%d", sock.getInetAddress(),sock.getPort());
	}
	

}
