import at.ac.tuwien.software.architectures.ws2012.General.Request;

public class AddressedRequest {
	Request req;
	String address;
	boolean server_conn;
	public AddressedRequest(Request r, String a, boolean srv_conn) {
		req=r;
		address = a;
		server_conn=srv_conn;
	}
}
