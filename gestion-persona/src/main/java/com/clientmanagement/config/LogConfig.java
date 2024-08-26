
package com.clientmanagement.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Rafael
 */
public class LogConfig {
    //Maneja archivos para escribir los logs en un archivo.
    private static FileHandler fileHandler;
    
    //Bloque que se ejecuta al cargar la clase
    static{
        try{
            //configuro un onico FileHandler para toda la aplicacion.
            fileHandler = new FileHandler("gestion-persona.log", true);
           //Defino formato simple para  los logs
            fileHandler.setFormatter(new SimpleFormatter());
        
        } catch (IOException e) {
            Logger.getGlobal().severe("No se configuro el archivo de log: " + e);
        }
    }
    
    //Devuelve un logger configurado para la clas e que se especifique
    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        //asocia el fileHandler la logger
        logger.addHandler(fileHandler);
        return logger;
    }
}
