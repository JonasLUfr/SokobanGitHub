package modele;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Musique {

    //utilisées pour stocker deux objets Clip représentant respectivement deux morceaux de musique de fond.
    // Clip est une classe de l'API Java Sound qui représente un extrait audio.
    private Clip clip1;
    private Clip clip2;

    public void playBackgroundMusic1() {
        try {
            stopCurrentMusic();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Sounds/1.wav").getAbsoluteFile());//obtenir un flux audio à partir d'un fichier audio
            clip1 = AudioSystem.getClip();//créer un nouveau objet Clip, qui sera utilisé pour lire l'extrait audio.
            clip1.open(audioIn);//ouvrir le flux audio et le charger dans l'objet clip1.
            FloatControl gainControl = (FloatControl) clip1.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);//pour contrôler le volume audio, en le réglant sur -20,0 décibels.
            clip1.loop(Clip.LOOP_CONTINUOUSLY);//pour jouer l'extrait audio en boucle.
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();

        }
    }

    public void playBackgroundMusic2() {
        try {
            stopCurrentMusic();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Sounds/background.wav").getAbsoluteFile());
            clip2 = AudioSystem.getClip();
            clip2.open(audioIn);
            FloatControl gainControl = (FloatControl) clip2.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);
            clip2.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopCurrentMusic() {
        if (clip1 != null && clip1.isRunning()) {
            clip1.stop();//arreter la premiere musique
        }
        if (clip2 != null && clip2.isRunning()) {
            clip2.stop();//arreter la deuxieme musique
        }
    }
}