/**
 * @authors Smeet Shah & Akash Patel
 */

package app;

import java.util.Comparator;

public class Song{
	String songName;
	String albumName;
	String albumYear;
	String artistName;
	
	public Song(String songName, String albumName, String artistName, String albumYear) {
		this.songName = songName;
		this.albumName = albumName;
		this.artistName = artistName;
		this.albumYear = albumYear;
	}
	
	@Override
	public String toString() {
		return this.songName +" ," + this.artistName;
	}
	
	public static Comparator<Song> songNameComp = new Comparator<Song>() {

		public int compare(Song song1, Song song2) {
		   String songName1 = song1.getSongName().toLowerCase();
		   String songName2 = song2.getSongName().toLowerCase();
		   
		   int result = songName1.compareTo(songName2);
		   
		   if(result == 0) {
			   String artistName1 = song1.getArtistName().toLowerCase();
			   String artistName2 = song2.getArtistName().toLowerCase();
			   result = artistName1.compareTo(artistName2);
		   }
		   

		   return result;

		}
		
	};
	
	public String getAlbumYear() {
		return this.albumYear;
	}
	
	public String getSongName() {
		return this.songName;
	}
	
	public String getAlbumName() {
		return this.albumName;
	}
	
	public String getArtistName() {
		return this.artistName;
	}
	
}
