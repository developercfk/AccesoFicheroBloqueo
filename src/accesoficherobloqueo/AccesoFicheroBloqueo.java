/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesoficherobloqueo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 *
 * @author chris
 */
public class AccesoFicheroBloqueo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
       
PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("ficheroLog.txt"), true)), true);
System.setOut(ps);
System.setErr(ps);


 RandomAccessFile raf = null; 
 File archivo = null; 
 FileLock bloqueo = null;
       
 String nombreFichero = null; 
 int orden = 0;
 int valor = 0;    

if(args.length == 0){
    System.err.println("No hay ningun parametro!");
    System.exit(-1);
}else{
   nombreFichero = args[0];
 
}



if(args.length == 2){
 
  orden = Integer.parseInt(args[1]);
}else{
    System.err.println("No hay de segundo argumento!");
    System.exit(-1);
}



 
        
        
        
  //Abrir el fichero
        archivo = new File(nombreFichero);
        if (!archivo.exists()) {
        System.err.println("El archivo " + nombreFichero + " no existe o no se ha podido abrir");
        System.exit(-1);
        }      
        

        
        try {
raf = new RandomAccessFile(archivo, "rwd"); //Abrimos el fichero
//Empieza sección crítica
bloqueo = raf.getChannel().lock();
System.out.println("*P " + orden + " ENTRA en sección crítica");
// Lectura del fichero
valor = Integer.parseInt(raf.readLine()); //leemos el valor
System.out.println("---p"+orden+" Valor leído del fichero: " + valor);
valor++;
raf.seek(0); //volvemos a colocarnos al principio del fichero
raf.writeBytes(String.valueOf(valor)); //escribimos el valor
System.out.println("---P"+orden+" Valor escrito en el fichero: " + valor);
bloqueo.release(); //Liberamos el bloqueo del canal del fichero
System.out.println("P" + orden + " SALE sección");
//Fin sección crítica
} catch (Exception e) {
System.err.println("P"+orden+" Error al leer el fichero");
}

//Cerrar fichero
try {
if (bloqueo != null) {
bloqueo.release();
}
if (raf != null) {
raf.close();
}
} catch (IOException ex) {
System.err.println("P" + orden + " Error al cerrar el fichero");
}

    }
    
}
