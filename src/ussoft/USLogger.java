/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ussoft;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static digiroda.DigiController.config;

public class USLogger extends Logger {
    
    static File directory = new File(config.getProperty("LOGDIR"));
    static private FileHandler fileHTML;
    static private Formatter formatterHTML;
    static private String fileName;
    
    protected USLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
        // TODO Auto-generated constructor stub
        try {
            setup();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   
    static public String setup(String logDirectory) throws IOException {
        directory=new File(logDirectory);
        return setup();        
    }
    
    static public String setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        // logger.setLevel(Level.FINEST);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        
        
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        fileName = directory+"/LOG_" + dateFormat.format(date).toString() + ".log";
       
        //fileHTML = new FileHandler(fileName);
        fileHTML = new FileHandler(fileName,true);
        
        
        // create an HTML formatter
        formatterHTML = new USHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);

        return fileName;

    }

}
