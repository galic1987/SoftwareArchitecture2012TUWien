package music;
import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.*;



public class MusicLibrary implements IMusicLibrary {
	
	private static Logger logger = Logger.getLogger("Peer.music.MusicLibrary");
	private String libPath;
	private Map<String, Fingerprint> library = new HashMap<String, Fingerprint>();
	
	
	/**
	 * Constructor requires path to the library (if null then use default /sampleAudio/)
	 * @param path to files
	 */
	public MusicLibrary(String path){
		if(path == null || path.equals("")){
			// use defaults
			path = System.getProperty("user.dir")+"/sampleAudio/";
		}

		this.libPath = path;
	//	this.updateLibrary();
	}

	/* (non-Javadoc)
	 * @see music.IMusicLibrary#updateLibrary()
	 */
	
	public void updateLibrary(){
		File[] files = listFiles(this.libPath);

		for(File f : files){
			// check 
			if( !library.containsKey(f.getName()) ) {
				library.put(f.getName(), this.getFingerprintFromAFile(f));
			}
		}		
	}
	
	
	
	/**
	 * Lists files in directory 
	 * 
	 * @param path to files
	 * @return all files in that path
	 */
	private File[] listFiles(final String path){
		String files;
		File folder = new File(path);

		File[] listOfAllFiles = folder.listFiles(); 	

		//all files from the library
		for (int i = 0; i < listOfAllFiles.length; i++){
			if (listOfAllFiles[i].isFile()) {
				files = listOfAllFiles[i].getName();
				logger.log(Level.INFO, files);
			}
		}
		return listOfAllFiles;
	}
	
	
	
	/**
	 * Gets fingerprints from a file or thorws a exeption
	 * @param file1
	 * @return
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	
	private Fingerprint getFingerprintFromAFile(File file1) {
		try{
			AudioInputStream inputStream1 = AudioSystem.getAudioInputStream(file1);
			Fingerprint f1 = FingerprintSystem.fingerprint(inputStream1);
			return f1;
		}catch(Exception e){
			logger.log(Level.SEVERE, e.getLocalizedMessage());
			return null;
		}
	}


	/* (non-Javadoc)
	 * @see music.IMusicLibrary#matchSong(ac.at.tuwien.infosys.swa.audio.Fingerprint)
	 */
	public String matchSong(Fingerprint f){
		
		Iterator it = library.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String songName = (String) pairs.getKey();
	        Fingerprint songFingerprint = (Fingerprint) pairs.getValue();
	        
	        double match = f.match(songFingerprint);
	        
	        logger.log(Level.INFO, "Song name checking" + songName + " match value " + match);

	        if(match > -1.0){
	        	return songName;
	        }
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
		
		return "NO";
	}

	public String addMusic(String song) {
		File f = new File(libPath+song);
		library.put(f.getName(), this.getFingerprintFromAFile(f));
		return "Added song with the name" + song + "succesfully";
	}

	public String removeMusic(String song) {
			library.remove(song);
			return "Removed song with the name" + song + "succesfully";

	}

}
