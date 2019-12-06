import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {
    private static SoundManager m_instance;
    private Clip m_backgroundMusic;
    private boolean m_isMuted = false;

    //constructor
    private SoundManager() {}

    //get the current SoundManager
    public static SoundManager getInstance() {
        if(m_instance == null){
            m_instance = new SoundManager();
        }
        return m_instance;
    }

    //play background music handler
    public void playBackGroundMusic()
    {
        try{
            m_backgroundMusic = AudioSystem.getClip();
            AudioInputStream ais;
            ais = AudioSystem.getAudioInputStream(new File(Constants.BACKGROUND_MUSIC));
            m_backgroundMusic.open(ais);
            m_backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            setMute(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //mute/unmute music handler
    public void setMute(boolean isMuted){
        m_isMuted = isMuted;
        if(isMuted)
            UIManager.getInstance().setMute();
        else
            UIManager.getInstance().setUnMute();
    }

    //check if the music is mute
    public boolean isMuted(){
        return m_isMuted;
    }

    //mute background music
    public void stopBackgroundMusic(){
        m_backgroundMusic.close();
        setMute(true);
    }

    //mute/unmute backbround music function
    public void toggleBackgroundMusic(){
        if(!isMuted()){
            stopBackgroundMusic();
        }
        else if (isMuted()){
            playBackGroundMusic();
        }
    }

    //play a sound
    public void playWave(String clipName) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais;
            ais = AudioSystem.getAudioInputStream(new File(Constants.SOUND_FILES_PATH + clipName));
            clip.open(ais);
            clip.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
