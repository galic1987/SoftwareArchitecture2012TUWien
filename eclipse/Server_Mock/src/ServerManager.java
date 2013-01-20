import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import at.ac.tuwien.software.architectures.ws2012.General.PeerRegistrationStatus;
import at.ac.tuwien.software.architectures.ws2012.General.Request;
import at.ac.tuwien.software.architectures.ws2012.General.Request.Builder;
import at.ac.tuwien.software.architectures.ws2012.General.RequestType;
import at.ac.tuwien.software.architectures.ws2012.General.SearchStatus;
import at.ac.tuwien.software.architectures.ws2012.General.ValidateSearchStatus;
import at.ac.tuwien.software.architectures.ws2012.General;
import at.ac.tuwien.software.architectures.ws2012.Server;
import at.ac.tuwien.software.architectures.ws2012.General.SearchRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.BootstrapResponse;
import at.ac.tuwien.software.architectures.ws2012.Server.PeerDeadRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.RegisterPeerRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.RegisterPeerResponse;
import at.ac.tuwien.software.architectures.ws2012.Server.ValidateSearchRequest;
import at.ac.tuwien.software.architectures.ws2012.Server.ValidateSearchResponse;

public class ServerManager {
	static Logger log=Logger.getLogger(ServerManager.class.toString());
	Map<String, PeerData> peerMap;
	Map<Long, ClientData> clientMap;
	Map<Long, SearchRequest> activeSearches;
	
	int searchCost;
	int finderReward;
	
	AddressedRequestQueue outQueue;
	
	public ServerManager(AddressedRequestQueue queue, int SearchCost, int FinderReward)
	{
		peerMap = new ConcurrentHashMap<String, PeerData>();
		clientMap = new ConcurrentHashMap<Long,ClientData>();
		
		outQueue=queue;
		
		searchCost = SearchCost;
		finderReward= FinderReward;
	}
	
	public synchronized void RegisterPeer(AddressedRequest req)
	{
		Request request=req.req;
		RegisterPeerRequest registerPeer=request.getExtension(Server.registerPeerRequest);
		
		int clientID = registerPeer.getClientId();
		String peerAddr = request.getListenAddress();
		PeerData peer=new PeerData(); peer.clientID=clientID; peer.peerAddress=req.address; peer.peerListenAddress=peerAddr;

		log.info(String.format("Peer registered. address: %s clientid: %d listenOn: %s", peer.peerAddress, peer.clientID, peer.peerListenAddress));
		peerMap.put(peer.peerListenAddress, peer);
		
		RegisterPeerResponse regresp=RegisterPeerResponse.newBuilder().setPeerAddress(peerAddr).setStatus(PeerRegistrationStatus.PEER_OK).build();
		Request resp=Request.newBuilder().setRequestId(request.getRequestId()).setRequestType(RequestType.REGISTER_PEER_RESPONSE).setTimestamp((new Date()).getTime()).setExtension(Server.registerPeerResponse, regresp).build();
		outQueue.addElement(new AddressedRequest(resp, req.address, false));
	}
	
	public synchronized void UnregisterPeer(AddressedRequest req)
	{
		Request request=req.req;
		PeerDeadRequest peerDead=request.getExtension(Server.peerDeadRequest);
		String deadpeer=peerDead.getDestinationPeer();
		String reporterpeer=req.address;
		
		if (peerMap.containsKey(deadpeer))
		{
			log.info(String.format("Peer %s reported as dead by peer %s",deadpeer, reporterpeer));
			peerMap.remove(deadpeer);
		}
		else
		{
			log.info(String.format("Duplicate: Peer %d reported as dead by peer %d",deadpeer, reporterpeer));
		}
	}
	
