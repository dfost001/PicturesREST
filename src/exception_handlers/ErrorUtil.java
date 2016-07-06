package exception_handlers;

import javax.ejb.EJBException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import org.hibernate.exception.*;

import client.model.ErrorEnum;
import exceptions.ClientException;

import org.jboss.logging.Logger;

public class ErrorUtil {
	
	private static Logger logger = Logger.getLogger(ErrorUtil.class);
	
	public static ClientException initTemporaryDb(Exception ex, String method){
		
		ErrorEnum err = ErrorEnum.TEMPORARY_DATABASE_ERROR;
		
        ClientException client = new ClientException(err.value());
		
		client.setDetail(method + ":" + ex.getMessage());	
		
		return client;	
	}
	
	public static ClientException initInvalidId(NoResultException ex, int id){
		
        ErrorEnum err = ErrorEnum.INVALID_CUSTOMER_ID;
		
		String info = "Customer Id '" + id + "' cannot be found.";
		
		ClientException client = new ClientException(err.value());
		
		client.setDetail(ex.getMessage());
		
		client.setClientInfo(info);
		
		return client;		
		
	}
	
	public static ClientException initDeleteError(String id){
		
		ErrorEnum err = ErrorEnum.DELETE_ERROR;
		
		String info = "At least one photo may have been previously deleted: photo #" + id;
		
		ClientException client = new ClientException(err.value());
		
		client.setDetail("EntityNotFoundException");
		
        client.setClientInfo(info);
		
		return client;	
	}
	
	/*
	 * Logic: Only a base class will be assignable from a base class
	 * A derived class will not be assignable from a base class.
	 * A base class exception is undefined, and may be temporary.
	 * 
	 */
	public static boolean  isApplicationException(Exception ex){
		Throwable cause = ErrorUtil.findFirstCause(ex);
		logger.info("cause=" + cause.getClass().getCanonicalName());
		logger.info("message=" + cause.getMessage());
		if(cause.getClass().equals(LockAcquisitionException.class))
			return false;
		else if(cause.getClass().equals(LockTimeoutException.class))
			return false;
		else if(cause.getClass().equals(JDBCConnectionException.class))
			return false;
		
		/*boolean assignable = cause.getClass().isAssignableFrom(PersistenceException.class);
		boolean assignable2 = cause.getClass().isAssignableFrom(EJBException.class);
		logger.info("Cause is assignable from PersistenceException=" + assignable);
		logger.info("Cause is assignable from EJBException=" + assignable2);*/
		
		else if(cause.getClass().isAssignableFrom(PersistenceException.class))//base is assignable from base
			return false;
		else if(cause.getClass().isAssignableFrom(EJBException.class))
			return false;
		return true;
	}
	
	private static Throwable findFirstCause(Throwable t){
		Throwable temp = t;
		Throwable cause = null;
		while(temp != null){
			System.out.println("Cause trace:" + temp.getClass().getCanonicalName());			
			cause = temp;
			if(isHibernateException(cause))
				break;
			temp = temp.getCause();
		}
		return cause;
	}
	
	private static boolean isHibernateException(Throwable ex){		
		
		String name = ex.getClass().getName();
		
		//logger.info("Cannonical Name=" + name);
		
		if(name.indexOf("org.hibernate.exception") > -1)
			return true;
		return false;
	}

}
