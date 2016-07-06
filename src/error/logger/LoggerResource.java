package error.logger;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
//import java.util.logging.XMLFormatter;
import java.util.logging.StreamHandler;

public class LoggerResource {
	
	private static FileHandler fh = null;
	private static StreamHandler strm = null;
	private final static String pattern = "%h/logs/DebugPicturesREST/PicturesREST%g-%u.log";
	private static final Integer byteLimit = 10000;
	private static final Integer fileCount = 10;
	private static final Boolean append = false;
	
	private static void initFileHandler() throws SecurityException, IOException{			  	   
		
		    fh = new FileHandler(pattern,byteLimit,fileCount,append);
		    fh.setFormatter(new SimpleFormatter());		
	}
	
	@SuppressWarnings("rawtypes")
	public static Logger produceLogger(Class cl) {

		String name = cl.getCanonicalName();
		Logger logger = Logger.getLogger(name);
		String exception = "";

		try {
			if (fh == null)
				initFileHandler();
		} catch (SecurityException e) {

			strm = new StreamHandler(System.out, new SimpleFormatter());
			exception = "Security Exception";

		} catch (IOException io) {
			strm = new StreamHandler(System.out, new SimpleFormatter());
			exception = "IOException " + io.getMessage();

		}
		if (fh != null)
			logger.addHandler(fh);
		else {
			logger.addHandler(strm);
			logger.info("Error creating FileHandler " + exception);
		}
		return logger;
	}

}
