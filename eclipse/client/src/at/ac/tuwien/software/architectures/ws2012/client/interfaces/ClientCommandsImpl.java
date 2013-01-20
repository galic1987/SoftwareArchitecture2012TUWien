package at.ac.tuwien.software.architectures.ws2012.client.interfaces;

import at.ac.tuwien.software.architectures.ws2012.client.IClientCommands;

public class ClientCommandsImpl implements IClientCommands{

	public String recognizeSong(String filepath) {
		// TODO here need code for song recognize 
		// if no peer available, request new peer form server
		// then open socket and try to concact him
		return null;
	}

	public String requestNewPeerFromServer(String serverEndpoing) {
		// TODO reqest new peer from server if no peers
		
		
		return null;
	}

}
