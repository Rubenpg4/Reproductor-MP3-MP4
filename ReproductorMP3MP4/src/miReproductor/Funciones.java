package miReproductor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import it.sauronsoftware.jave.*;

/**
 * @author Ruben Prieto
 */

public class Funciones {
    
    /**
     * FUNCION ABRIR ARCHIVO
     * 
     * @param tipo: mensaje que muestra el tipo de archivo que queremos abrir
     * @param formato: formato del archivo que vamos a abrir (mp3)
     * @return devuelve un vector de string con los archivos que hemos seleccionado para abrir
     *          en caso de que no pueda abrir el archivo devuelve un valor nulo (NULL)
     *         
     */
    public static String[] abrirArchivo(String tipo,String[] formato){
        JFileChooser fich=new JFileChooser();
        fich.setMultiSelectionEnabled(true);
        fich.setFileFilter(new FileNameExtensionFilter(tipo,formato));

        if(fich.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            File archivos[]=fich.getSelectedFiles();
            String rutas[]=new String[archivos.length];
            
            for(int i=0; i<archivos.length; i++){
                rutas[i]= archivos[i].getPath();
            }
            return rutas;
        }
        return null;
    }
    
    /**
     * FUNCION AGREGAR ARCHIVO
     * 
     * @param tipo: mensaje que muestra el tipo de archivo que queremos abrir
     * @param formato: formato del archivo que vamos a abrir (mp3)
     * @param rutas: vector de rutas con las canciones que ya tenemos en el reproductor
     * @return Devuelve un nuevo vector, con las canciones que ya teniamos reproduciendose
     *          y las canciones nuevas que deseamos agregar a la lista de reproduccion
     */
    public static String[] agregarArchivo(String tipo,String formato, String []rutas){
        JFileChooser fich=new JFileChooser();
        fich.setMultiSelectionEnabled(true);
        fich.setFileFilter(new FileNameExtensionFilter(tipo,formato));

        if(fich.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            File archivos[]=fich.getSelectedFiles();
            String aux[] = new String[archivos.length+rutas.length];
            
            //Primero las que ya teniamos
            for(int i=0; i<rutas.length;i++){
                aux[i] = rutas[i];
            }
            
            //Luego las nuevas
            int j=0;
            for(int i=rutas.length; i<(archivos.length+rutas.length); i++){
                aux[i]= archivos[j].getPath();
                j++;
            }
            return aux;
        }
        return null;
    }
    
    /**
     * FUNCION ARBIR CARPETA
     * 
     * @return Devuelve un vector con las canciones que contiene la capeta seleccionada
     */
    public static String[] abrirCarpeta(){        
        JFileChooser carpeta=new JFileChooser();
        carpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
        if(carpeta.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            String rutaCarpeta=carpeta.getSelectedFile().getPath();
            File directorio = new File(rutaCarpeta);
            String listaDirectorio[] = directorio.list();
            
            for(int i=0; i<listaDirectorio.length; i++){
                listaDirectorio[i]=rutaCarpeta+"\\"+listaDirectorio[i];
            }
            return listaDirectorio;
        }else{
            return null;
        }    
    }
    
    /**
     * FUNCION AGREGAR CARPETA
     * 
     * @param rutas: vector con las canciones que hay actualmente en el reproductor
     * @return Devuelve un vector con las nuevas canciones de la carpeta 
     *          seleccionada junto con las canciones que habia antes en el reproductor
     * 
     */
    public static String[] agregarCarpeta(String []rutas){        
        JFileChooser carpeta=new JFileChooser();
        carpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
        if(carpeta.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            String rutaCarpeta=carpeta.getSelectedFile().getPath();
            File directorio = new File(rutaCarpeta);
            String listaDirectorio[] = directorio.list();
            
            for(int i=0; i<listaDirectorio.length; i++){
                listaDirectorio[i]=rutaCarpeta+"\\"+listaDirectorio[i];
            }
            String aux[] = new String[listaDirectorio.length+rutas.length];
            
            for(int i=0; i<rutas.length;i++){
                aux[i] = rutas[i];
            }
            
            int j=0;
            for(int i=rutas.length; i<(listaDirectorio.length+rutas.length); i++){
                aux[i]= listaDirectorio[j];
                j++;
            }
            
            return aux;
        }else{
            return null;
        }   
    }
    
    /**
     * @param segundos: total de duracion de la cancion en segundos
     * @return devuelve el tiempo en horas,minutos y segundos
     */
    public static String tiempo(long segundos){
        long seg = segundos%60;
        long min = segundos/60;
        long hor = min/60;
        
        String segundo;
        String minuto;
        String hora;
        
        segundo = Objects.toString(seg, null);
        minuto = Objects.toString(min, null);
        hora = Objects.toString(hor, null);
        
        if(seg < 10) segundo = "0"+segundo;
        if(min < 10) minuto = "0"+minuto;
        
        if(hor != 0){
            min = min%60;
            return hora+":"+minuto+":"+segundo;
        }
        
        return minuto+":"+segundo;
    }
    
