package error.logger;

import java.util.logging.Logger;
import java.text.MessageFormat;

public class EhrLogger {
	
	public enum Style {TEXT, HTML, XML};
	
      public static void log(Logger logger, Throwable t, Style style){
    	 
    	  String trace = getTrace(t, style);
    	  String messages = getMessages(t,style);
    	  logger.info(MessageFormat.format("Messages:\n {0}",messages));
    	  logger.info(MessageFormat.format("Trace:\n {0}", trace));
    	      	  
      }
      
      public static String getTrace(Throwable throwable,  Style style){
    	  
    	  Throwable t = throwable;
    	  String line = "";
    	  String lineSep = "";
    	  
    	  if(style.equals(Style.TEXT))
    		  lineSep = System.getProperty("line.separator");
    	  else lineSep = "<br/>";
    	  
    	  line = lineSep;
    	  
    	  for(StackTraceElement el : t.getStackTrace()) {
    		  line += MessageFormat.format("{0}.{1} ({2}:{3}) ",
    		  		el.getClassName(),el.getMethodName(), el.getFileName(), el.getLineNumber());
    	      line += lineSep;
    	  }
    	  return line;
      }
      
      public static String getMessages(Throwable throwable, Style style){
    	  
    	  Throwable t = throwable;
    	  String line = "";
    	  String lineSep = "";
    	  
    	  if(style.equals(Style.TEXT))
    		  lineSep = System.getProperty("line.separator");
    	  else lineSep = "<br/>";
    	  
    	  line = lineSep;
    	  
    	  while(t != null){
    		  line += MessageFormat.format("{0} -> {1}", t.getClass().getCanonicalName(),
    				  t.getMessage());
    		  line += lineSep;
    		  t = t.getCause();
    	  }
    	  return line;
      }
 }
