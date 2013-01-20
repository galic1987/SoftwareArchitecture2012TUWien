package music;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

/**
 * Interfaces for management of music library
 * @author galic1987
 *
 */

public interface IMusicLibrary {

	/**
	 * Updates files in the library - add new files as well 
	 * deleted will not be deleted
	 */

	public abstract void updateLibrary();

	/**
	 * matches the song in the library against some external fingerprint
	 * @param f Fingerprint from song to be found
	 * @return "NO" if no song found or song name
	 */
	public abstract String matchSong(Fingerprint f);

}