    /**
     * Lee de un fichero las rutas de las canciones que contiene
     * @param fichero
     * @return devuelve un vector con las rutas de las canciones
     */
    public static String[] leerFichero(String fichero){
        File fich = new File(fichero);
        try {
            fich.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numLineas = numLineasFich(fichero);
        String canciones[] = new String[numLineas];
        try {
            
            BufferedReader reader = new BufferedReader(new FileReader(fichero));
            for(int i=0; i<numLineas; i++){
                canciones[i] = reader.readLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return canciones;
    }
    
    /**
     * Dado un fichero, devuelve el numero de lineas que contiene (numero de canciones)
     * @param fichero
     * @return
     */
    public static int numLineasFich(String fichero){
        int numLineas=0;
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(fichero));
            String linea;
            
            while ((linea = br.readLine())!=null) {
                numLineas++;
            }
            br.close();
            
            } catch (IOException ex) {
                Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        return numLineas;
    }
    
    /**
     * Dado un nombre, crea un fichero de play list con ese nombre
     * @param nombre 
     */
    public static void aniadirPlaylists(String nombre){
        try {
            File file = new File(nombre);
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Borra las canciones de una play lists
     * @param nombre 
     */
    public static void borraPlaylist(String nombre){
        
        try {
            File file = new File(nombre);
            BufferedWriter fich = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            fich.write("");
            
            fich.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Añade canciones al fichero de canciones de la lista
     * @param nombre: nombre del fichero donde se guardaran las canciones
     */
    public static void aniadirCancionPlaylist(String nombre){
        try{
            JFileChooser fich=new JFileChooser();
            fich.setMultiSelectionEnabled(true);
            fich.setFileFilter(new FileNameExtensionFilter("Archivos con extension MP3","mp3"));

            if(fich.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                File archivos[]=fich.getSelectedFiles();
                String rutas[]=new String[archivos.length];

                for(int i=0; i<archivos.length; i++){
                    rutas[i]= archivos[i].getPath();
                }
                
            File file = new File(nombre);
            BufferedWriter fichero = new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true));

            for(int i=0; i< rutas.length;i++){
                fichero.write(rutas[i]+"\r\n");
            }
            fichero.close();
            
            }
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Añade carpeta de canciones al fichero de canciones de la lista
     * @param nombre: nombre del fichero de la lista
     */
    public static void aniadirCarpetaPlaylist(String nombre){
        try{
            JFileChooser carpeta=new JFileChooser();
            carpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if(carpeta.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                String rutaCarpeta=carpeta.getSelectedFile().getPath();
                File directorio = new File(rutaCarpeta);
                String listaDirectorio[] = directorio.list();

                for(int i=0; i<listaDirectorio.length; i++){
                    listaDirectorio[i]=rutaCarpeta+"\\"+listaDirectorio[i];
                }
                
            File file = new File(nombre);
            BufferedWriter fichero = new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true));

            for(int i=0; i< listaDirectorio.length;i++){
                fichero.write(listaDirectorio[i]+"\r\n");
            }
            fichero.close();
            
            }
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Escribe en un fichero dado, la ruta de la cancion que se le pasa
     * @param nombre
     * @param canciones 
     */
    public static void escribirPlaylistFich(String nombre, String canciones[]){
        try{
            File file = new File(nombre);
            BufferedWriter fich = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            
            for(int i=0; i< canciones.length;i++){
                fich.write(canciones[i]+"\r\n");
            }
            fich.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Guarda en un fichero de texto el nombre de las playlists para futuras ejecuciones
     * @param nombresPlaylists 
     */
    public static void guardaNombrePlaylists(String[] nombresPlaylists) {
        try{
            File file = new File("nombrePlaylists.txt");
            BufferedWriter fich = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            
            for(int i=0; i< nombresPlaylists.length;i++){
                fich.write(nombresPlaylists[i]+"\r\n");
            }
            fich.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Convierte un archivo de una extension de audio a MP3
     * @param source: Archivo a transformar
     * @param target: Archivo transfromado a MP3
     */
    public static void convertToMP3(File source, File target) {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(new Integer(128000));
        audio.setChannels(new Integer(2));
        audio.setSamplingRate(new Integer(44100));

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        Encoder encoder = new Encoder();
        try {
            encoder.encode(source, target, attrs);
            System.out.println("Conversión realizada con éxito");
        } catch (IllegalArgumentException | EncoderException e) {
            e.printStackTrace();
            System.err.println("Error en la conversión de audio");
        }
    }

}

