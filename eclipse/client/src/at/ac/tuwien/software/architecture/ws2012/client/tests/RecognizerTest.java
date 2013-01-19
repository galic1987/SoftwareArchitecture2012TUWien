package at.ac.tuwien.software.architecture.ws2012.client.tests;

import org.junit.Test;
import ac.at.tuwien.infosys.swa.audio.*;

import java.io.*;

import javax.sound.sampled.*;


public class RecognizerTest {

	@Test
	public void test() {
		 try {
		

	
		// 1. open some music files
		// file to open
		String sourceFileName1 = "sampleAudio/test1.wav";
		String sourceFileName2 = "sampleAudio/test2.wav";
		String sourceFileName3 = "sampleAudio/test3.wav";
		
	    File file1 = new File(sourceFileName1);
	    File file2 = new File(sourceFileName2);
	    File file3 = new File(sourceFileName3);
		
	   
		AudioInputStream inputStream1 = AudioSystem.getAudioInputStream(file1);
		AudioInputStream inputStream2 = AudioSystem.getAudioInputStream(file2);
		AudioInputStream inputStream3 = AudioSystem.getAudioInputStream(file3);
			

		// 2. create few fingerprints from music files
		Fingerprint f1 = FingerprintSystem.fingerprint(inputStream1);
		Fingerprint f2 = FingerprintSystem.fingerprint(inputStream2);
		Fingerprint f3 = FingerprintSystem.fingerprint(inputStream3);
		
			
		// 3. compare some fingerprints, search for 			
		double t1 = f1.match(f2) ;
		System.out.println("we found some match f1 - f2 number" + t1);
		
		
		double t2 = f2.match(f3) ;
		System.out.println("we found some match f3 - f3 number" + t2);
		
		
		double t3 = f1.match(f1) ;
		System.out.println("we found some match f1 - f1 number" + t3);
		
			
		// 4. subfingerprints? test that what for?
			
			
			
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		
		
	
	}

}
