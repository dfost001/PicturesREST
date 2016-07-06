package service;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
//import javax.validation.groups.Default;

import client.model.ErrorEnum;
import exceptions.ClientException;
import model.PicSample;

public class BeanValidator {
	
	private Validator validator = null;	
	
	public BeanValidator() {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
	}
	
	public void validatePicSample(PicSample picSample) throws ClientException {
		
		Set<ConstraintViolation<PicSample>> set = validator.validate(picSample);
		
		if(set == null || set.size() == 0)
			return;
		
		String info = doMessage(set);		
		
		ClientException client = this.initException(info);
		
		throw client;
	}
	
	private String doMessage(Set<ConstraintViolation<PicSample>> set) {
		
		String info = "";
		
		for(ConstraintViolation<PicSample> v : set) {
			String field = this.getName(v.getPropertyPath());
			String message = v.getMessage();
			info += info + field + ":" + message + "; ";
		}
		
		return info.substring(0, info.length() - 1);
	}
	
	private String getName(Path path){
		String name = "";
		Iterator<Node> node = path.iterator();
		while(node.hasNext())
			name +=  node.next().getName() + ".";
		return name.substring(0, name.length() - 1);
	}
	
	private ClientException initException(String detail) {
		
		ClientException client = new ClientException(ErrorEnum.INVALID_COLUMN.name());
		
		client.setDetail(detail);
		
		client.setClientInfo(detail);
		
		return client;
		
	}

}
