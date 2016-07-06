package exception_handlers;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.persistence.PersistenceException;

import error.logger.EhrLogger;
import error.logger.LoggerResource;



@Provider
public class PersistenceExceptionHandler implements ExceptionMapper<PersistenceException>{

	@Override
	public Response toResponse(PersistenceException e) {
		
		Logger logger = LoggerResource.produceLogger(PersistenceExceptionHandler.class);
		
		e.fillInStackTrace();
		
		if(logger.getHandlers() != null && logger.getHandlers().length > 0)	{	
			
		     EhrLogger.log(logger, e, EhrLogger.Style.TEXT);
		}
		else {
			org.jboss.logging.Logger.getLogger(this.getClass()).info("Error obtaining FileHandler for logger.");
		}
		
		//String debugging = "ExceptionMapper:" + e.getMessage();
		
		//Response response = Response.status(500).entity(debugging).type("text/plain").build();
		
		String debugging = getHtml(e);
		Response response = Response.status(500).entity(debugging).type("text/html").build();
		return response;
	}
	
	private String getHtml(PersistenceException e) {
		String messages = EhrLogger.getMessages(e, EhrLogger.Style.HTML);
		String trace = EhrLogger.getTrace(e, EhrLogger.Style.HTML);
		return "Messages:<br/>" + messages + "<br/>Trace:<br/>" + trace;
	}

}
