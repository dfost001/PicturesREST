package service;

import java.util.List;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;

import client.model.ClientResponse;
import client.model.ErrorEnum;
import model.Customer;
import model.PicSample;
import ejb.PicturesFacade;
import exceptions.ClientException;

public class CalculateInterval {
	
	private int calculatedTo;
	private int calculatedFrom;
	private List<PicSample> picList = null;
	private int total;		

	@EJB(beanName="PicturesFacade")
	private PicturesFacade picFacade;
	
	public CalculateInterval(PicturesFacade facade){
		picFacade = facade;
	}
	
	public ClientResponse calculateNext(int fromIndex, int toIndex, Customer c)
			throws ClientException, PersistenceException{
		
		if(fromIndex == -1 || toIndex == -1)
			throw new ClientException(ErrorEnum.INVALID_INDICES.name(), 
					"Indices cannot have a negative value.");
		
		if(fromIndex > toIndex)
			throw new ClientException(ErrorEnum.INVALID_INDICES.name(), 
					"'" + fromIndex + "' is greater than '" + toIndex + "'");
		
		this.calculatedFrom = toIndex + 1; 
		this.calculatedTo = calculatedFrom + 2; // Three more records        
		
		total = picFacade.count(c.getId());
		
		int lastIdx = total - 1;
		 
		if(calculatedFrom > lastIdx){ //throw No more records
			throw new ClientException(ErrorEnum.NO_MORE_RECORDS.name())	;	
		}
		else if(calculatedTo > lastIdx)
			calculatedTo = lastIdx;
		
		picList = picFacade.findRange(c.getId(), new int[] {calculatedFrom, calculatedTo});
		
		return initClientResponse(c);
		
	}
	
	public ClientResponse initInterval(Customer c) throws PersistenceException, ClientException{
		
		int from = -1;
		int to = -1;		
		
		 picList = null;
		 
		total = picFacade.count(c.getId());	 
		
		if(total >= 3){				
			to = 2;
			from = 0;
		}
		else if(total > 0){
			to = total - 1;
			from = 0;
		}
		if(total > 0){
		    picList = picFacade.findRange(c.getId(), new int[] { from, to });
		}
		
		calculatedTo = to;
		calculatedFrom = from;
		
		return initClientResponse(c);
		
	}
	
	private ClientResponse initClientResponse(Customer c){
		ClientResponse client = new ClientResponse();
		client.setIndexFrom(calculatedFrom);
		client.setIndexTo(calculatedTo);
		client.setTotal(total);
		client.setPicture(picList);
		client.setCustomer(c);
		return client;
	}

}
