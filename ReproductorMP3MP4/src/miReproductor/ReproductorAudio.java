package miReproductor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * @author Pablo Valdivia
 */

public class ReproductorAudio {
    public BasicPlayer player;
    public Inicio inicio;
    
    String ruta, titulo, autor, album, anio;
    long duracion;
    float volumenActual;
    float balance;

    public ReproductorAudio(Inicio inicio) {
        player = new BasicPlayer();
        this.inicio = inicio;
    }
    
    public void Play() throws Exception {
        if(player.getStatus() == 1){
            player.resume();
        }else{
            player.play();
        }
    }

    public boolean AbrirFichero(String ruta) {
        this.ruta = ruta;
        try {
            player.open(new File(this.ruta));
            datosCancion();
            inicio.actualizaDatosCancion();
            return true;
            
        } catch (BasicPlayerException ex) {
            System.out.print("Error en la apertura del fichero");
            return false;
        }
    }

    public void Pausa() throws Exception {
        player.pause();
    }

    public void Continuar() throws Exception {
        player.resume();
        player.setGain(volumenActual);
    }

    public void Stop() throws Exception {
        player.stop();
        player.setGain(volumenActual);
    }
    
    public void Balance(float bal){
        balance = bal;
        try {
            player.setPan(bal);
        } catch (BasicPlayerException ex) {
            Logger.getLogger(ReproductorAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void datosCancion(){
        File mp3Archivo = new File(ruta);
        AudioFileFormat baseFileFormat = null;
        try {
            baseFileFormat = AudioSystem.getAudioFileFormat(mp3Archivo);
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(ReproductorAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(baseFileFormat instanceof TAudioFileFormat){
            Map properties = ((TAudioFileFormat)baseFileFormat).properties();
            titulo=(String)properties.get("title");
            autor=(String)properties.get("author");
            album=(String)properties.get("album");
            anio=(String)properties.get("date");
            duracion=(long)properties.get("duration")/1000000;
        }
    }
    
    public void pulsaProgreso(double posicion) throws BasicPlayerException{
        player.seek((long)posicion*1024);
    }
    
    public void setVolumen(float volumen) throws BasicPlayerException{
        volumenActual = volumen;
        if(player.hasGainControl()){
            player.setGain(volumen);
        }
    }
    
    public float getVolumen() throws BasicPlayerException{
        return player.getGainValue();
    }
}
