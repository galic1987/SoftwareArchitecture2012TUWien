package at.ac.tuwien.software.architectures.ws2012.client.interfaces;


import ac.at.tuwien.infosys.swa.audio.*;

import java.io.*;

import javax.sound.sampled.*;



public class ClientMusicOperations {
	
	public Fingerprint getFingerprintFromAFile(String filepath) throws UnsupportedAudioFileException, IOException{
		
	    File file1 = new File(filepath);
		AudioInputStream inputStream1 = AudioSystem.getAudioInputStream(file1);
		Fingerprint f1 = FingerprintSystem.fingerprint(inputStream1);

		return f1;
	}

}
