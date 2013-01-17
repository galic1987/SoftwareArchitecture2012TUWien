import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;


public class AddressedRequestQueue {
	static Logger log=Logger.getLogger(Connection.class);
	LinkedBlockingQueue<AddressedRequest> queue;

	public AddressedRequestQueue() {
		queue = new LinkedBlockingQueue<AddressedRequest>();
	}
	
	public void addElement(AddressedRequest req)
	{
		queue.add(req);
		log.info(String.format("New message added to queue: %s", req.req.toString()));
	}
	
	public AddressedRequest getElement()
	{
		AddressedRequest req=queue.poll();
		return req;
	}
	
	public int Count()
	{
		return queue.size();
	}
}