	public synchronized void Bootstrap(AddressedRequest req)
	{
		Request request=req.req;
		BootstrapRequest bootstrap = request.getExtension(Server.bootstrapRequest);
		
		String peerAddr=req.address;
		int peernum=bootstrap.getNumberOfPeers();
		long reqid=request.getRequestId();
		String peerListenAddress="";
		if (req.req.hasListenAddress())
			peerListenAddress=req.req.getListenAddress();
		
		log.info(String.format("Bootstrap requested address:%s numOfPeers:%d peerlisten:%s", peerAddr, peernum, peerListenAddress));

		//different strategies could be employed here. 
		//we are just taking random peers from the map and giving their contacts out. 
		//more involved methods for building overlay network based on connectedness, geographical/network distance etc
		//can be used.
		Builder mainBuilder = Request.newBuilder().
				setRequestId(reqid).
				setRequestType(RequestType.BOOTSTRAP_RESPONSE).
				setTimestamp((new Date()).getTime());
		BootstrapResponse.Builder bsrespBuilder = BootstrapResponse.newBuilder();

		Object[] keys= peerMap.keySet().toArray();
		int numOfKeys=keys.length;
		Set<String> keysToSend=new HashSet<String>();
		int counter=0;
		Random rnd=new Random();
		while (counter<numOfKeys && keysToSend.size()<peernum )
		{
			String key=(String) keys[rnd.nextInt(numOfKeys)];
			if (!key.equals(peerListenAddress) && (!keysToSend.contains(key)))
				keysToSend.add(key);
			counter++;
		}
		
		Iterator<String> it =keysToSend.iterator();
		while (it.hasNext())
		{
			String key=it.next();
			PeerData peer=peerMap.get(key);
			General.PeerData data=General.PeerData.newBuilder().setClientId(peer.clientID).setPeerAddress(peer.peerListenAddress).build();
			bsrespBuilder.addData(data);
		}
		mainBuilder.setExtension(Server.bootstrapResponse, bsrespBuilder.build());
		Request response = mainBuilder.build();
		outQueue.addElement(new AddressedRequest(response, peerAddr, false));
		
		log.debug(response.toString());
		log.info(String.format("Bootstrapping found %d unique peers to send out of %d requested (%d total)",keysToSend.size(), peernum, numOfKeys));
	}
	
	public synchronized void ApproveSearch(AddressedRequest req)
	{
		Request request=req.req;
		SearchRequest srch=request.getExtension(Server.validateSearchRequest).getSearchRequest();
		
		long clientID= srch.getClientid();
		long reqid=request.getRequestId();
		String peerAddr=req.address;
		
		log.info(String.format("Search approval requested reqid:%d address:%s clientid:%d", reqid, peerAddr, clientID ));
		
		Builder responseBuilder=Request.newBuilder().
				setRequestId(reqid).
				setRequestType(RequestType.VALIDATE_SEARCH_RESPONSE).
				setTimestamp((new Date()).getTime());
		ValidateSearchResponse validation;
		if (clientMap.containsKey(clientID))
		{
			ClientData data=clientMap.get(clientID);
			if (data.numOfCoins<searchCost)
			{
				log.info(String.format("Client %d could not perform search, insufficient coins: %d",clientID, data.numOfCoins));
				validation=ValidateSearchResponse.newBuilder().setSearchStatus(ValidateSearchStatus.SEARCH_DENIED).build();
			}
			else
			{
				log.info(String.format("Client %d charged %d coins for the search, coins left: %d",clientID, searchCost, data.numOfCoins));
				data.numOfCoins-=searchCost;
				validation=ValidateSearchResponse.newBuilder().setSearchStatus(ValidateSearchStatus.SEARCH_OK).build();
				activeSearches.put(reqid, srch);
			}
		}
		else
		{
			log.info(String.format("Client %d not found in the list.",  clientID));
			validation=ValidateSearchResponse.newBuilder().setSearchStatus(ValidateSearchStatus.SEARCH_ERROR).build();
		}
		Request response=responseBuilder.setExtension(Server.validateSearchResponse, validation).build();
		log.debug(response.toString());
		outQueue.addElement(new AddressedRequest(response, req.address, false));
	}
	
	public void SearchDone(SearchStatus status)
	{
		
	}
}
