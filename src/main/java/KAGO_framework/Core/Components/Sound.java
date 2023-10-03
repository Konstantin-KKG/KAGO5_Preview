package KAGO_framework.Core.Components;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.Sound.SoundHandler;
import javafx.scene.media.MediaPlayer;

public class Sound extends Component {
    private String filename;
    private boolean looping = false;
    private MediaPlayer mediaPlayer;

    public Sound(String filename){
        this.filename = filename;
        SoundHandler.ExecLogicImminently(this);
    }

    public Sound(String filename, boolean looping){
        this.filename = filename;
        this.looping = looping;
        SoundHandler.ExecLogicImminently(this);
    }

    public void Start(){
        if(mediaPlayer != null)
            mediaPlayer.play();
        else
            Debug.Log("The Sound component was already deleted", LogType.ERROR);
    }

    public void Stop(){
        if(mediaPlayer != null)
            mediaPlayer.stop();
        else
            Debug.Log("The Sound component was already deleted", LogType.ERROR);
    }

    public void Delete(){
        mediaPlayer.dispose();
        mediaPlayer = null;
    }

    public String getFilename(){
        return filename;
    }

    public boolean getLooping(){
        return looping;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }
}
