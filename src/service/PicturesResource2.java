package service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.GenericEntity;

import model.Customer;
import model.PicSample;

import org.jboss.logging.Logger;

import client.model.ClientResponse;
import client.model.ErrorEnum;
import ejb.PicturesFacade;
import exceptions.ClientException;

@Path("/pictures2")
public class PicturesResource2 {
	
 private static Logger logger = Logger.getLogger("service.PicturesResource2");
	
	@EJB
	private PicturesFacade picFacade;
	
	@GET
	@Produces("text/plain")
	public String helloTest(){
		return "Hello";
	}
	
	
	@Path("/insert2/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public GenericEntity<ClientResponse> insertPicSample2(@PathParam("id") Integer id,
			PicSample pic)
	                throws  ClientException, PersistenceException, Exception, Error {
		
		Customer c = null;
		
		ClientResponse client = null;	
		
        if(pic == null)
        	throw new ClientException(ErrorEnum.INVALID_UPLOAD_OBJECT.name()); //INVALID_UPLOAD_OBJECT  
        
        c = picFacade.customerById(id);        
         
        new BeanValidator().validatePicSample(pic);
        
        pic.setOriginalPicName(pic.getPicName());      
		
        new PicSampleResize().process(pic);
            
        List<PicSample> pics = picFacade.findSampleByName(pic.getOriginalPicName(), c.getId());
        
            String s = "";
            
            if(pics != null && pics.size() > 0) {         	
			    s = this.adjustName(pics.size(), pic.getPicName());
			   
			    logger.info("PicturesResource2#insertPicSample2 " + pic.getPicName());
            }
            else {
            	s = this.adjustName(0, pic.getPicName());
            }			
            pic.setPicName(s);
            
			picFacade.insertPicSample2(c, pic);
			
			client = new CalculateInterval(picFacade).initInterval(c);		
			
				    
		GenericEntity<ClientResponse> entity = new GenericEntity<ClientResponse>(client) {};
		
		return entity;
	} 
	
	@GET
	@Path("/firstInterval/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public GenericEntity<ClientResponse> init(@PathParam("id") Integer id)
	             throws ClientException, PersistenceException, Exception, Error {	
		
		ClientResponse client = null;

		try {
			Customer c = picFacade.customerById(id);
			client = new CalculateInterval(picFacade).initInterval(c);	
			
		} catch (ClientException ex) {
			ex.setDetail("init:" + ex.getDetail());
			throw ex;
		} catch (PersistenceException ex) {
            throw ex;
		} catch (Exception ex) {  
			throw ex;
		} catch (Error e) {
			throw new Error("init:" + e.getMessage(), e);
		}
		return new GenericEntity<ClientResponse>(client) {};
	}	
	
	@GET
	@Path("/nextInterval/{id}/{from}/{to}")
	@Produces(MediaType.APPLICATION_XML)
	public GenericEntity<ClientResponse> nextInterval(
			@PathParam("id") Integer id,
			@PathParam("from") Integer from,
			@PathParam("to") Integer to) 
					throws ClientException, PersistenceException, Exception, Error{	
		
		ClientResponse client = null;

		try {
			Customer c = picFacade.customerById(id);
			
			client = new CalculateInterval(picFacade).calculateNext(from, to, c);
			
		} catch (ClientException ex) {
			ex.setDetail("nextInterval:" + ex.getDetail());
			throw ex;
		} catch (PersistenceException ex) {
            throw new PersistenceException("nextInterval:" + ex.getMessage(), ex);
		} catch (Exception ex){
			throw ex;
		} catch (Error e){
			throw e;
		}
		return new GenericEntity<ClientResponse>(client) {};
	}	
	
	@Path("deleteById/{customerId}")
	@DELETE
	@Produces(MediaType.APPLICATION_XML)
	public GenericEntity<ClientResponse> deletePicList(
			@PathParam("customerId") Integer id,
			@QueryParam("delete") List<Integer> deleteList)
			throws ClientException, PersistenceException, Exception, Error {

		//String debug = "";			
		
		if(deleteList == null || deleteList.isEmpty())
			throw new ClientException(ErrorEnum.INVALID_UPLOAD_OBJECT.name());
		
		Customer customer = picFacade.customerById(id);
		
		List<PicSample> listToDelete = new ArrayList<PicSample>();
		
		for(int i = 0; i < deleteList.size(); i++){
			PicSample sample = picFacade.findPicSample(deleteList.get(i));
			listToDelete.add(sample);
		}
		
		ClientResponse client = null;				

		picFacade.deletePicSample(deleteList, customer.getId());	
		
		picFacade.adjustNamesTransaction(listToDelete);		

		client = new CalculateInterval(picFacade)
					.initInterval(customer);	

		return new GenericEntity<ClientResponse>(client) {
		};
	}
	
	@Path("/debugInsert")
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public void debugInsert(PicSample pic)
			throws ClientException, PersistenceException, Exception, Error {
		
			new PicSampleResize().process(pic);
			PicSample picSource = picFacade.findPicSample(11);			
		    String msg = new CompareUpload().compareResized(
		    		pic.getPhoto(),
		    		picSource.getPhoto());
		    logger.info("PicturesResource2#debugInsert: " + msg);
	}
	
	private String adjustName(int rows, String name){
		int position = name.indexOf("."); //all periods already replaced in PicSampleResize
		Integer n = new Integer(rows + 1);
		String edited = name.substring(0,position) + "_" + n.toString() + "." 
		   + name.substring(position + 1);
		return edited;
	}
	
 /*	@Path("/debugInsert")
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public void debugInsert(PicSample pic) throws IOException {
		byte [] upload = pic.getPhoto();
		String msg = new CompareUpload().compare(upload);
		logger.info("PicturesResource2#debugInsert: " + msg);
	}
*/	
	
	
/*	private Response makeResponse(Object entity){
		Response resp = Response.status(200).type("application/xml").entity(entity).build();
		return resp;
	}*/


}
