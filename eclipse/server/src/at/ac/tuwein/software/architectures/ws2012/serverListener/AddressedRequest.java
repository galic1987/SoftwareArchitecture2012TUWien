package at.ac.tuwein.software.architectures.ws2012.serverListener;


import at.ac.tuwien.software.architectures.ws2012.General.Request;

public class AddressedRequest {
	Request req;
	String address;
	public AddressedRequest(Request r, String a) {
		req=r;
		address = a;
	}
}
