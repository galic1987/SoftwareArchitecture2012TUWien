import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;


public class AddressedRequestQueue {
	static Logger log=Logger.getLogger(Connection.class);
	LinkedBlockingQueue<AddressedRequest> queue;

	String name;
	
	public AddressedRequestQueue(String n) {
		queue = new LinkedBlockingQueue<AddressedRequest>();
		name =n;
	}
	public void addElement(AddressedRequest req)
	{
		log.debug(String.format("message arrived in queue: %s", name));
		queue.add(req);
	}
	
	public AddressedRequest getElement()
	{
		AddressedRequest req=queue.poll();
		if (req!=null)
			log.debug(String.format("message picked up from queue: %s", name));
		return req;
	}
	
	public int Count()
	{
		return queue.size();
	}
}
