package client.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlType;
//import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name="clientErrorResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientErrorResponse {

	@XmlElement(required=true)
	private ErrorEnum errorEnum;
	
	@XmlElement(required=true)
	private String message;	
	
	@XmlElement(required=false)
	private String detail = "";
	
	@XmlElement(required=false)
	private String info = "";
	
	/*
	 * Construct with the String value of enum
	 */
	

	public ErrorEnum getErrorEnum() {
		return errorEnum;
	}

	public void setErrorEnum(ErrorEnum errorEnum) {
		this.errorEnum = errorEnum;
	}		

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}	
	
	
	
} //end class
