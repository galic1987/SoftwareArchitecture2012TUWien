
package at.ac.tuwein.software.architectures.ws2012.serverListener;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General.Request;


public class RequestQueue {
	static Logger log=Logger.getLogger(Connection.class);
	LinkedBlockingQueue<Request> queue;

	public void addElement(Request req)
	{
		queue.add(req);
		log.info(String.format("New message added to queue: %s", req.toString()));
	}
	
	public Request getElement()
	{
		Request req=queue.remove();
		return req;
	}
	
	public int Count()
	{
		return queue.size();
	}
}
