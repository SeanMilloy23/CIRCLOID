package circlegame;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayMusicThread {

    public static void main(String[] args) {
        String filepath = "candyland.wav";
        PlayMusic(filepath);
        // The program will exit after playing the music
    }

    public static void PlayMusic(String location) {
        try {
            File musicPath = new File(location);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();

                // Wait until the music finishes playing
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.stop();
                clip.close();
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

	public void start() {
		// TODO Auto-generated method stub
	        String filepath = "candyland.wav";
	        PlayMusic(filepath);
	        // The program will exit after playing the music
	    
	}
}
