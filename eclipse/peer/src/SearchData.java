import java.util.Date;

import at.ac.tuwien.software.architectures.ws2012.General.SearchRequest;

import com.google.protobuf.ByteString;


public class SearchData {
	ByteString fingerprint;
	Date timestamp;
	String clientAddress;
	int clientID;
	String originalPeerListen;
	boolean originalPeer;
	SearchRequest request;
}
