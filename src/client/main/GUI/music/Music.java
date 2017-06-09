package client.main.GUI.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Andrea
 * @author Luca
 */
public class Music {
    private static final String PATH = "src/client/main/GUI/music/";
    private File claps;
    private Clip clip;
    private AudioInputStream audio;


    public String getPath(){
        return PATH;
    }

    public void play(String resource){
        try {
            claps = new File(resource);
            clip = AudioSystem.getClip();
            audio = AudioSystem.getAudioInputStream(claps);
            clip.open(audio);
            if(resource.equals("MainTheme.wav"))
                clip.loop(10);
            clip.start();
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.out.println("Audio ok");
        }
    }

    public void stop(){
        clip.stop();
    }


}
