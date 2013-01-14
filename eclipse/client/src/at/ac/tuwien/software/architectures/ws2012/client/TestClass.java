package at.ac.tuwien.software.architectures.ws2012.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.ServerRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.ServerRequestType;

public class TestClass {

	public TestClass() throws IOException
	{
		//more information here:
		//https://developers.google.com/protocol-buffers/docs/proto
		//
		//and especially here, regarding information about how "extensions" are used:
		//https://developers.google.com/protocol-buffers/docs/proto#extensions
		//
		//check out style guide if you want to create your own messages:
		//https://developers.google.com/protocol-buffers/docs/style
		
		//note that the "real" message is stored in a "container" message - first create the real message
		//we use bootstrap message to ask server for one peer address
		BootstrapRequest req=BootstrapRequest.newBuilder().
				setNumberOfPeers(1).
				build();
		//when everything is set we call build and message is done
		
		//now we create "container" message and store real message inside (as a so called extension)
		ServerRequest highlevelmsg=ServerRequest.newBuilder().
				setReqId(230).
				setServerRequestType(ServerRequestType.BOOTSTRAP_REQUEST).
				setExtension(Server.bootstrapRequest, req).
				build();
	
		CodedOutputStream os = null;
		//then send
		os.writeRawVarint32(highlevelmsg.getSerializedSize());
		//then finally encode the message after the size
		highlevelmsg.writeTo(os);

		
		
		//meanwhile, on the other side:
		
		CodedInputStream is=null;
		while (true)
		{
			//let's see how long is the next message
			int size=is.readRawVarint32();
			//this is some internal stuff - we limit how much data can be read from input stream - otherwise, the whole content of input stream would be parsed and not just the content of the next message
			int previous=is.pushLimit(size);
			ServerRequest receivedreq=ServerRequest.parseFrom(is);
			//now we can restore reading limit to what it was before we started reading last message
			is.popLimit(previous);
			
			//now do whatever processing needs to be done with received message
			
			//and when we get to the end of block, whole process is repeated for the next message, basically creating a message processing loop
			
		}
	}
}
